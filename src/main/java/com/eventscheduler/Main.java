package com.eventscheduler;

import com.eventscheduler.model.ConnectionDB;
import com.eventscheduler.model.EventModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    private CalendarController calendarController;
    private ConnectionDB connectionDB;
    private EventModel eventModel;

    @Override
    public void start(Stage stage) throws IOException {
        initDBConnection();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Calendar.fxml"));
        Parent root = fxmlLoader.load();
        calendarController = fxmlLoader.getController();
        calendarController.loadModel(eventModel);
        Scene scene = new Scene(root, 1024, 768);
        stage.setTitle("Event scheduler");
        stage.setScene(scene);
        // Set on close request to close all windows
        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });
        stage.setResizable(false);
        stage.show();
    }

    public void initDBConnection() {
        connectionDB = new ConnectionDB();
        eventModel = new EventModel(connectionDB.getEvents());
    }

    public static void main(String[] args) {
        launch();
    }
}
