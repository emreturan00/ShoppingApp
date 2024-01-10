package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.UserService;
import com.example.shoppingapp.services.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class UserSettingsController {

    @FXML
    private TextField UpdateUsernameField;

    @FXML
    private TextField UpdatepasswordField;

    @FXML
    private TextField UpdateadressField;

    @FXML
    private Text UpdateweakText;

    @FXML
    private Text UpdatetakenUsername;
    @FXML
    private Button logoutButton;
    @FXML
    private Text updateCustomernameText;

    @FXML
    private Text roleText;


    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    UserService userService = new UserService(databaseAdapter);


    @FXML
    protected void initialize(){
        updateCustomernameText.setText("USER: " + UserSession.getInstance().getUsername());
        roleText.setText("ROLE: " + UserSession.getInstance().getRole());

    }
    @FXML
    private void handleLogout(){

        UserSession.getInstance().clearSession();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setScene(new Scene(signUpContainer));
            signUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(){
        String newUsername = UpdateUsernameField.getText();
        String newPassword = UpdatepasswordField.getText();
        String newAdress = UpdateadressField.getText();

        if(userService.isStrongPassword(newPassword) && !userService.isUsernameTaken(newUsername) && !newAdress.isEmpty()){
            UpdateweakText.setVisible(false);
            UpdatetakenUsername.setVisible(false);
            userService.updateUserInfo(newUsername, newPassword, newAdress);

        }
        else if (userService.isUsernameTaken(newUsername)) {
            UpdatetakenUsername.setVisible(true);
        }
        else if (!userService.isStrongPassword(newPassword)){
            UpdateweakText.setVisible(true);
        }

    }

}
