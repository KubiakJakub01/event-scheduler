package com.eventscheduler.controller;

import com.eventscheduler.model.Model;

public interface Observable {

    void addObserver(Observer observer);
    void removeObserver(Observer  observer);
    void notifyObservers();
}
