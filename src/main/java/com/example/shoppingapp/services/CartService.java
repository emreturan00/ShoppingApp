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
                        float currentQuantity = resultSet.getFloat("quantity");
                        float newQuantity = currentQuantity + cartItem.getQuantity();

                        String updateQuery = "UPDATE cartinfo SET quantity = ? WHERE user_id = ? AND product_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setFloat(1, newQuantity);
                            updateStatement.setInt(2, cartItem.getUserId());
                            updateStatement.setInt(3, cartItem.getProduct().getProductId());
                            updateStatement.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO cartinfo (user_id, product_id, quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setInt(1, cartItem.getUserId());
                            insertStatement.setInt(2, cartItem.getProduct().getProductId());
                            insertStatement.setFloat(3, cartItem.getQuantity());
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                        float quantity = resultSet.getFloat("quantity");

                        Product product = new Product();
                        product.setProductId(product_id);
                        product.setName(resultSet.getString("name"));
                        product.setType(resultSet.getString("type"));
                        product.setStock(resultSet.getFloat("stock"));
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
            e.printStackTrace();
        }

        return cartItems;
    }


    public void updateCartItemQuantity(int productId, float newQuantity) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String updateQuery = "UPDATE cartinfo SET quantity = ? WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setFloat(1, newQuantity);
                updateStatement.setInt(2, UserSession.getInstance().getUserId());
                updateStatement.setInt(3, productId);

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromCart(int productId) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String removeQuery = "DELETE FROM cartinfo WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement removeStatement = connection.prepareStatement(removeQuery)) {
                removeStatement.setInt(1, UserSession.getInstance().getUserId());
                removeStatement.setInt(2, productId);

                removeStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void completeCart(String deliveryTime, String carrier) {
        try (Connection connection = databaseAdapter.getConnection()) {
            connection.setAutoCommit(false);

            try {
                if(updateStockAndPrice()){
                    System.out.println(updateStockAndPrice());
                    moveItemsToOrders(deliveryTime, carrier);
                    clearUserCart();
                    connection.commit();
                }

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int moveItemsToOrders(String deliveryTime, String carrier) throws SQLException {
        String insertOrderQuery = "INSERT INTO orderinfo (userId, orderTime, deliveryTime, carrier, isDelivered, totalCost, products) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertOrderStatement = databaseAdapter.getConnection().prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
            int userId = UserSession.getInstance().getUserId();
            insertOrderStatement.setInt(1, userId);
            insertOrderStatement.setString(2, deliveryTime);
            insertOrderStatement.setString(3, carrier);
            insertOrderStatement.setBoolean(4, false);

            double totalCost = calculateTotalCost(databaseAdapter.getConnection());
            insertOrderStatement.setDouble(5, totalCost);

            List<Integer> productIds = getCartProductIds(userId, databaseAdapter.getConnection());

            String productsCsv = productIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            insertOrderStatement.setString(6, productsCsv);

            insertOrderStatement.executeUpdate();

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
        String calculateTotalCostQuery = "SELECT SUM(p.price * c.quantity + (p.price * c.quantity/100)) AS total_cost FROM cartinfo c JOIN productinfo p ON c.product_id = p.id WHERE c.user_id = ?";
        try (PreparedStatement calculateTotalCostStatement = connection.prepareStatement(calculateTotalCostQuery)) {
            calculateTotalCostStatement.setInt(1, UserSession.getInstance().getUserId());

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
                        "WHERE (p.stock - c.quantity) >= 0 AND p.id = c.product_id";

        try (PreparedStatement updateStockAndPriceStatement = databaseAdapter.getConnection().prepareStatement(updateStockAndPriceQuery)) {
            int rowsAffected = updateStockAndPriceStatement.executeUpdate();
            System.out.println(rowsAffected);

            if (rowsAffected > 0) {
                return true;
            }
        }
        return false;
    }




    private void clearUserCart() throws SQLException {
        String clearCartQuery = "DELETE FROM cartinfo WHERE user_id = ?";
        try (PreparedStatement clearCartStatement = databaseAdapter.getConnection().prepareStatement(clearCartQuery)) {
            clearCartStatement.setInt(1, UserSession.getInstance().getUserId());

            clearCartStatement.executeUpdate();
        }
    }

    public boolean checkStock(int productId, float cartQuantity) throws SQLException {
        String selectStockQuery = "SELECT stock FROM productinfo WHERE id = ?";

        try (PreparedStatement selectStockStatement = databaseAdapter.getConnection().prepareStatement(selectStockQuery)) {
            selectStockStatement.setInt(1, productId);

            try (ResultSet resultSet = selectStockStatement.executeQuery()) {
                if (resultSet.next()) {
                    float currentStock = resultSet.getFloat("stock");

                    return currentStock >= cartQuantity && cartQuantity > 0;
                } else {
                    System.out.println("Error: Product with ID " + productId + " not found.");
                    return false;
                }
            }
        }
    }


}
