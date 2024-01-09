package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.CartService;
import com.example.shoppingapp.services.ProductService;
import com.example.shoppingapp.services.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CartController {
    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    CartService cartService = new CartService(databaseAdapter);
    ProductService productService = new ProductService(databaseAdapter);
    @FXML
    private VBox cartContainer;

    @FXML
    private ScrollPane allCart;



    private void handleRemoveButton(Button button){
        CartItem cartItem = (CartItem) button.getUserData();
        cartService.removeProductFromCart(cartItem.getProduct().getProductId());
    }

    //===================================================================================

    @FXML
    private void initialize() {
        displayProducts(allCart, cartContainer, cartService.viewCart());
    }

    private void displayProducts(ScrollPane scrollPane, VBox vBox, List<CartItem> cartItems) {


        vBox.getChildren().clear(); // Clear existing content

        for (CartItem cartItem : cartItems) {
            VBox productBox = createProductBox(cartItem);
            vBox.getChildren().add(productBox);
        }

        scrollPane.setContent(vBox);
    }

    private VBox createProductBox(CartItem cartItem) {
        VBox productBox = new VBox();
        productBox.setStyle("-fx-border-color: black; -fx-padding: 10;");
        productBox.setSpacing(5);
        productBox.setMaxSize(850,100);


        Text nameLabel = new Text("Product Name: " + cartItem.getProduct().getName());
        Text priceLabel = new Text("Price: " + cartItem.getProduct().getPrice() + " TL / Kg");
        ImageView imageView = new ImageView(new Image(new File(cartItem.getProduct().getImageLocation()).toURI().toString()));

//        imageView.setFitWidth(50);
//        imageView.setFitHeight(50);

        TextArea quantityTextArea = new TextArea();
        quantityTextArea.setPromptText("Enter quantity");
        quantityTextArea.setMaxSize(50,50);
        quantityTextArea.setPrefWidth(50);

        Button removeButton = new Button("remove");
        removeButton.setId("removeButton");
        removeButton.setUserData(cartItem);

        removeButton.setOnAction(event -> handleRemoveButton(removeButton));
        productBox.getChildren().addAll(nameLabel, priceLabel, imageView, quantityTextArea, removeButton);

        return productBox;
    }
}
