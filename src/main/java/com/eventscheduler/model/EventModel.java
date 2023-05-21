/**
 * Model class for Event.
 * Implements Model interface.
 * Represents an event with a title, date, duration, place, and description.
 */
package com.eventscheduler.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class EventModel implements Model {
    private static final System.Logger logger = System.getLogger(EventModel.class.getName());

    @BsonId
    private ObjectId id;
    private String title;
    private LocalDateTime date;
    private Double duration;
    private String place;
    private String description;

    public EventModel() {
    }

    /**
     * Constructs an EventModel object with the specified title, date, duration, place, and description.
     *
     * @param title
     * @param date
     * @param duration
     * @param place
     * @param description
     */
    public EventModel(String title, LocalDateTime date, Double duration, String place, String description) {
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.place = place;
        this.description = description;
        // Log the creation of a new event with to string method
        logger.log(System.Logger.Level.INFO, this.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof EventModel))
            return false;
        if (obj == this)
            return true;
        return this.title.equals(((EventModel) obj).title) && this.date.equals(((EventModel) obj).date)
                && this.duration.equals(((EventModel) obj).duration)
                && this.place.equals(((EventModel) obj).place)
                && this.description.equals(((EventModel) obj).description);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("EventModel{");
        sb.append("title='");
        sb.append(title);
        sb.append("', date='");
        sb.append(date);
        sb.append("', place='");
        sb.append(place);
        sb.append("', duration='");
        sb.append(duration);
        sb.append("', description='");
        sb.append(description);
        sb.append("'}");
        return sb.toString();
    }
}
