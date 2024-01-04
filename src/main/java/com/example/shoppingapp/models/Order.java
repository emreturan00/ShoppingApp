package com.example.shoppingapp.models;

// Order.java
public class Order {
    private int orderId;
    private String customer;
    private String status;

    public Order(int orderId, String customer, String status) {
        this.orderId = orderId;
        this.customer = customer;
        this.status = status;
    }



    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
