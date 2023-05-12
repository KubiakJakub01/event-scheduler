package com.eventscheduler;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private static final System.Logger logger = System.getLogger(CalendarActivity.class.getName());

    private String title;
    private ZonedDateTime date;
    private String place;
    private Double time;
    private String description;

    public CalendarActivity(String title, ZonedDateTime date, Double time, String place, String description) {
        this.title = title;
        this.date = date;
        this.place = place;
        this.time = time;
        this.description = description;
        // Log the creation of a new event with to string method
        logger.log(System.Logger.Level.INFO, this.toString());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
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
        sb.append("CalendarActivity{");
        sb.append("title='");
        sb.append(title);
        sb.append("', date='");
        sb.append(date);
        sb.append("', place='");
        sb.append(place);
        sb.append("', time='");
        sb.append(time);
        sb.append("', description='");
        sb.append(description);
        sb.append("'}");
        return sb.toString();
    }
}
