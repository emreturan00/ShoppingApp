package com.example.shoppingapp.services;
import java.sql.*;

import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;
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



    public List<CartItem> viewCart(int userId) {
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT c.quantity, p.* FROM cartinfo c JOIN productinfo p ON c.product_id = p.product_id WHERE c.user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int quantity = resultSet.getInt("quantity");

                        // Create a new CartItem with the retrieved quantity and Product information
                        Product product = new Product();
                        product.setProductId(resultSet.getInt("product_id"));
                        product.setName(resultSet.getString("name"));
                        product.setType(resultSet.getString("type"));
                        product.setStock(resultSet.getInt("stock"));
                        product.setPrice(resultSet.getDouble("price"));
                        product.setImageLocation(resultSet.getString("imageLocation"));
                        product.setThreshold(resultSet.getInt("threshold"));

                        CartItem cartItem = new CartItem();
                        cartItem.setUserId(userId);
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

    public void updateCartItemQuantity(int userId, int productId, int newQuantity) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Update the quantity in the cartinfo table
            String updateQuery = "UPDATE cartinfo SET quantity = ? WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, newQuantity);
                updateStatement.setInt(2, userId);
                updateStatement.setInt(3, productId);

                // Execute the update statement
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void removeProductFromCart(int userId, int productId) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Remove the product from the cartinfo table
            String removeQuery = "DELETE FROM cartinfo WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement removeStatement = connection.prepareStatement(removeQuery)) {
                removeStatement.setInt(1, userId);
                removeStatement.setInt(2, productId);

                // Execute the delete statement
                removeStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    public void completeCart(int userId) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            try {
                // Insert items from the cart into the orders table
                int orderId = moveItemsToOrders(userId, connection);

                // Update product stock based on the cart
                updateProductStock(userId, connection);

                // Clear the user's cart
                clearUserCart(userId, connection);

                // Commit the transaction
                connection.commit();
            } catch (SQLException e) {
                // Handle exceptions, log errors, and roll back the transaction in case of failure
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int moveItemsToOrders(int userId, Connection connection) throws SQLException {
        // Insert items from the cart into the orders table
        String insertOrderQuery = "INSERT INTO orderinfo (userId, orderTime, total_cost) VALUES (?, CURRENT_TIMESTAMP, ?)";
        try (PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertOrderStatement.setInt(1, userId);

            // Calculate total cost based on the cart items
            double totalCost = calculateTotalCost(userId, connection);
            insertOrderStatement.setDouble(2, totalCost);

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

    private double calculateTotalCost(int userId, Connection connection) throws SQLException {
        // Calculate total cost based on the cart items
        String calculateTotalCostQuery = "SELECT SUM(p.price * c.quantity) AS total_cost FROM cartinfo c JOIN productinfo p ON c.product_id = p.product_id WHERE c.user_id = ?";
        try (PreparedStatement calculateTotalCostStatement = connection.prepareStatement(calculateTotalCostQuery)) {
            calculateTotalCostStatement.setInt(1, userId);

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

    private void updateProductStock(int userId, Connection connection) throws SQLException {
        // Update product stock based on the cart
        String updateStockQuery = "UPDATE productinfo SET stock = stock - c.quantity FROM cartinfo c WHERE c.user_id = ? AND c.product_id = product.product_id";
        try (PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery)) {
            updateStockStatement.setInt(1, userId);

            // Execute the update statement
            updateStockStatement.executeUpdate();
        }
    }

    private void clearUserCart(int userId, Connection connection) throws SQLException {
        // Clear the user's cart
        String clearCartQuery = "DELETE FROM cartinfo WHERE user_id = ?";
        try (PreparedStatement clearCartStatement = connection.prepareStatement(clearCartQuery)) {
            clearCartStatement.setInt(1, userId);

            // Execute the delete statement
            clearCartStatement.executeUpdate();
        }
    }

}
