package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.models.User;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.OrderService;
import com.example.shoppingapp.services.ProductService;
import com.example.shoppingapp.services.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OwnerController {
    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    ProductService productService = new ProductService(databaseAdapter);
    OrderService orderService = new OrderService(databaseAdapter);

    @FXML
    private Tab ordersTab;
    @FXML
    private Tab reportsTab;

    @FXML
    private ScrollPane ordersScroll;
    @FXML
    private ScrollPane reportsScroll;
    @FXML
    private VBox ordersContainer;



    @FXML
    private TextField name;
    @FXML
    private TextField type;
    @FXML
    private TextField stock;
    @FXML
    private TextField price;
    @FXML
    private TextField imageLocation;
    @FXML
    private TextField threshold;


    @FXML
    private TextField nameDelete;

    @FXML
    private Button add;
    @FXML
    private Button deleteButton;
    @FXML
    private Button fireButton;

    @FXML
    private ChoiceBox<String> carrierBox;

    @FXML
    private Text gotouser;


    @FXML
    private TextField nameUpdate;
    @FXML
    private TextField typeUpdate;
    @FXML
    private TextField stockUpdate;
    @FXML
    private TextField priceUpdate;
    @FXML
    private TextField imageLocationUpdate;
    @FXML
    private TextField thresholdUpdate;
    @FXML
    private Button update;






    @FXML
    private void handleAdd(){
        Product product = new Product();
        product.setProductId(0);
        product.setName(name.getText());
        product.setStock(Integer.parseInt(stock.getText()));
        product.setType(type.getText());
        product.setThreshold(Integer.parseInt(threshold.getText()));
        product.setPrice(Double.parseDouble(price.getText()));
        product.setImageLocation(imageLocation.getText());

        productService.addProduct(product);

        name.clear();
        type.clear();
        stock.clear();
        price.clear();
        imageLocation.clear();
        threshold.clear();



    }


    @FXML
    private void handleDelete(){
        System.out.println(nameDelete.getText());

        productService.removeProduct(nameDelete.getText());
        nameDelete.clear();

    }

    @FXML
    private void handleUpdate(){
        Product newProduct = new Product(0,nameUpdate.getText(),
                typeUpdate.getText(),Float.parseFloat(stockUpdate.getText()),
                Double.parseDouble(priceUpdate.getText()),imageLocationUpdate.getText(),
                Integer.parseInt(thresholdUpdate.getText()));

        productService.updateProduct(newProduct);
        nameUpdate.clear();
        typeUpdate.clear();
        stockUpdate.clear();
        priceUpdate.clear();
        imageLocationUpdate.clear();
        thresholdUpdate.clear();


    }

    @FXML
    private void handleGotouser(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CustomerSettings.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            // Create a new container (e.g., BorderPane) and set the loaded content as its center
            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setScene(new Scene(signUpContainer));

            signUpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void initialize() {
        gotouser.setText("USER: " + UserSession.getInstance().getUsername());
        displayProducts(ordersScroll, ordersContainer, orderService.viewOrders());
        ObservableList<String> typeOptions = FXCollections.observableArrayList("emrekargo", "suratkargo", "ups");

        carrierBox.setItems(typeOptions);
        carrierBox.setOnAction(event -> setCarrier(carrierBox.getValue()));
    }

    private void setCarrier(String newCarrier) {
        if (newCarrier != null && !newCarrier.isEmpty()) {
            String updateQuery = "UPDATE orderinfo SET carrier = ? WHERE carrier = 'notselected' AND isdelivered = false";

            try (Connection connection = databaseAdapter.getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                // Set the parameters using setString and setInt
                updateStatement.setString(1, newCarrier);


                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Carrier updated successfully for orders where isDelivered = false");
                } else {
                    System.out.println("No orders were updated. Make sure there are orders with isDelivered=false.");
                }

            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            System.out.println("Invalid input for newCarrier. It cannot be null or empty.");
        }
    }

    @FXML
    private void fireCarrier() {
        String updateQuery = "UPDATE orderinfo SET carrier = ? WHERE isdelivered = false";

        try (Connection connection = databaseAdapter.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameters using setString and setInt
            updateStatement.setString(1, "notselected");

            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Carrier fired");
            } else {
                System.out.println("could not fire!");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    private void displayProducts(ScrollPane scrollPane, VBox vBox, List<Order> orderItems) {

        vBox.getChildren().clear(); // Clear existing content

        for (Order order : orderItems) {

            VBox productBox = createProductBox(order);
            vBox.getChildren().add(productBox);
        }
        scrollPane.setContent(vBox);
    }
    private VBox createProductBox(Order orderItem) {
        VBox productBox = new VBox();
        productBox.setStyle("-fx-border-color: black; -fx-padding: 10;");
        productBox.setSpacing(5);
        productBox.setMaxSize(850,100);


        Text orderID = new Text("Order ID: " + orderItem.getOrderId());
        Text orderPrice = new Text("Order Price: " + orderItem.getTotalcost());
        Text orderTime = new Text("Order Time: " + orderItem.getOrderTime());
        Text deliveryTime = new Text("Delivery Time: " + orderItem.getDeliveryTime());
        Text carrierName = new Text("Carrier Name: " + orderItem.getCarrier());
        Text deliveryStatus = new Text("Delivery Status: " + orderItem.isIsdelivered());


        productBox.getChildren().addAll(orderID,orderPrice,orderTime,deliveryTime,carrierName,deliveryStatus);

        return productBox;
    }



}
