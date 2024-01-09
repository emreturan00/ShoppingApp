package com.example.shoppingapp.controller;

import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.repository.DatabaseAdapter;
import com.example.shoppingapp.repository.MySqlConnectionAdapter;
import com.example.shoppingapp.services.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class OwnerController {
    DatabaseAdapter databaseAdapter = new MySqlConnectionAdapter();
    ProductService productService = new ProductService(databaseAdapter);

    @FXML
    private Tab ordersTab;
    @FXML
    private Tab reportsTab;

    @FXML
    private ScrollPane ordersScroll;
    @FXML
    private ScrollPane reportsScroll;
    @FXML
    private VBox ordersContainer;



    @FXML
    private TextField name;
    @FXML
    private TextField type;
    @FXML
    private TextField stock;
    @FXML
    private TextField price;
    @FXML
    private TextField imageLocation;
    @FXML
    private TextField threshold;


    @FXML
    private TextField nameDelete;

    @FXML
    private Button add;
    @FXML
    private Button deleteButton;




    @FXML
    private void handleAdd(){
        Product product = new Product();
        product.setProductId(0);
        product.setName(name.getText());
        product.setStock(Integer.parseInt(stock.getText()));
        product.setType(type.getText());
        product.setThreshold(Integer.parseInt(threshold.getText()));
        product.setPrice(Double.parseDouble(price.getText()));
        product.setImageLocation(imageLocation.getText());

        productService.addProduct(product);

    }


    @FXML
    private void handleDelete(){
        System.out.println(nameDelete.getText());

        productService.removeProduct(nameDelete.getText());

    }

    @FXML
    private void handleUpdate(){
//        productService.updateProduct();

    }

    @FXML
    private void handleGotouser(){
//        productService.updateProduct();

    }

}
