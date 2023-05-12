package com.eventscheduler;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivityEvent implements Observable {
    private List<Observable> observers = new ArrayList<>();

    @Override
    public void addObserver(Observable observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observable observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Activity activity) {

    }
}
