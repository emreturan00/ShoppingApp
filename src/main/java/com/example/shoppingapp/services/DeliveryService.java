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

    public List<Order> viewAvailabledeliveries(String carrierName) {
        List<Order> selectedDeliveries = new ArrayList<>();

        String query = "SELECT * FROM orderinfo WHERE carrier = ? AND isSelected = 0 AND isdelivered = 0";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter value using setString
            statement.setString(1, carrierName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    String orderTime = resultSet.getString("orderTime");
                    String deliveryTime = resultSet.getString("deliveryTime");
                    String carrier = resultSet.getString("carrier");
                    boolean isDelivered = resultSet.getBoolean("isDelivered");
                    double totalCost = resultSet.getDouble("totalCost");
                    String productIdsCSV = resultSet.getString("products");
                    boolean isSelected = resultSet.getBoolean("isSelected");

                    // Convert product IDs from CSV to a list of integers
                    List<Integer> productIds = convertCSVToList(productIdsCSV);

                    // Create an Order object and add it to the list
                    Order order = new Order(orderId, UserSession.getInstance().getUserId(), orderTime, deliveryTime, productIds.toString(), carrier, isDelivered, totalCost,isSelected);
                    selectedDeliveries.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return selectedDeliveries;
    }

    public List<Order> viewSelecteddeliveries(String carrierName) {
        List<Order> selectedDeliveries = new ArrayList<>();

        String query = "SELECT * FROM orderinfo WHERE carrier = ? AND isSelected = 1 AND isdelivered = 0";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter value using setString
            statement.setString(1, carrierName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    String orderTime = resultSet.getString("orderTime");
                    String deliveryTime = resultSet.getString("deliveryTime");
                    String carrier = resultSet.getString("carrier");
                    boolean isDelivered = resultSet.getBoolean("isDelivered");
                    double totalCost = resultSet.getDouble("totalCost");
                    String productIdsCSV = resultSet.getString("products");
                    boolean isSelected = resultSet.getBoolean("isSelected");

                    // Convert product IDs from CSV to a list of integers
                    List<Integer> productIds = convertCSVToList(productIdsCSV);

                    // Create an Order object and add it to the list
                    Order order = new Order(orderId, UserSession.getInstance().getUserId(), orderTime, deliveryTime, productIds.toString(), carrier, isDelivered, totalCost,isSelected);
                    selectedDeliveries.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return selectedDeliveries;
    }

    public List<Order> viewCompleteddeliveries(String carrierName) {
        List<Order> selectedDeliveries = new ArrayList<>();

        String query = "SELECT * FROM orderinfo WHERE carrier = ? AND isdelivered = 1";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter value using setString
            statement.setString(1, carrierName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    String orderTime = resultSet.getString("orderTime");
                    String deliveryTime = resultSet.getString("deliveryTime");
                    String carrier = resultSet.getString("carrier");
                    boolean isDelivered = resultSet.getBoolean("isDelivered");
                    double totalCost = resultSet.getDouble("totalCost");
                    String productIdsCSV = resultSet.getString("products");
                    boolean isSelected = resultSet.getBoolean("isSelected");

                    // Convert product IDs from CSV to a list of integers
                    List<Integer> productIds = convertCSVToList(productIdsCSV);

                    // Create an Order object and add it to the list
                    Order order = new Order(orderId, UserSession.getInstance().getUserId(), orderTime, deliveryTime, productIds.toString(), carrier, isDelivered, totalCost,isSelected);
                    selectedDeliveries.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return selectedDeliveries;
    }

    public List<Order> viewDeliveriesbycarriername(String carriername) {
        List<Order> deliveries = new ArrayList<>();

        String query = "SELECT * FROM orderinfo WHERE carrier = ?";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter value using setString
            statement.setString(1, carriername);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("orderId");
                    String orderTime = resultSet.getString("orderTime");
                    String deliveryTime = resultSet.getString("deliveryTime");
                    String carrier = resultSet.getString("carrier");
                    boolean isDelivered = resultSet.getBoolean("isDelivered");
                    double totalCost = resultSet.getDouble("totalCost");
                    String productIdsCSV = resultSet.getString("products");
                    boolean isSelected = resultSet.getBoolean("isSelected");

                    // Convert product IDs from CSV to a list of integers
                    List<Integer> productIds = convertCSVToList(productIdsCSV);

                    // Create an Order object and add it to the list
                    Order order = new Order(orderId, UserSession.getInstance().getUserId(), orderTime, deliveryTime, productIds.toString(), carrier, isDelivered, totalCost,isSelected);
                    deliveries.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return deliveries;
    }


    public static List<Integer> convertCSVToList(String csv) {
        List<Integer> result = new ArrayList<>();
        if (csv != null && !csv.isEmpty()) {
            String[] values = csv.split(",");
            for (String value : values) {
                try {
                    int intValue = Integer.parseInt(value.trim());
                    result.add(intValue);
                } catch (NumberFormatException e) {
                    // Handle the case where a non-integer value is encountered
                    System.err.println("Invalid integer value in CSV: " + value);
                }
            }
        }
        return result;

    }


    public void updateOrderSelection(int orderId) {
        String updateQuery = "UPDATE orderinfo SET isSelected = 1 WHERE orderId = ?";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameter value using setInt
            updateStatement.setInt(1, orderId);

            // Execute the update query
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Order selection updated successfully.");
            } else {
                System.out.println("Failed to update order selection.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }


    }


    public void updateOrderDelivery(int orderId) {
        String updateQuery = "UPDATE orderinfo SET isdelivered = 1 WHERE orderId = ?";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameter value using setInt
            updateStatement.setInt(1, orderId);

            // Execute the update query
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Order selection updated successfully.");
            } else {
                System.out.println("Failed to update order selection.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

    }
}
