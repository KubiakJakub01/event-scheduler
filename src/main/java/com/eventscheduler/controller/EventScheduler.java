package com.eventscheduler.controller;

import com.eventscheduler.model.EventManager;
import com.eventscheduler.model.EventModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventScheduler implements Observer {
    private static final System.Logger logger = System.getLogger(EventScheduler.class.getName());

    private ScheduledExecutorService scheduler;
    private final EventManager eventManager;
    private EventModel scheduledEvent;
    private Stage eventWindow;

    public EventScheduler(EventManager eventManager) {
        this.eventManager = eventManager;
        this.scheduledEvent = eventManager.getNearestEvents(1).get(0);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleEvent(scheduledEvent);
    }

    public void scheduleEvent(EventModel event) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime eventDateTime = event.getDate();
        Duration duration = Duration.between(currentDateTime, eventDateTime);

        long initialDelay = Math.max(duration.getSeconds(), 0);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " scheduled in " + initialDelay + " seconds");

        scheduler.schedule(() -> {
            // Action to be performed when the event starts
            Platform.runLater(() -> showEventWindow(event));
            logger.log(System.Logger.Level.INFO, "Event: " + event.getTitle() + " started at " + LocalDateTime.now());
        }, initialDelay, TimeUnit.SECONDS);
    }

    private void showEventWindow(EventModel event) {
        eventWindow = new Stage();
        eventWindow.setTitle("Event Started");

        Label titleLabel = new Label("Event: " + event.getTitle());
        Label dateLabel = new Label("Date: " + event.getDate().toString());
        Label durationLabel = new Label("Duration: " + event.getDuration().toString());
        Label placeLabel = new Label("Place: " + event.getPlace());
        Label descriptionLabel = new Label("Description: " + event.getDescription());
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            eventWindow.close();
            scheduleNextEvent();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(titleLabel, dateLabel, durationLabel, placeLabel, descriptionLabel, closeButton);

        Scene scene = new Scene(layout, 300, 300);
        eventWindow.setOnCloseRequest(e -> {
            eventWindow.close();
            scheduleNextEvent();
        });
        eventWindow.setScene(scene);
        eventWindow.show();
    }

    private void shutdownScheduler() {
        // Shutdown the scheduler when no longer needed
        scheduler.shutdown();
        logger.log(System.Logger.Level.INFO, "Scheduler shutdown");
    }

    private void reloadScheduler() {
        // Reload the scheduler when the event is updated
        shutdownScheduler();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleEvent(scheduledEvent);
    }

    private void scheduleNextEvent() {
        EventModel nextEvent = eventManager.getNearestEvents(1).get(0);
        this.scheduledEvent = nextEvent;
        scheduleEvent(nextEvent);
    }

    @Override
    public void update() {
        // Update the scheduler when the event list is updated
        EventModel newEvent = eventManager.getNearestEvents(1).get(0);
        if (this.scheduledEvent.equals(newEvent)) {
            logger.log(System.Logger.Level.INFO, "Event already scheduled");
            return;
        }
        this.scheduledEvent = newEvent;
        reloadScheduler();
    }
}
