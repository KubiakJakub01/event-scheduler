package com.eventscheduler;

import com.eventscheduler.controller.CalendarController;
import com.eventscheduler.controller.EventObservable;
import com.eventscheduler.controller.EventScheduler;
import com.eventscheduler.model.ConnectionDB;
import com.eventscheduler.model.EventManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private CalendarController calendarController;
    private ConnectionDB connectionDB;
    private EventManager eventManager;
    private EventObservable eventObservable;
    private EventScheduler eventScheduler;

    @Override
    public void start(Stage stage) throws IOException {
        initDBConnection();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Calendar.fxml"));
        Parent root = fxmlLoader.load();
        calendarController = fxmlLoader.getController();
        initDependencies();
        Scene scene = new Scene(root, 1024, 768);
        stage.setTitle("Event scheduler");
        stage.setScene(scene);
        // Set on close request to close all windows
        stage.setOnCloseRequest(windowEvent -> {
            closeDBConnection();
            System.exit(0);
        });
        stage.setResizable(false);
        stage.show();
    }

    public void initDBConnection() {
        connectionDB = new ConnectionDB();
        eventManager = new EventManager(connectionDB.getEvents(), eventObservable);
    }

    public void initDependencies() {
        eventObservable = new EventObservable();
        initDBConnection();
        eventScheduler = new EventScheduler(eventManager);
        calendarController.initController(eventManager);
        eventObservable.addObserver(eventScheduler);
        eventObservable.addObserver(calendarController);
    }

    public void closeDBConnection() {
        connectionDB.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}
