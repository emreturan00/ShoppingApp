package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.UserService;
import com.example.shoppingapp.services.UserSession;
import com.example.shoppingapp.services.WindowManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.example.shoppingapp.controller.CarrierController;
import com.example.shoppingapp.controller.CartController;
import com.example.shoppingapp.controller.OrderController;
import com.example.shoppingapp.controller.OwnerController;
import com.example.shoppingapp.controller.ShoppingController;
import com.example.shoppingapp.controller.UserServiceController;


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
        UpdateUsernameField.setPromptText(UserSession.getInstance().getUsername());
        UpdateUsernameField.setEditable(false);

    }
    @FXML
    private void handleLogout(){
        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        currentStage.close();
        UserSession.getInstance().clearSession();
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
            Parent signUpRoot = fxmlLoader.load();

            BorderPane signUpContainer = new BorderPane();
            signUpContainer.setCenter(signUpRoot);

            Stage signUpStage = new Stage();
            signUpStage.setScene(new Scene(signUpContainer));
            WindowManager.closeAllStages();
            signUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(){

        String newPassword = UpdatepasswordField.getText();
        String newAdress = UpdateadressField.getText();

        if(userService.isStrongPassword(newPassword) && !newAdress.isEmpty()){
            UpdateweakText.setVisible(false);
            UpdatetakenUsername.setVisible(false);
            userService.updateUserInfo(newPassword, newAdress);

        }

        else if (!userService.isStrongPassword(newPassword)){
            UpdateweakText.setVisible(true);
        }

    }

}
