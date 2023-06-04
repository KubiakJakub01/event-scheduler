package com.eventscheduler.controller.event.utils;

import com.eventscheduler.observer.Observable;
import com.eventscheduler.observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * EventObservable class for Observer Pattern
 */
public class EventObservable implements Observable {
    private static final System.Logger logger = System.getLogger(EventObservable.class.getName());
    private final List<Observer> observers;

    /**
     * Constructor for EventObservable
     */
    public EventObservable() {
        observers = new ArrayList<>();
    }

    /**
     * Adds an observer to the list of observers
     *
     * @param observer Observer to be added
     */
    @Override
    public void addObserver(Observer observer) {
        logger.log(System.Logger.Level.INFO, "Adding observer " + observer.getClass().getName());
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers
     *
     * @param observer Observer to be removed
     */
    @Override
    public void removeObserver(Observer observer) {
        logger.log(System.Logger.Level.INFO, "Removing observer " + observer.getClass().getName());
        observers.remove(observer);
    }

    /**
     * Notifies all observers in the list of observers
     */
    @Override
    public void notifyObservers() {
        logger.log(System.Logger.Level.INFO, "Notifying " + observers.size() + " observers");
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
