package com.eventscheduler;

public interface Observable {
    void addObserver(Observable observer);
    void removeObserver(Observable observer);
    void notifyObservers();
}
