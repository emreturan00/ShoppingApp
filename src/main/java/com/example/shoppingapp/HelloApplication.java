package com.example.shoppingapp;

import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Delivery;
import com.example.shoppingapp.models.Order;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.CartService;
import com.example.shoppingapp.services.DeliveryService;
import com.example.shoppingapp.services.ProductService;
import com.example.shoppingapp.services.UserService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;
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
    }

    public static void main(String[] args) {
        launch();


    }
}