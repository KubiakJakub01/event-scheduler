package com.eventscheduler.model;

import com.eventscheduler.CalendarActivity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

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

    public List<CalendarActivity> getEventsByMonth(int year, int month) {
        // Create the start and end date of the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        logger.log(System.Logger.Level.INFO, "Getting events from database for month " + month + " of year " + year);
        // Create the query filter
        Bson filter = and(
                eq("$expr", and(
                        eq("$eq", List.of(
                                new Document("$year", "$date"),
                                year
                        )),
                        eq("$eq", List.of(
                                new Document("$month", "$date"),
                                month
                        )),
                        eq("$gte", List.of("$date", startDate.atStartOfDay())),
                        eq("$lte", List.of("$date", endDate.atTime(23, 59, 59)))
                ))
        );

        // Retrieve the events matching the filter
        List<CalendarActivity> eventsList = new ArrayList<>();
        try (MongoCursor<CalendarActivity> cursor = events.find(filter).iterator()) {
            while (cursor.hasNext()) {
                eventsList.add(cursor.next());
            }
        }
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");

        return eventsList;
    }

    public List<CalendarActivity> getNearestEvents(int limit) {
        logger.log(System.Logger.Level.INFO, "Getting " + limit + " nearest events from database");
        // Create the query filter
        Bson filter = and(
                eq("$expr", and(
                        eq("$gte", List.of("$date", LocalDate.now().atStartOfDay())),
                        eq("$lte", List.of("$date", LocalDate.now().plusDays(7).atTime(23, 59, 59)))
                ))
        );

        // Retrieve the events matching the filter
        List<CalendarActivity> eventsList = new ArrayList<>();
        try (MongoCursor<CalendarActivity> cursor = events.find(filter).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                eventsList.add(cursor.next());
            }
        }
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");

        return eventsList;
    }
}
