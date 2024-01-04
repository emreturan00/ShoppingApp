package com.example.shoppingapp;

import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.UserService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

//        DatabaseAdapter mysqlAdapter = new MySqlConnectionAdapter();
//        UserService userService = new UserService(mysqlAdapter);
//
//        userService.signUp("cemre", "salladim", "owner", "");


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