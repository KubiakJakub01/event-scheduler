package com.eventscheduler.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The ProgressWindow class is responsible for displaying the progress of a task.
 */
public class ProgressWindow {
    private ProgressBar progressBar;
    private Stage stage;
    private Label label;

    /**
     * Constructs a ProgressWindow object with the given title.
     *
     * @param title The title of the progress window.
     */
    public ProgressWindow(String title) {
        label = new Label(title);
    }

    /**
     * Starts the progress window.
     */
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

    /**
     * Updates the progress of the task.
     *
     * @param progress The progress of the task.
     */
    public void updateProgress(double progress) {
        if (progressBar != null) {
            Platform.runLater(() -> progressBar.setProgress(progress));
        }
    }

    /**
     * Closes the progress window.
     */
    public void closeWindow() {
        if (stage != null) {
            Platform.runLater(() -> stage.close());
        }
    }
}
