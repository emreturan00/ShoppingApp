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
import javafx.scene.Node;
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
    @FXML
    private Button purchase;
    @FXML
    private Button continueShopping;
    @FXML
    private TextArea summary;
    @FXML
    private TextArea selectDate;
    @FXML
    private TextArea totalArea;
    @FXML
    private Text total;






    private void handleRemoveButton(Button button){
        CartItem cartItem = (CartItem) button.getUserData();
        cartService.removeProductFromCart(cartItem.getProduct().getProductId());
        Stage currentStage = (Stage) button.getScene().getWindow();
        currentStage.close();

        ShoppingController shoppingController = new ShoppingController();
        shoppingController.handleGotocart();


    }

    @FXML
    private void handleContinue(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();
    }
    @FXML
    private void handleOrders(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Orders.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Orders");
            signUpStage.setScene(new Scene(signUpContainer));
            signUpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePurchase(ActionEvent event){
        System.out.println(selectDate.getText());
        cartService.completeCart(selectDate.getText(),"notselected");
    }

    //===================================================================================

    @FXML
    private void initialize() {
        displayProducts(allCart, cartContainer, cartService.viewCart());
    }

    private void displayProducts(ScrollPane scrollPane, VBox vBox, List<CartItem> cartItems) {

        Double totalPrice = 0.0;
        vBox.getChildren().clear(); // Clear existing content
        summary.setEditable(false);
        summary.setWrapText(true);
        totalArea.setEditable(false);
        totalArea.setWrapText(true);

        for (CartItem cartItem : cartItems) {
            summary.appendText("\n" + cartItem.getProduct().getName() + "---- x" + String.format("%.2f", cartItem.getQuantity()) + "---- " + String.format("%.2f",cartItem.getQuantity() * cartItem.getProduct().getPrice()));
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            VBox productBox = createProductBox(cartItem);
            vBox.getChildren().add(productBox);
        }
        double tax = totalPrice/100;
        summary.appendText("\n" + "VALUE ADDING TAX 1%: " + String.format("%.2f",tax));
        totalPrice += tax;
        totalArea.setText(String.format("%.2f",totalPrice) + "TL");


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

        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        TextArea quantityTextArea = new TextArea();
        quantityTextArea.setPromptText("Enter quantity");
        quantityTextArea.setMaxSize(50,50);
        quantityTextArea.setPrefWidth(50);

        Button removeButton = new Button("remove");
        removeButton.setId("removeButton");
        removeButton.setUserData(cartItem);

        Button addButton = new Button("Add to Cart");
        addButton.setId("addButton");
        addButton.setUserData(cartItem.getProduct());

        addButton.setOnAction(event -> handleAddButton(quantityTextArea, addButton));

        removeButton.setOnAction(event -> handleRemoveButton(removeButton));
        productBox.getChildren().addAll(nameLabel, priceLabel, imageView, quantityTextArea, addButton, removeButton);

        return productBox;
    }


    private void handleAddButton(TextArea valueText, Button addButton) {
        try {
            Product product = (Product) addButton.getUserData();
            cartService.removeProductFromCart(product.getProductId());

            Scene scene = valueText.getScene();

            // Get the stage from the scene
            Stage stage = (Stage) scene.getWindow();

            // Close the stage
            stage.close();



            // Get the text from valueText
            String text = valueText.getText();

            // Check if the text is not empty
            if (!text.isEmpty()) {
                // Parse the text to an integer
                float quantity = Float.parseFloat(text);

                // Your existing code to add to the cart
                CartItem cartItem = new CartItem(product, quantity);
                cartService.addToCart(cartItem);
            } else {
                // Handle the case where the text is empty (show an error message, etc.)
                System.err.println("Quantity cannot be empty.");
            }

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShoppingCart.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setScene(new Scene(signUpContainer));
            signUpStage.show();

        } catch (NumberFormatException e) {
            // Handle the case where the text is not a valid integer
            System.err.println("Invalid quantity format. Please enter a valid number.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
