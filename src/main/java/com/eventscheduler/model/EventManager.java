package com.eventscheduler.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class EventManager implements DBManager<EventModel>{
    private static final System.Logger logger = System.getLogger(EventManager.class.getName());

    MongoCollection<EventModel> events;

    public EventManager(MongoCollection<EventModel> events) {
        this.events = events;
    }

    @Override
    public void addElement(EventModel event) {
        events.insertOne(event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " added to database");
    }

    @Override
    public void removeElement(EventModel event) {
        events.deleteOne((Bson) event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " deleted from database");
    }

    @Override
    public void updateElement(EventModel event, EventModel updatedEvent) {
        Bson filter = Filters.eq("_id", event.getId());
        events.updateOne(filter, new Document("$set", updatedEvent));
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " updated in database");
    }


    @Override
    public EventModel getElement(EventModel event) {
        logger.log(System.Logger.Level.INFO, "Getting event " + event.getTitle() + " from database");
        return events.find((Bson) event).first();
    }

    @Override
    public List<EventModel> getAllElements() {
        logger.log(System.Logger.Level.INFO, "Getting all events from database");
        List<EventModel> eventsList = events.find().into(new ArrayList<>());
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");
        return eventsList;
    }

    public List<EventModel> getEventsByMonth(int year, int month) {
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
        List<EventModel> eventsList = new ArrayList<>();
        try (MongoCursor<EventModel> cursor = events.find(filter).iterator()) {
            while (cursor.hasNext()) {
                eventsList.add(cursor.next());
            }
        }
        // Sort the events by date
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");

        return eventsList;
    }

    public List<EventModel> getEventsByDay(int year, int month, int day){
        // Create the start and end date of the day
        LocalDate startDate = LocalDate.of(year, month, day);
        LocalDate endDate = startDate.plusDays(1);

        logger.log(System.Logger.Level.INFO, "Getting events from database for day " + day + " of month " + month + " of year " + year);
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
                        eq("$eq", List.of(
                                new Document("$dayOfMonth", "$date"),
                                day
                        )),
                        eq("$gte", List.of("$date", startDate.atStartOfDay())),
                        eq("$lte", List.of("$date", endDate.atTime(23, 59, 59)))
                ))
        );

        // Define the sort order
        Bson sort = Sorts.ascending("date");

        // Retrieve the events matching the filter
        List<EventModel> eventsList = new ArrayList<>();
        try (MongoCursor<EventModel> cursor = events.find(filter).sort(sort).iterator()) {
            while (cursor.hasNext()) {
                eventsList.add(cursor.next());
            }
        }
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");

        return eventsList;
    }

    public List<EventModel> getNearestEvents(int limit) {
        logger.log(System.Logger.Level.INFO, "Getting " + limit + " nearest events from database");
        // Create the query filter
        Bson filter = and(
                eq("$expr", and(
                        eq("$gte", List.of("$date", LocalDate.now().atStartOfDay()))
                ))
        );

        // Define the sort order
        Bson sort = Sorts.ascending("date");

        // Retrieve the events matching the filter
        List<EventModel> eventsList = new ArrayList<>();
        try (MongoCursor<EventModel> cursor = events.find(filter).sort(sort).limit(limit).iterator()) {
            while (cursor.hasNext()) {
                eventsList.add(cursor.next());
            }
        }
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");

        return eventsList;
    }
}
