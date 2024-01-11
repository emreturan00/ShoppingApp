package com.example.shoppingapp.models;

import com.example.shoppingapp.models.Product;
import com.example.shoppingapp.services.UserSession;

// CartItem.java
public class CartItem {
    private int userId;
    private Product product;
    private float quantity;

    public CartItem(Product product, float quantity) {
        this.userId = UserSession.getInstance().getUserId();
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(){

    }

    @Override
    public String toString() {
        return "CartItem{" +
                "userId=" + userId +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}



