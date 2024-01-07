package com.example.shoppingapp.models;

import java.util.List;

// Order.java
public class Order {
    private int orderId;
    private int userID;
    private String orderTime;
    private String deliveryTime;
    private String products;
    private String carrier;
    private boolean isdelivered;
    private double totalcost;


    public Order(int orderId, int userID, String orderTime, String deliveryTime, String products, String carrier, boolean isdelivered, double totalcost) {
        this.orderId = orderId;
        this.userID = userID;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.products = products;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userID=" + userID +
                ", orderTime='" + orderTime + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", products='" + products + '\'' +
                ", carrier='" + carrier + '\'' +
                ", isdelivered=" + isdelivered +
                ", totalcost=" + totalcost +
                '}';
    }
}