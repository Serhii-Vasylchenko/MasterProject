package com.serhiivasylchenko.utils;

import javafx.scene.control.Alert;

/**
 * @author Serhii Vasylchenko
 */
public class Utils {
    public static void notImplemented() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Soon!");
        alert.setHeaderText(null);
        alert.setContentText("Not implemented yet. Stay tuned!");

        alert.showAndWait();
    }
}
