package com.eventscheduler;

public interface Obeservable {
    void addObserver(Obeservable observer);
    void removeObserver(Obeservable observer);
    void notifyObservers(CalendarActivity activity);
}
