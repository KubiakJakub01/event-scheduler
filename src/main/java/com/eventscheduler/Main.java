package com.eventscheduler;

import com.eventscheduler.controller.calendar.CalendarController;
import com.eventscheduler.controller.event.utils.EventObservable;
import com.eventscheduler.controller.event.utils.EventScheduler;
import com.eventscheduler.model.ConnectionDB;
import com.eventscheduler.model.dao.EventManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main class for the Event Scheduler application.
 * This class extends the JavaFX Application class and serves as the entry point for the application.
 */
public class Main extends Application {

    private CalendarController calendarController;
    private ConnectionDB connectionDB;
    private EventManager eventManager;
    private EventObservable eventObservable;

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method called when the JavaFX application is launched.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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

    /**
     * Initializes the database connection.
     */
    public void initDBConnection() {
        connectionDB = ConnectionDB.getInstance();
        eventManager = new EventManager(connectionDB.getEvents(), eventObservable);
    }

    /**
     * Initializes the dependencies of the application.
     */
    public void initDependencies() {
        eventObservable = new EventObservable();
        initDBConnection();
        EventScheduler eventScheduler = new EventScheduler(eventManager);
        calendarController.initController(eventManager);
        eventObservable.addObserver(eventScheduler);
        eventObservable.addObserver(calendarController);
    }

    /**
     * Closes the database connection.
     */
    public void closeDBConnection() {
        connectionDB.closeConnection();
    }
}
