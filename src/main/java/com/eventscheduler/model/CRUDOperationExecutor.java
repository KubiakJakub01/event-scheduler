package com.eventscheduler.model;

import com.eventscheduler.controller.ProgressWindow;

public class CRUDOperationExecutor {
    private EventManager eventManager;

    public CRUDOperationExecutor(EventManager eventManager) {
        this.eventManager = eventManager;
    }

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
        eventManager.addElement(event);
        thread.start();
    }

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
        eventManager.removeElement(event);
        thread.start();
    }

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
        eventManager.updateElement(event, updatedEvent);
        thread.start();
    }
}
