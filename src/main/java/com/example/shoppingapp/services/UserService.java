package com.example.shoppingapp.services;
import com.example.shoppingapp.repository.DatabaseAdapter;

import java.sql.*;

public class UserService {

    private DatabaseAdapter databaseAdapter;

    public UserService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public boolean signUp(String username, String password, String role , String adress) {
        try (Connection connection = databaseAdapter.getConnection())  {
            String query = "INSERT INTO userinfo (username, password, role,adress) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);
                preparedStatement.setString(4, adress);
                preparedStatement.executeUpdate();
                System.out.println("User signed up successfully!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean signIn(String username, String password) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("User signed in successfully!");
                    return true;
                } else {
                    System.out.println("Invalid credentials. Sign in failed.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

}
