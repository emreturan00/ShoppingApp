package com.example.shoppingapp.services;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private String username;
    private String role;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserData(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }

    public void clearSession() {
        userId = 0;
        username = null;
        role = null;
    }
}
