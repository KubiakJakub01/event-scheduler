package com.eventscheduler.observer;

/**
 * Observable interface for Observer Pattern
 */
public interface Observable {

    /**
     * Adds an observer to the list of observers
     *
     * @param observer Observer to be added
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the list of observers
     *
     * @param observer Observer to be removed
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all observers in the list of observers
     */
    void notifyObservers();
}
