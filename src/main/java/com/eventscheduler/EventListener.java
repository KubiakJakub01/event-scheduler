package com.eventscheduler;

public interface EventListener {
    void addEventObserver(EventListener observer);
    void removeEventObserver(EventListener observer);
    void notifyEventObservers();
}
