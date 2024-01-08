package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserServiceController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameFieldDone;

    @FXML
    private TextField passwordFieldDone;

    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button CompleteButton;

    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    UserService userService = new UserService(databaseAdapter);

    @FXML
    protected void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        userService.signIn(username,password);
        Stage currentStage = (Stage) usernameField.getScene().getWindow();

        currentStage.close();


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ShoppingPage.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            // Create a new container (e.g., BorderPane) and set the loaded content as its center
            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Shopping");
            signUpStage.setScene(new Scene(signUpContainer));

            signUpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SignUp.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            // Create a new container (e.g., BorderPane) and set the loaded content as its center
            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setTitle("Signup");
            signUpStage.setScene(new Scene(signUpContainer));
            signUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleComplete() {
        String username = usernameFieldDone.getText();
        String password = passwordFieldDone.getText();

        userService.signUp(username,password,"customer","essek mahallesi");

        Stage currentStage = (Stage) usernameFieldDone.getScene().getWindow();

        // Close the current Stage
        currentStage.close();
    }

}
