package com.example.shoppingapp.models;

// Product.java
public class Product {
    private String Name;
    private String type;
    private int stock;
    private double price;
    private String imageLocation;
    private int threshold;

    public Product(String name, String type, int stock, double price,
                   String imageLocation, int threshold) {
        Name = name;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.imageLocation = imageLocation;
        this.threshold = threshold;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getProductName() {
        return Name;
    }

    public void setProductName(String productName) {
        this.Name = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

