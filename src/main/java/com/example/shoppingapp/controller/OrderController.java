package com.example.shoppingapp.controller;


import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.CartService;
import com.example.shoppingapp.services.OrderService;
import com.example.shoppingapp.services.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class OrderController {

    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    OrderService orderService = new OrderService(databaseAdapter);
    @FXML
    private VBox orderContainer;
    @FXML
    private ScrollPane allOrder;

    private void handleRemoveButton(Button button){



    }
    @FXML
    private void initialize() {
        displayProducts(allOrder, orderContainer, orderService.viewOrdersbyid());
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

        Button removeButton = new Button("remove");
        removeButton.setId("removeButton");
        removeButton.setUserData(orderItem);

        removeButton.setOnAction(event -> handleRemoveButton(removeButton));
        productBox.getChildren().addAll(orderID,orderPrice,orderTime,deliveryTime,carrierName,deliveryStatus);

        return productBox;
    }


}
