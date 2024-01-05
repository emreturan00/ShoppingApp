package com.example.shoppingapp.models;

import java.util.List;

// Order.java
public class Order {
    private int orderId;
    private int userID;
    private String orderTime;
    private String deliveryTime;
    private List<Product> products;
    private String user;
    private String carrier;
    private boolean isdelivered;
    private double totalcost;

    public Order(int orderId, String orderTime, String deliveryTime,
                 List<Product> products, String user,
                 String carrier, boolean isdelivered, double totalcost) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.products = products;
        this.user = user;
        this.carrier = carrier;
        this.isdelivered = isdelivered;
        this.totalcost = totalcost;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public boolean isIsdelivered() {
        return isdelivered;
    }

    public void setIsdelivered(boolean isdelivered) {
        this.isdelivered = isdelivered;
    }

    public double getTotalcost() {
        return totalcost;
    }

    public void setTotalcost(double totalcost) {
        this.totalcost = totalcost;
    }
}
