package com.eventscheduler.model;

import com.eventscheduler.controller.ProgressWindow;

/**
 * Class for executing CRUD operations in a separate thread with a progress window
 */
public class CRUDOperationExecutor {
    private EventManager eventManager;

    /**
     * Constructs a CRUDOperationExecutor object with the given event manager
     *
     * @param eventManager the event manager
     */
    public CRUDOperationExecutor(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Adds an event to the event manager with a progress window
     *
     * @param event the event to be added
     */
    public void addWithProgress(EventModel event){
        ProgressWindow progressWindow = new ProgressWindow("Adding event...");
        progressWindow.startProgressWindow();
        Thread thread = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                double progress = i / 100.0;
                progressWindow.updateProgress(progress);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            progressWindow.closeWindow();
        });
        thread.start();
        eventManager.addElement(event);
    }

    /**
     * Deletes an event from the event manager with a progress window
     *
     * @param event the event to be deleted
     */
    public void deleteWithProgress(EventModel event){
        ProgressWindow progressWindow = new ProgressWindow("Deleting event...");
        progressWindow.startProgressWindow();
        Thread thread = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                double progress = i / 100.0;
                progressWindow.updateProgress(progress);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            progressWindow.closeWindow();
        });
        thread.start();
        eventManager.removeElement(event);
    }

    /**
     * Updates an event in the event manager with a progress window
     *
     * @param event the event to be updated
     * @param updatedEvent the updated event
     */
    public void updateWithProgress(EventModel event, EventModel updatedEvent){
        ProgressWindow progressWindow = new ProgressWindow("Updating event...");
        progressWindow.startProgressWindow();
        Thread thread = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                double progress = i / 100.0;
                progressWindow.updateProgress(progress);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            progressWindow.closeWindow();
        });
        thread.start();
        eventManager.updateElement(event, updatedEvent);
    }
}
