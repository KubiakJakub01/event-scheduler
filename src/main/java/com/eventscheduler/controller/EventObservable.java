package com.eventscheduler.controller;

import com.eventscheduler.model.EventManager;
import com.eventscheduler.model.EventModel;
import com.eventscheduler.model.Model;

import java.util.ArrayList;
import java.util.List;

public class EventObservable implements Observable {
    private static final System.Logger logger = System.getLogger(EventObservable.class.getName());
    private List<Observer> observers = new ArrayList<>();
    private EventManager eventManager;

    public EventObservable(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addEvent(Model model) {
        eventManager.addElement((EventModel) model);
        logger.log(System.Logger.Level.INFO, "New model added to calendar");
        notifyObservers();
    }

    public void removeEvent(Model model) {
        eventManager.removeElement((EventModel) model);
        logger.log(System.Logger.Level.INFO, "Model removed from calendar");
        notifyObservers();
    }

    public void updateEvent(Model model, Model updatedModel) {
        eventManager.updateElement((EventModel) model, (EventModel) updatedModel);
        logger.log(System.Logger.Level.INFO, "Model updated in calendar");
        notifyObservers();
    }
}
