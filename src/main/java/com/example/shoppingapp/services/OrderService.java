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

    public List<Order> viewOrders(String carrierUsername) {
        List<Order> orders = new ArrayList<>();
        return orders;
    }

    public List<Order> viewAllOrders() {
        List<Order> orders = new ArrayList<>();

        return orders;
    }
}

