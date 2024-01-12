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
            String query = "SELECT * FROM userinfo WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int loggedInUserId = resultSet.getInt("id");
                    String loggedInUsername = resultSet.getString("username");
                    String loggedInRole = resultSet.getString("role");
                    UserSession.getInstance().setUserData(loggedInUserId, loggedInUsername, loggedInRole);
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

    public boolean isStrongPassword(String password) {
        // Check if the password meets the criteria
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }

        if (!hasUppercase) {
            System.out.println("Password must contain at least one uppercase letter.");
        }

        if (!hasLowercase) {
            System.out.println("Password must contain at least one lowercase letter.");
        }

        if (!hasDigit) {
            System.out.println("Password must contain at least one digit.");
        }

        return hasUppercase && hasLowercase && hasDigit;
    }

    public boolean isUsernameTaken(String username) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT COUNT(*) FROM userinfo WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUserInfo(String newUsername, String newPassword, String newAddress) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "UPDATE userinfo SET username = ?, password = ?, adress = ? WHERE id = " + UserSession.getInstance().getUserId();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, newPassword);
                preparedStatement.setString(3, newAddress);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("User information updated successfully!");
                    return true;
                } else {
                    System.out.println("User not found or information not updated.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
