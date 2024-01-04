package com.example.shoppingapp.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionAdapter implements DatabaseAdapter {
    private String jdbcUrl = "jdbc:mysql://localhost:3306/shopping";
    private String username = "root";
    private String password = "Kutukutu11";

    public MySqlConnectionAdapter() {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
