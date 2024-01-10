package com.example.shoppingapp.controller;

import com.example.shoppingapp.HelloApplication;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.UserService;
import com.example.shoppingapp.services.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class UserServiceController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField adressFieldDone;
    @FXML
    private TextField usernameFieldDone;

    @FXML
    private TextField passwordFieldDone;
    @FXML
    private Text weakText;

    @FXML
    private Text takenUsername;



    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button CompleteButton;

    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();




    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    UserService userService = new UserService(databaseAdapter);

    @FXML
    protected void initialize(){
        ObservableList<String> typeOptions = FXCollections.observableArrayList("owner", "customer", "carrier");
        choiceBox.setItems(typeOptions);


    }

    @FXML
    protected void handleLogin(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        userService.signIn(username,password);
        System.out.println(UserSession.getInstance().getUsername());
        Stage currentStage = (Stage) usernameField.getScene().getWindow();

        currentStage.close();

        String whatScene;

        if ("carrier".equals(UserSession.getInstance().getRole())){
            whatScene = "Carrier.fxml";
            }
        else if ("customer".equals(UserSession.getInstance().getRole())) {
            whatScene = "ShoppingPage.fxml";
        }
        else {
            whatScene = "Owner.fxml";
        }




        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(whatScene));
            Parent signUpRoot = fxmlLoader.load();

            // Create a new container (e.g., BorderPane) and set the loaded content as its center
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
        takenUsername.setVisible(false);
        weakText.setVisible(false);
        String username = usernameFieldDone.getText();
        String password = passwordFieldDone.getText();
        String adress = adressFieldDone.getText();

        if(userService.isStrongPassword(password) && !userService.isUsernameTaken(username)){
            weakText.setVisible(false);
            takenUsername.setVisible(false);
            userService.signUp(username,password, choiceBox.getValue(),adress);
            Stage currentStage = (Stage) usernameFieldDone.getScene().getWindow();
            currentStage.close();
        }
         else if (userService.isUsernameTaken(username)) {
            takenUsername.setVisible(true);
            }
        else if (!userService.isStrongPassword(password)){
            weakText.setVisible(true);
        }
    }



}
