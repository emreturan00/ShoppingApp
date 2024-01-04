package com.example.shoppingapp.controller;
import com.example.shoppingapp.models.User;
import com.example.shoppingapp.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;





public class loginController {
    private UserService authService;  // Injected dependency

    @FXML
    private TextField loginUsernameField;

    @FXML
    private TextField loginPasswordField;

    @FXML
    private TextField signupUsernameField;

    @FXML
    private TextField signupPasswordField;

    @FXML
    private TextField signupRoleField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;


    public loginController(UserService authService) {
        this.authService = authService;
    }


    @FXML
    private void handleLogin(ActionEvent event) {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        // Call login method from AuthService
        boolean loginSuccessful = authService.signIn(username, password);

        if (loginSuccessful) {
            // Redirect to the next screen or perform other actions
        } else {
            // Display an error message to the user
        }

    }

//    @FXML
//    private void handleSignup(ActionEvent event) {
//        String username = signupUsernameField.getText();
//        String password = signupPasswordField.getText();
//        String role = signupRoleField.getText();
//
//        // Call signup method from AuthService
//        boolean signupSuccessful = authService.signUp(username, password, role,adress);
//
//        if (signupSuccessful) {
//            // Redirect to the next screen or perform other actions
//        } else {
//            // Display an error message to the user
//        }
//    }
    }

