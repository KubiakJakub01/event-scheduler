package com.eventscheduler.model;

import com.eventscheduler.CalendarActivity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class EventModel {

    private MongoDatabase database;
    private MongoCollection<CalendarActivity> events_collection;

    public EventModel(MongoDatabase database) {
        this.database = database;
        this.events_collection = database.getCollection("events", CalendarActivity.class);
    }

    public void addEvent(CalendarActivity event) {
        events_collection.insertOne(event);
    }
}
