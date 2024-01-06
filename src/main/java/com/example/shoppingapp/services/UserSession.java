package com.example.shoppingapp.services;

public class UserSession {
    private static UserSession instance;
    private int userId;

    private UserSession() {
        // private constructor to enforce singleton pattern
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void clearSession() {
        userId = 0;
        // clear other session data if needed
    }
}
