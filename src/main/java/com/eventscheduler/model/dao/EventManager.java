package com.eventscheduler.model.dao;

import com.eventscheduler.controller.event.utils.EventObservable;
import com.eventscheduler.model.dockument.EventModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * The EventManager class is responsible for managing events in the MongoDB database.
 * It implements the DBManager interface and provides methods to add, remove, update, and retrieve events from the database.
 * It also provides methods to retrieve events based on specific criteria such as month, day, and nearest events.
 */
public class EventManager implements DBManager<EventModel> {
    private static final System.Logger logger = System.getLogger(EventManager.class.getName());

    MongoCollection<EventModel> events;
    EventObservable eventObservable;

    /**
     * Constructs an EventManager object with the specified MongoCollection and EventObservable.
     *
     * @param events          The MongoCollection representing the events collection in the database.
     * @param eventObservable The EventObservable object to notify observers of changes in events.
     */
    public EventManager(MongoCollection<EventModel> events, EventObservable eventObservable) {
        this.events = events;
        this.eventObservable = eventObservable;
    }

    @Override
    public void addElement(EventModel event) {
        events.insertOne(event);
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " added to database");
        eventObservable.notifyObservers();
    }

    @Override
    public void removeElement(EventModel event) {
        events.deleteOne(eq("_id", event.getId()));
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " removed from database");
        eventObservable.notifyObservers();
    }

    @Override
    public void updateElement(EventModel event, EventModel updatedEvent) {
        Bson filter = Filters.eq("_id", event.getId());
        events.updateOne(filter, new Document("$set", updatedEvent));
        logger.log(System.Logger.Level.INFO, "Event " + event.getTitle() + " updated in database");
        eventObservable.notifyObservers();
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

    /**
     * Retrieves events from the database for the specified month and year.
     *
     * @param year  The year.
     * @param month The month.
     * @return A list of EventModel objects representing the events in the specified month and year.
     */
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

        Bson sort = Sorts.ascending("date");

        // Retrieve the events matching the filter
        List<EventModel> eventsList = new ArrayList<>();
        try (MongoCursor<EventModel> cursor = events.find(filter).sort(sort).iterator()) {
            while (cursor.hasNext()) {
                eventsList.add(cursor.next());
            }
        }
        // Sort the events by date
        logger.log(System.Logger.Level.INFO, "Found " + eventsList.size() + " events in database");

        return eventsList;
    }

    /**
     * Retrieves events from the database for the specified day, month, and year.
     *
     * @param year  The year.
     * @param month The month.
     * @param day   The day.
     * @return A list of EventModel objects representing the events on the specified day, month, and year.
     */
    public List<EventModel> getEventsByDay(int year, int month, int day) {
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

    /**
     * Retrieves the nearest events from the database.
     *
     * @param limit The maximum number of events to retrieve.
     * @return A list of EventModel objects representing the nearest events.
     */
    public List<EventModel> getNearestEvents(int limit) {
        logger.log(System.Logger.Level.INFO, "Getting " + limit + " nearest events from database");
        // Create the query filter
        Bson filter = and(
                eq("$expr", and(
                        eq("$gte", List.of("$date", LocalDateTime.now()))
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
