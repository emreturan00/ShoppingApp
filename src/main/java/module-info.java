module com.example.shoppingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.shoppingapp to javafx.fxml;
    exports com.example.shoppingapp;
    exports com.example.shoppingapp.controller;
    opens com.example.shoppingapp.controller to javafx.fxml;

}