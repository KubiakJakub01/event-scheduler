package com.eventscheduler.controller;

import com.eventscheduler.model.EventModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventScheduler {
    private static final System.Logger logger = System.getLogger(EventScheduler.class.getName());

    private ScheduledExecutorService scheduler;
    private EventModel scheduledEvent;

    public EventScheduler(EventModel scheduledEvent) {
        this.scheduledEvent = scheduledEvent;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleEvent(scheduledEvent);
    }

    public void updateScheduledEvent(EventModel scheduledEvent) {
        if (this.scheduledEvent.equals(scheduledEvent)) {
            logger.log(System.Logger.Level.INFO, "Event already scheduled");
            return;
        }
        this.scheduledEvent = scheduledEvent;
        reloadScheduler();
    }

    public void scheduleEvent(EventModel event) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime eventDateTime = event.getDate();
        Duration duration = Duration.between(currentDateTime, eventDateTime);

        long initialDelay = Math.max(duration.getSeconds(), 0);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " scheduled in " + initialDelay + " seconds");

        scheduler.schedule(() -> {
            // Action to be performed when the event starts
            logger.log(System.Logger.Level.INFO, "Event scheduled: " + event.getTitle());
        }, initialDelay, TimeUnit.SECONDS);
    }

    public void shutdownScheduler() {
        // Shutdown the scheduler when no longer needed
        scheduler.shutdown();
        logger.log(System.Logger.Level.INFO, "Scheduler shutdown");
    }

    private void reloadScheduler(){
        // Reload the scheduler when the event is updated
        shutdownScheduler();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleEvent(scheduledEvent);
    }
}
