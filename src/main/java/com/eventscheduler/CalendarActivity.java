package com.eventscheduler;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private static final System.Logger logger = System.getLogger(CalendarActivity.class.getName());

    private String title;
    private ZonedDateTime date;
    private String place;
    private Float time;
    private String description;

    public CalendarActivity(String title, ZonedDateTime date, String clientName, Float time, String description) {
        this.title = title;
        this.date = date;
        this.place = clientName;
        this.time = time;
        this.description = description;
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

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
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
