package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.DeliveryService;
import com.example.shoppingapp.services.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CarrierController  {
    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    DeliveryService deliveryService = new DeliveryService(databaseAdapter);

    @FXML
    private VBox availableContainer;
    @FXML
    private VBox currentContainer;
    @FXML
    private VBox completedContainer;

    @FXML
    private ScrollPane availableScroll;
    @FXML
    private ScrollPane currentScroll;
    @FXML
    private ScrollPane completedScroll;

    @FXML
    private Tab availableTab;
    @FXML
    private Tab currentTab;
    @FXML
    private Tab completedTab;

    @FXML
    private Text gotouser;










    @FXML
    private void handleSelect(Button selectButton){
        Order order = (Order) selectButton.getUserData();
        deliveryService.updateOrderSelection(order.getOrderId());
    }

    @FXML
    private void handleDelivered(Button selectButton){
        Order order = (Order) selectButton.getUserData();
        deliveryService.updateOrderDelivery(order.getOrderId());
    }



    @FXML
    private void initialize() {
        gotouser.setText("USER: " + UserSession.getInstance().getUsername());
        displayDeliveries(availableScroll,availableContainer,deliveryService.viewAvailabledeliveries(UserSession.getInstance().getUsername()));
        displayDeliveries(currentScroll,currentContainer,deliveryService.viewSelecteddeliveries(UserSession.getInstance().getUsername()));
        displayDeliveries(completedScroll,completedContainer,deliveryService.viewCompleteddeliveries(UserSession.getInstance().getUsername()));
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



    private void displayDeliveries(ScrollPane scrollPane, VBox vBox, List<Order> orderItems) {

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
        productBox.setMaxSize(850,400);


        Text orderID = new Text("Order ID: " + orderItem.getOrderId());
        Text orderPrice = new Text("Order Price: " + orderItem.getTotalcost());
        Text orderTime = new Text("Order Time: " + orderItem.getOrderTime());
        Text deliveryTime = new Text("Delivery Time: " + orderItem.getDeliveryTime());
        Text carrierName = new Text("Carrier Name: " + orderItem.getCarrier());
        Text deliveryStatus = new Text("Delivery Status: " + orderItem.isIsdelivered());


        if(orderItem.getisSelected() == false && orderItem.isIsdelivered() == false){
            Button selectButton = new Button("Select");
            selectButton.setId("selectButton");
            selectButton.setUserData(orderItem);
            selectButton.setOnAction(event -> handleSelect(selectButton));
            productBox.getChildren().addAll(orderID,orderPrice,orderTime,deliveryTime,carrierName,deliveryStatus,selectButton);
        }
        else if(orderItem.getisSelected() == true && orderItem.isIsdelivered() == false){
            Button deliveredButton = new Button("Money Taken");
            deliveredButton.setId("deliveredButton");
            deliveredButton.setUserData(orderItem);
            deliveredButton.setOnAction(event -> handleDelivered(deliveredButton));
            productBox.getChildren().addAll(orderID,orderPrice,orderTime,deliveryTime,carrierName,deliveryStatus,deliveredButton);
        }
        else
        {
            productBox.getChildren().addAll(orderID,orderPrice,orderTime,deliveryTime,carrierName,deliveryStatus);
        }


        return productBox;
    }
}
