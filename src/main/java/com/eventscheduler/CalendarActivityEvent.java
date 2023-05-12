package com.eventscheduler;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivityEvent implements CalendarActivityListener{
    private List<CalendarActivityListener> observers = new ArrayList<>();

    @Override
    public void addObserver(CalendarActivityListener observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(CalendarActivityListener observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(CalendarActivity activity) {

    }
}
