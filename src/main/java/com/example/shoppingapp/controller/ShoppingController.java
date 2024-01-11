package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.*;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ShoppingController {
    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    CartService cartService = new CartService(databaseAdapter);
    ProductService productService = new ProductService(databaseAdapter);
    @FXML
    private VBox productContainer;

    @FXML
    private VBox productContainerFruits;

    @FXML
    private VBox productContainerVegetables;

    @FXML
    private Tab allTab;

    @FXML
    private Tab fruitsTab;

    @FXML
    private Tab vegetablesTab;

    @FXML
    private ScrollPane allScroll;

    @FXML
    private ScrollPane fruitsScroll;

    @FXML
    private ScrollPane vegetablesScroll;

    @FXML
    private Text gotouser;

    @FXML
    private Button gotocart;


    @FXML
    protected void handleGotouser(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CustomerSettings.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Settings");
            signUpStage.setScene(new Scene(signUpContainer));
            signUpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleGotocart(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShoppingCart.fxml"));
            Parent signUpRoot = fxmlLoader.load();


            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Cart");
            signUpStage.setScene(new Scene(signUpContainer));
            signUpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAddButton(TextArea valueText, Button addButton) {
        try {
            // Get the text from valueText
            String text = valueText.getText();

            // Check if the text is not empty
            if (!text.isEmpty()) {
                // Parse the text to an integer
                float quantity = Float.parseFloat(text);

                // Your existing code to add to the cart
                Product product = (Product) addButton.getUserData();
                CartItem cartItem = new CartItem(product, quantity);
                cartService.addToCart(cartItem);
            } else {
                // Handle the case where the text is empty (show an error message, etc.)
                System.err.println("Quantity cannot be empty.");
            }
        } catch (NumberFormatException e) {
            // Handle the case where the text is not a valid integer
            System.err.println("Invalid quantity format. Please enter a valid number.");
        }
    }


    //===================================================================================

    @FXML
    private void initialize() {
        gotouser.setText("USER: " + UserSession.getInstance().getUsername());
        displayProducts(allScroll, productContainer, productService.viewAvailableProducts());
        displayProducts(fruitsScroll, productContainerFruits, productService.getProductsByType("meyve"));
        displayProducts(vegetablesScroll, productContainerVegetables, productService.getProductsByType("ayicik"));

    }

    private void displayProducts(ScrollPane scrollPane, VBox vBox, List<Product> products) {


        vBox.getChildren().clear(); // Clear existing content

        for (Product product : products) {
            VBox productBox = createProductBox(product);
            vBox.getChildren().add(productBox);
        }

        scrollPane.setContent(vBox);
    }

    private VBox createProductBox(Product product) {
        VBox productBox = new VBox();
        productBox.getStyleClass().add("product-box");

        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.TOP_LEFT);

        ImageView imageView = new ImageView(new Image(new File(product.getImageLocation()).toURI().toString()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        VBox productInfo = new VBox();
        productInfo.getStyleClass().add("product-info");

        Text nameLabel = new Text("Product Name: " + product.getName());
        Text priceLabel = new Text("Price: " + product.getPrice() + " TL / Kg");

        HBox quantityBox = new HBox();
        quantityBox.setAlignment(Pos.CENTER_LEFT);
        quantityBox.getStyleClass().add("quantity-box");

        TextArea quantityTextArea = new TextArea();
        quantityTextArea.setPromptText("Qty");
        quantityTextArea.setMaxSize(40, 20);
        quantityTextArea.setPrefWidth(40);
        quantityTextArea.setPrefWidth(10);

        Button addButton = new Button("Add to Cart");
        addButton.setId("addButton");
        addButton.getStyleClass().add("button-cart");
        addButton.setUserData(product);
        addButton.setOnAction(event -> handleAddButton(quantityTextArea, addButton));

        productInfo.getChildren().addAll(nameLabel, priceLabel);
        quantityBox.getChildren().addAll(quantityTextArea, addButton);
        contentBox.getChildren().addAll(imageView, productInfo, quantityBox);
        productBox.getChildren().add(contentBox);

        return productBox;
    }




}
