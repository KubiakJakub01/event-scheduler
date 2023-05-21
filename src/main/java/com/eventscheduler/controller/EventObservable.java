package com.eventscheduler.controller;

import com.eventscheduler.model.EventManager;
import com.eventscheduler.model.EventModel;
import com.eventscheduler.model.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventObservable implements Observable {
    private static final System.Logger logger = System.getLogger(EventObservable.class.getName());
    private List<Observer> observers;

    public EventObservable() {
        observers = new ArrayList<>();
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
}
