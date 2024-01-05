package com.example.shoppingapp;

import com.example.shoppingapp.models.CartItem;
import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.CartService;
import com.example.shoppingapp.services.ProductService;
import com.example.shoppingapp.services.UserService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.Console;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

//        DatabaseAdapter mysqlAdapter = new MySqlConnectionAdapter();
//        UserService userService = new UserService(mysqlAdapter);
//
//        userService.signUp("cemre", "salladim", "owner", "");
        DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();

//        Product product = new Product("muz", "meyve", 3
//                ,82,"essek",10);
//
        ProductService productService = new ProductService(databaseAdapter);
        CartService cartService = new CartService(databaseAdapter);
        Product product = productService.viewAvailableProducts().get(0);
        CartItem cartItem = new CartItem(2,product,1);
        cartService.addToCart(cartItem);
//        cartService.viewCart(1);
        cartService.updateCartItemQuantity(1,0,5);
//        cartService.completeCart(0);






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