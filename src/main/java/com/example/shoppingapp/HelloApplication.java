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

//        Product product = new Product(30,"ejder", "meyve", 3,82,"essek",10);
////
        ProductService productService = new ProductService(databaseAdapter);
////
//        productService.addProduct(product);

        UserService userService = new UserService(databaseAdapter);
        userService.signIn("cemre","salladim");
        CartService cartService = new CartService(databaseAdapter);
        Product product = productService.viewAvailableProducts().get(2);
        CartItem cartItem = new CartItem(product,1);
//        cartService.addToCart(cartItem);




//        cartService.completeCart(2);





//        FXMLLoader fxmlLoader;
//        fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//

//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();

//        UserService userService = new UserService(databaseAdapter);
//
//        userService.signIn("ahmet","salladim");
//
//        CartItem cartItem = new CartItem(productService.viewAvailableProducts().get(0),1);
////
//        cartService.addToCart(cartItem);
//
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
//        cartService.completeCart(formattedDateTime,"emrekargocuccccc");

        OrderService orderService = new OrderService(databaseAdapter);

        System.out.println(orderService.viewOrders().toString());


//

    }

    public static void main(String[] args) {
        launch();


    }
}