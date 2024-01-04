package com.example.shoppingapp.models;

// Delivery.java
public class Delivery {
    private int deliveryId;
    private int orderId;
    private String status;

    public Delivery(int deliveryId, int orderId, String status) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.status = status;
    }


    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

