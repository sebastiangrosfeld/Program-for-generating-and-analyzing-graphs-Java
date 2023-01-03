package org.grapher.gui;

import javafx.scene.control.Alert;

public class ErrorAlert {
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }
}
