package com.example.shoppingapp;

import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Delivery;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();

//        Product product = new Product("muz", "meyve", 3
//                ,82,"essek",10);
//
        ProductService productService = new ProductService(databaseAdapter);
//
        CartService cartService = new CartService(databaseAdapter);
//        Product product = productService.viewAvailableProducts().get(0);
//        CartItem cartItem = new CartItem(2,product,1);
//        cartService.addToCart(cartItem);

//        List<CartItem> cartItems = cartService.viewCart(2);
//        if (cartItems != null && !cartItems.isEmpty()) {
//            System.out.println(cartItems.toString());
//        } else {
//            System.out.println("Cart is empty or null.");
//        }



//        cartService.completeCart(2);





//        FXMLLoader fxmlLoader;
//        fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//

//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();

        UserService userService = new UserService(databaseAdapter);

        userService.signIn("ahmet","salladim");

//        CartItem cartItem = new CartItem(productService.viewAvailableProducts().get(0),1);
//
//        cartService.addToCart(cartItem);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDateTime = LocalDateTime.now().format(formatter);

//

    }

    public static void main(String[] args) {
        launch();


    }
}