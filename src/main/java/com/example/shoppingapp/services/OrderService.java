package com.example.shoppingapp.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;

public class OrderService {
    private DatabaseAdapter databaseAdapter;

    public OrderService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public List<Order> viewOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orderinfo";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int orderId = resultSet.getInt("orderId");
                int userId = resultSet.getInt("userId");
                String orderTime = resultSet.getString("orderTime");
                String deliveryTime = resultSet.getString("deliveryTime");
                String carrier = resultSet.getString("carrier");
                boolean isDelivered = resultSet.getBoolean("isDelivered");
                double totalCost = resultSet.getDouble("totalCost");
                String productIdsCSV = resultSet.getString("products");
                boolean isSelected = resultSet.getBoolean("isSelected");

                List<Integer> productIds = convertCSVToList(productIdsCSV);

                Order order = new Order(orderId, UserSession.getInstance().getUserId(), orderTime, deliveryTime, productIds.toString(), carrier, isDelivered, totalCost,isSelected);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private List<Integer> convertCSVToList(String csv) {
        List<Integer> list = new ArrayList<>();
        if (csv != null && !csv.isEmpty()) {
            String[] tokens = csv.split(",");
            for (String token : tokens) {
                list.add(Integer.parseInt(token.trim()));
            }
        }
        return list;
    }


    public List<Order> viewOrdersbyid() {
        List<Order> orders = new ArrayList<>();
        int id = UserSession.getInstance().getUserId();

        String query = "SELECT * FROM orderinfo WHERE userID = ? ";
        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

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

                    List<Integer> productIds = convertCSVToList(productIdsCSV);

                    Order order = new Order(orderId, UserSession.getInstance().getUserId(), orderTime, deliveryTime, productIds.toString(), carrier, isDelivered, totalCost,isSelected);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

}

