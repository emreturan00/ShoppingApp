package com.example.shoppingapp.models;

// Product.java
public class Product {

    // Assuming this is the auto-incrementing primary key
    private int productId;
    private String Name;
    private String type;
    private float stock;
    private double price;
    private String imageLocation;
    private int threshold;


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", Name='" + Name + '\'' +
                ", type='" + type + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", imageLocation='" + imageLocation + '\'' +
                ", threshold=" + threshold +
                '}';
    }

    public Product() {
    }

    public Product(int productId, String name, String type, float stock, double price, String imageLocation, int threshold) {
        this.productId = productId;
        this.Name = name;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.imageLocation = imageLocation;
        this.threshold = threshold;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
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


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

