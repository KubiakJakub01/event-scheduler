package com.eventscheduler.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgressWindow {
    private ProgressBar progressBar;
    private Stage stage;
    private Label label;

    public ProgressWindow(String title) {
        label = new Label(title);
    }

    public void startProgressWindow() {
        stage = new Stage();
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);

        VBox root = new VBox(label, progressBar);
        root.setPadding(new javafx.geometry.Insets(10));
        root.setSpacing(10);
        Scene scene = new Scene(root, 250, 100);

        stage.setScene(scene);
        stage.setTitle("Progress");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            closeWindow();
        });

        stage.show();
    }

    public void updateProgress(double progress) {
        if (progressBar != null) {
            Platform.runLater(() -> progressBar.setProgress(progress));
        }
    }

    public void closeWindow() {
        if (stage != null) {
            Platform.runLater(() -> stage.close());
        }
    }
}
