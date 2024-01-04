package com.example.shoppingapp.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.shoppingapp.models.Delivery;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;

public class DeliveryService {
    private DatabaseAdapter databaseAdapter;

    public DeliveryService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public List<Order> viewDeliveries(String carrierUsername) {
        List<Order> deliveries = new ArrayList<>();

        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT * FROM orderinfo WHERE carrier = ? AND isdelivered = true";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, carrierUsername);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Order delivery = mapResultSetToOrder(resultSet);
                        deliveries.add(delivery);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return deliveries;
    }

    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        // Retrieve order details from the ResultSet and create an Order object
        int orderId = resultSet.getInt("orderId");
        String orderTime = resultSet.getString("orderTime");
        String deliveryTime = resultSet.getString("deliveryTime");
        String user = resultSet.getString("user");
        String carrier = resultSet.getString("carrier");
        boolean isDelivered = resultSet.getBoolean("isDelivered");
        double totalCost = resultSet.getDouble("totalCost");

        // Assuming there's a method to retrieve a list of products from the ResultSet
        List<Product> products = mapResultSetToProductList(resultSet);

        // Create an Order object with the retrieved details
        Order order = new Order(orderId, orderTime, deliveryTime, products, user, carrier, isDelivered, totalCost);
        return order;
    }

    // Helper method to map a ResultSet to a list of Product objects
    private List<Product> mapResultSetToProductList(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();

        // Assuming there's a method to retrieve a Product from the ResultSet
        while (resultSet.next()) {
            Product product = mapResultSetToProduct(resultSet);
            products.add(product);
        }

        return products;
    }

    // Helper method to map a ResultSet to a Product object
    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("productId");
        String productName = resultSet.getString("productName");
        String productType = resultSet.getString("productType");
        int stock = resultSet.getInt("stock");
        double price = resultSet.getDouble("price");
        String imageLocation = resultSet.getString("imageLocation");
        int threshold = resultSet.getInt("threshold");

        // Create a Product object with the retrieved details
        return new Product(productName, productType, stock, price, imageLocation, threshold);
    }


}
