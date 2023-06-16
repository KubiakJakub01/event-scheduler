package com.eventscheduler.controller.event.utils;

import com.eventscheduler.model.dao.EventManager;
import com.eventscheduler.model.dockuments.EventModel;
import com.eventscheduler.observer.Observer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The EventScheduler class is responsible for scheduling and managing events.
 * It implements the Observer interface to receive updates when the event list is updated.
 */
public class EventScheduler implements Observer {
    private static final System.Logger logger = System.getLogger(EventScheduler.class.getName());
    private final EventManager eventManager;
    private ScheduledExecutorService scheduler;
    private EventModel scheduledEvent;
    private Stage eventWindow;

    /**
     * Constructs an EventScheduler object with the given EventManager.
     *
     * @param eventManager The EventManager instance.
     */
    public EventScheduler(EventManager eventManager) {
        this.eventManager = eventManager;
        List<EventModel> tempScheduledEvents = eventManager.getNearestEvents(1);
        if (tempScheduledEvents != null) {
            this.scheduledEvent = tempScheduledEvents.get(0);
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduleEvent(scheduledEvent);
        } else {
            logger.log(System.Logger.Level.INFO, "No events to schedule");
        }
    }

    /**
     * Schedules the given event.
     *
     * @param event The EventModel to be scheduled.
     */
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

    /**
     * Displays the event window for the given event.
     *
     * @param event The EventModel for which to display the event window.
     */
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

    /**
     * Shuts down the scheduler.
     */
    private void shutdownScheduler() {
        // Shutdown the scheduler when no longer needed
        scheduler.shutdown();
        logger.log(System.Logger.Level.INFO, "Scheduler shutdown");
    }

    /**
     * Reloads the scheduler when the event is updated.
     */
    private void reloadScheduler() {
        // Reload the scheduler when the event is updated
        shutdownScheduler();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleEvent(scheduledEvent);
    }

    /**
     * Schedules the next event based on the nearest upcoming event in the event list.
     */
    private void scheduleNextEvent() {
        List<EventModel> tempScheduledEvents = eventManager.getNearestEvents(1);
        if (tempScheduledEvents == null) {
            logger.log(System.Logger.Level.INFO, "No events to schedule");
            return;
        }
        EventModel nextEvent = tempScheduledEvents.get(0);
        this.scheduledEvent = nextEvent;
        scheduleEvent(nextEvent);
    }

    /**
     * Updates the scheduler when the event list is updated.
     */
    @Override
    public void update() {
        // Update the scheduler when the event list is updated
        List<EventModel> tempScheduledEvents = eventManager.getNearestEvents(1);
        if (tempScheduledEvents == null) {
            logger.log(System.Logger.Level.INFO, "No events to schedule");
            return;
        }
        EventModel newEvent = tempScheduledEvents.get(0);
        if (this.scheduledEvent.equals(newEvent)) {
            logger.log(System.Logger.Level.INFO, "Event already scheduled");
            return;
        }
        this.scheduledEvent = newEvent;
        reloadScheduler();
    }
}
