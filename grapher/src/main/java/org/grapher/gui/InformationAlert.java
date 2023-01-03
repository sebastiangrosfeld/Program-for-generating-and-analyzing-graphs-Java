package org.grapher.gui;

import javafx.scene.control.Alert;

public class InformationAlert {
    public static void showNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }
}
