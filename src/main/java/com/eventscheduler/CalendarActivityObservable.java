package com.eventscheduler;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivityObservable implements Observable {
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Model model) {
        for (Observer observer : observers) {
            observer.update(model);
        }
    }
}
