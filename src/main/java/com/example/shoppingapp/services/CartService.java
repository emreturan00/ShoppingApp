package com.example.shoppingapp.services;
import java.sql.Connection;
import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CartService {
    private DatabaseAdapter databaseAdapter;

    public CartService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public void addToCart(Product product, int quantity, String username) {
        try (Connection connection = databaseAdapter.getConnection()) {
            // Check if the product is already in the user's cart
            String checkQuery = "SELECT * FROM cartinfo WHERE user_id = (SELECT user_id FROM User WHERE username = ?) AND product_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, username);
                checkStatement.setInt(2, product.getProductName()); // Assuming there's a method to get the product ID
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Product is already in the cart, update quantity
                        int currentQuantity = resultSet.getInt("quantity");
                        int newQuantity = currentQuantity + quantity;

                        // Update the quantity in the cart
                        String updateQuery = "UPDATE cartinfo SET quantity = ? WHERE user_id = (SELECT user_id FROM User WHERE username = ?) AND product_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setString(2, username);
                            updateStatement.setInt(3, product.getProductName());
                            updateStatement.executeUpdate();
                        }
                    } else {
                        // Product is not in the cart, insert a new row
                        String insertQuery = "INSERT INTO cartinfo (user_id, product_id, quantity) VALUES ((SELECT user_id FROM User WHERE username = ?), ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, username);
                            insertStatement.setInt(2, product.getProductName());
                            insertStatement.setInt(3, quantity);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public List<CartItem> viewCart(String username) {
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT c.quantity, p.* FROM cartinfo c JOIN Product p ON c.product_id = p.product_id WHERE c.user_id = (SELECT user_id FROM User WHERE username = ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int quantity = resultSet.getInt("quantity");
                        int productId = resultSet.getInt("product_id");
                        String productName = resultSet.getString("Name");
                        String productType = resultSet.getString("type");
                        int stock = resultSet.getInt("stock");
                        double price = resultSet.getDouble("price");
                        String imageLocation = resultSet.getString("imageLocation");
                        int threshold = resultSet.getInt("threshold");

                        Product product = new Product(productName, productType, stock, price, imageLocation, threshold);
                        CartItem cartItem = new CartItem(product, quantity);
                        cartItems.add(cartItem);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return cartItems;
    }

    public void completeOrder(String username) {
        // Implement the logic to complete the order
        // This might involve moving the items from the cart to a separate table representing orders
        // and updating the product stock accordingly
        // ...
    }
}
