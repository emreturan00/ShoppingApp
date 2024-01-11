package com.example.shoppingapp.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;

public class ProductService {
    private final DatabaseAdapter databaseAdapter;

    public ProductService(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public List<Product> viewAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();

        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT * FROM productinfo WHERE stock > 0";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("Name");
                    String type = resultSet.getString("type");
                    int stock = resultSet.getInt("stock");
                    double price = resultSet.getDouble("price");
                    String imageLocation = resultSet.getString("imageLocation");
                    int threshold = resultSet.getInt("threshold");


                    Product product = new Product(id,name, type, stock, price, imageLocation, threshold);
                    availableProducts.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return availableProducts;
    }

    public List<Product> getProductsByType(String productType) {
        List<Product> productsByType = new ArrayList<>();

        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "SELECT * FROM productinfo WHERE type = ? AND stock > 0";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productType);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("Name");
                        String type = resultSet.getString("type");
                        int stock = resultSet.getInt("stock");
                        double price = resultSet.getDouble("price");
                        String imageLocation = resultSet.getString("imageLocation");
                        int threshold = resultSet.getInt("threshold");

                        Product product = new Product(id, name, type, stock, price, imageLocation, threshold);
                        productsByType.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return productsByType;
    }



    public void addProduct(Product product) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "INSERT INTO productinfo (Name, type, stock, price, imageLocation, threshold) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setString(2, product.getType());
                preparedStatement.setFloat(3, product.getStock());
                preparedStatement.setDouble(4, product.getPrice());
                preparedStatement.setString(5, product.getImageLocation());
                preparedStatement.setInt(6, product.getThreshold());

                preparedStatement.executeUpdate();
                System.out.println("Product added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void removeProduct(String name) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "DELETE FROM productinfo WHERE Name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Product removed successfully!");
                } else {
                    System.out.println("Product not found or could not be removed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void updateProduct(Product product) {
        try (Connection connection = databaseAdapter.getConnection()) {
            String query = "UPDATE productinfo SET type = ?, stock = ?, price = ?, imageLocation = ?, threshold = ? WHERE Name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, product.getType());
                preparedStatement.setFloat(2, product.getStock());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setString(4, product.getImageLocation());
                preparedStatement.setInt(5, product.getThreshold());
                preparedStatement.setString(6, product.getName());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Product updated successfully!");
                } else {
                    System.out.println("Product not found or could not be updated.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
