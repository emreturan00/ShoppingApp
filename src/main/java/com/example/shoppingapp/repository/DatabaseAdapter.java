package com.example.shoppingapp.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseAdapter {
    Connection getConnection() throws SQLException;
    void closeConnection(Connection connection) throws SQLException;
    // Other database-related methods
}
