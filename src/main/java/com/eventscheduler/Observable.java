package com.eventscheduler;

public interface Observable {
    void addObserver(Observer  observer);
    void removeObserver(Observer  observer);
    void notifyObservers(Activity activity);
}
