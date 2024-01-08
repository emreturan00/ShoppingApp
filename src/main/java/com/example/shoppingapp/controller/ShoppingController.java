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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ShoppingController {
    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    CartService cartService = new CartService(databaseAdapter);
    ProductService productService = new ProductService(databaseAdapter);


    @FXML
    private Text gotouser;
    @FXML
    private TextArea valueText;
    @FXML
    private TextArea valueText2;
    @FXML
    private TextArea valueText3;

    @FXML
    private Button gotocart;

    @FXML
    private Button addButton;

    @FXML
    private Button addButton2;

    @FXML
    private Button addButton3;

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

    @FXML
    protected void handleAdd() {
        try {
            // Get the text from valueText
            String text = valueText.getText();

            // Check if the text is not empty
            if (!text.isEmpty()) {
                // Parse the text to an integer
                int quantity = Integer.parseInt(text);

                // Your existing code to add to the cart
                Product product = productService.viewAvailableProducts().get(0);
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

    @FXML
    protected void handleAdd2() {
        try {
            // Get the text from valueText
            String text = valueText2.getText();

            // Check if the text is not empty
            if (!text.isEmpty()) {
                // Parse the text to an integer
                int quantity = Integer.parseInt(text);

                // Your existing code to add to the cart
                Product product = productService.viewAvailableProducts().get(0);
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

    @FXML
    protected void handleAdd3() {
        try {
            // Get the text from valueText
            String text = valueText3.getText();

            // Check if the text is not empty
            if (!text.isEmpty()) {
                // Parse the text to an integer
                int quantity = Integer.parseInt(text);

                // Your existing code to add to the cart
                Product product = productService.viewAvailableProducts().get(0);
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

}
