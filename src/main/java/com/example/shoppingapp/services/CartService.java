package com.example.shoppingapp.services;
import java.sql.*;

import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartService {
    private DatabaseAdapter databaseAdapter;

    public CartService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public void addToCart(CartItem cartItem) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String checkQuery = "SELECT * FROM cartinfo WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, cartItem.getUserId());
                checkStatement.setInt(2, cartItem.getProduct().getProductId());
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Product is already in the cart, update quantity
                        int currentQuantity = resultSet.getInt("quantity");
                        int newQuantity = currentQuantity + cartItem.getQuantity();

                        // Update the quantity in the cart
                        String updateQuery = "UPDATE cartinfo SET quantity = ? WHERE user_id = ? AND product_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, cartItem.getUserId());
                            updateStatement.setInt(3, cartItem.getProduct().getProductId());
                            updateStatement.executeUpdate();
                        }
                    } else {
                        // Product is not in the cart, insert a new row
                        String insertQuery = "INSERT INTO cartinfo (user_id, product_id, quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setInt(1, cartItem.getUserId());
                            insertStatement.setInt(2, cartItem.getProduct().getProductId());
                            insertStatement.setInt(3, cartItem.getQuantity());
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }



    public List<CartItem> viewCart() {
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT c.user_id, c.product_id, c.quantity, p.* FROM cartinfo c JOIN productinfo p ON c.product_id = p.id WHERE c.user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, UserSession.getInstance().getUserId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int user_id = resultSet.getInt("user_id");
                        int product_id = resultSet.getInt("product_id");
                        int quantity = resultSet.getInt("quantity");

                        // Create a new CartItem with the retrieved quantity and Product information
                        Product product = new Product();
                        product.setProductId(product_id);
                        product.setName(resultSet.getString("name"));
                        product.setType(resultSet.getString("type"));
                        product.setStock(resultSet.getInt("stock"));
                        product.setPrice(resultSet.getDouble("price"));
                        product.setImageLocation(resultSet.getString("imageLocation"));
                        product.setThreshold(resultSet.getInt("threshold"));

                        CartItem cartItem = new CartItem();
                        cartItem.setUserId(user_id);
                        cartItem.setProduct(product);
                        cartItem.setQuantity(quantity);

                        cartItems.add(cartItem);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return cartItems;
    }


    public void updateCartItemQuantity(int productId, int newQuantity) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Update the quantity in the cartinfo table
            String updateQuery = "UPDATE cartinfo SET quantity = ? WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, newQuantity);
                updateStatement.setInt(2, UserSession.getInstance().getUserId());
                updateStatement.setInt(3, productId);

                // Execute the update statement
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void removeProductFromCart(int productId) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Remove the product from the cartinfo table
            String removeQuery = "DELETE FROM cartinfo WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement removeStatement = connection.prepareStatement(removeQuery)) {
                removeStatement.setInt(1, UserSession.getInstance().getUserId());
                removeStatement.setInt(2, productId);

                // Execute the delete statement
                removeStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    public void completeCart(String deliveryTime, String carrier) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            try {
                // Insert items from the cart into the orders table
                int orderId = moveItemsToOrders(deliveryTime, carrier);

                // Update product stock based on the cart
                if(updateStockAndPrice()){

                    // Clear the user's cart
                    clearUserCart();

                    // Commit the transaction
                    connection.commit();
                }

            } catch (SQLException e) {
                // Handle exceptions, log errors, and roll back the transaction in case of failure
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int moveItemsToOrders(String deliveryTime, String carrier) throws SQLException {
        // Insert items from the cart into the orders table
        String insertOrderQuery = "INSERT INTO orderinfo (userId, orderTime, deliveryTime, carrier, isDelivered, totalCost, products) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertOrderStatement = databaseAdapter.getConnection().prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
            // Set values for the columns
            int userId = UserSession.getInstance().getUserId();
            insertOrderStatement.setInt(1, userId);
            insertOrderStatement.setString(2, deliveryTime);
            insertOrderStatement.setString(3, carrier);
            insertOrderStatement.setBoolean(4, false);

            // Calculate total cost based on the cart items
            double totalCost = calculateTotalCost(databaseAdapter.getConnection());
            insertOrderStatement.setDouble(5, totalCost);

            // Retrieve the list of product IDs from the cart
            List<Integer> productIds = getCartProductIds(userId, databaseAdapter.getConnection());

            // Convert the list of product IDs to a CSV string
            String productsCsv = productIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            insertOrderStatement.setString(6, productsCsv);

            // Execute the insert statement
            insertOrderStatement.executeUpdate();

            // Get the generated order ID
            try (ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated order ID.");
                }
            }
        }
    }

    private List<Integer> getCartProductIds(int userId, Connection connection) throws SQLException {
        // Implement logic to retrieve the list of product IDs from the cart
        List<Integer> productIds = new ArrayList<>();
        String query = "SELECT product_id FROM cartinfo WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    productIds.add(resultSet.getInt("product_id"));
                }
            }
        }

        return productIds;
    }



    private double calculateTotalCost(Connection connection) throws SQLException {
        // Calculate total cost based on the cart items
        String calculateTotalCostQuery = "SELECT SUM(p.price * c.quantity + (p.price * c.quantity/100)) AS total_cost FROM cartinfo c JOIN productinfo p ON c.product_id = p.id WHERE c.user_id = ?";
        try (PreparedStatement calculateTotalCostStatement = connection.prepareStatement(calculateTotalCostQuery)) {
            calculateTotalCostStatement.setInt(1, UserSession.getInstance().getUserId());

            // Execute the query
            try (ResultSet resultSet = calculateTotalCostStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total_cost");
                } else {
                    throw new SQLException("Failed to calculate total cost.");
                }
            }
        }
    }


    private boolean updateStockAndPrice() throws SQLException {
        String updateStockAndPriceQuery =
                "UPDATE productinfo p " +
                        "JOIN cartinfo c ON p.id = c.product_id " +
                        "SET p.stock = CASE WHEN (p.stock - c.quantity) >= 0 THEN (p.stock - c.quantity) ELSE -1 END, " +
                        "    p.price = CASE " +
                        "                 WHEN (p.stock - c.quantity) <= p.threshold AND NOT p.doubled_price THEN p.price * 2 " +
                        "                 ELSE p.price " +
                        "             END, " +
                        "    p.doubled_price = CASE WHEN (p.stock - c.quantity) <= p.threshold THEN true ELSE p.doubled_price END " +
                        "WHERE (p.stock - c.quantity) >= 0";




        try (PreparedStatement updateStockAndPriceStatement = databaseAdapter.getConnection().prepareStatement(updateStockAndPriceQuery)) {
            // Execute the update statement
            int rowsAffected = updateStockAndPriceStatement.executeUpdate();

            // Check if any rows were affected (i.e., the update was successful)
            if (rowsAffected > 0) {
                // Query to retrieve the updated stock values for the affected products
                String selectStockQuery = "SELECT id, stock FROM productinfo p WHERE EXISTS " +
                        "(SELECT 1 FROM cartinfo c WHERE p.id = c.product_id AND (p.stock - c.quantity) >= 0)";

                try (PreparedStatement selectStockStatement = databaseAdapter.getConnection().prepareStatement(selectStockQuery)) {
                    // Execute the select statement
                    try (ResultSet resultSet = selectStockStatement.executeQuery()) {
                        while (resultSet.next()) {
                            int productId = resultSet.getInt("id");
                            int updatedStock = resultSet.getInt("stock");

                            // Print or handle the updated stock for each affected product
                            System.out.println("Product ID: " + productId + ", Updated Stock: " + updatedStock);

                            // Return false if any updated stock is < 0
                            if (updatedStock < 0) {
                                return false;
                            }
                        }

                        // Return true if all updated stocks are >= 0
                        return true;
                    }
                }
            }
        }

        // Return false if the update was not successful or the stock is < 0
        return false;
    }







    private void clearUserCart() throws SQLException {
        // Clear the user's cart
        String clearCartQuery = "DELETE FROM cartinfo WHERE user_id = ?";
        try (PreparedStatement clearCartStatement = databaseAdapter.getConnection().prepareStatement(clearCartQuery)) {
            clearCartStatement.setInt(1, UserSession.getInstance().getUserId());

            // Execute the delete statement
            clearCartStatement.executeUpdate();
        }
    }



//    public void cancelOrder(int orderId) throws SQLException {
//        String cancelOrderQuery =
//                "UPDATE productinfo p " +
//                        "JOIN orderinfo od ON p.id = od.product_id " +
//                        "SET p.stock = p.stock + od.quantity, " +
//                        "    p.doubled_price = CASE WHEN p.stock > p.threshold THEN p.doubled_price ELSE false END " +
//                        "WHERE od.orderId = ?";
//
//        try (PreparedStatement cancelOrderStatement = databaseAdapter.getConnection().prepareStatement(cancelOrderQuery)) {
//            // Set the order ID parameter
//            cancelOrderStatement.setInt(1, orderId);
//
//            // Execute the cancel order statement
//            int rowsAffected = cancelOrderStatement.executeUpdate();
//
//            // Print or handle the result
//            System.out.println(rowsAffected + " rows updated for canceled order: " + orderId);
//        } catch (SQLException e) {
//            // Handle the exception
//            e.printStackTrace();
//        }
//    }


}
