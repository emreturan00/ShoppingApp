package com.example.shoppingapp.services;

import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class WindowManager {

    // List to store references to open stages
    private static final List<Stage> openStages = new ArrayList<>();

    public static List<Stage> getOpenStages() {
        return openStages;
    }

    public static void addOpenStage(Stage stage) {
        openStages.add(stage);
    }

    public static void removeOpenStage(Stage stage) {
        openStages.remove(stage);
    }

    public static void closeAllStages() {
        for (Stage stage : openStages) {
            stage.close();
        }
        openStages.clear();
    }


}

