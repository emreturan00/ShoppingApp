package com.example.shoppingapp.models;

import com.example.shoppingapp.models.Product;

// CartItem.java
public class CartItem {
    private int userId;
    private Product product;
    private int quantity;

    public CartItem(int userId, Product product, int quantity) {
        this.userId = userId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}



