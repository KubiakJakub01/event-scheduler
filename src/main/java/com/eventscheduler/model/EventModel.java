package com.eventscheduler.model;

import com.eventscheduler.CalendarActivity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventModel {
    private static final System.Logger logger = System.getLogger(EventModel.class.getName());

    MongoCollection<CalendarActivity> events;

    public EventModel(MongoCollection<CalendarActivity> events) {
        this.events = events;
    }

    public void addEvent(CalendarActivity event) {
        events.insertOne(event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " added to database");
    }

    public void deleteEvent(CalendarActivity event) {
        events.deleteOne((Bson) event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " deleted from database");
    }

    public void updateEvent(CalendarActivity event) {
        events.updateOne((Bson) event, (Bson) event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " updated in database");
    }

    public List<CalendarActivity> getAllEvents() {
        logger.log(System.Logger.Level.INFO, "Getting all events from database");
        List<CalendarActivity> eventsList = events.find().into(new ArrayList<>());
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");
        return eventsList;
    }
}
