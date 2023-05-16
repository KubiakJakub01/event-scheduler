package com.eventscheduler.model;

import com.eventscheduler.CalendarActivity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;

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

    public void deleteEvent(CalendarActivity event) {
        events_collection.deleteOne((Bson) event);
    }

    public void updateEvent(CalendarActivity event) {
        events_collection.updateOne((Bson) event, (Bson) event);
    }

    public void getEvent(CalendarActivity event) {
        events_collection.find((Bson) event);
    }

    public void getAllEvents() {
        events_collection.find();
    }
}
