package com.eventscheduler.model;

import com.eventscheduler.CalendarActivity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;

import java.util.List;

public class EventModel {
    private static final System.Logger logger = System.getLogger(EventModel.class.getName());

    private MongoDatabase database;
    private MongoCollection<CalendarActivity> events_collection;

    public EventModel(MongoDatabase database) {
        this.database = database;
        this.events_collection = database.getCollection("events", CalendarActivity.class);
    }

    public void addEvent(CalendarActivity event) {
        events_collection.insertOne(event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " added to database");
    }

    public void deleteEvent(CalendarActivity event) {
        events_collection.deleteOne((Bson) event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " deleted from database");
    }

    public void updateEvent(CalendarActivity event) {
        events_collection.updateOne((Bson) event, (Bson) event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " updated in database");
    }

    public CalendarActivity getEvent(CalendarActivity event) {
        return (CalendarActivity) events_collection.find((Bson) event);
    }

    public List<CalendarActivity> getAllEvents() {
        logger.log(System.Logger.Level.INFO, "Getting all events from database");
        return (List<CalendarActivity>) events_collection.find();
    }
}
