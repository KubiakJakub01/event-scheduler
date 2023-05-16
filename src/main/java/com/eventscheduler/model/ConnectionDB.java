package com.eventscheduler.model;

import com.eventscheduler.CalendarActivity;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ConnectionDB {

    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<CalendarActivity> events_collection;

    public ConnectionDB() throws FileNotFoundException {
        // Create connection to mongoDB
        String url = readURL();
        this.mongoClient = MongoClients.create(url);
        this.database = mongoClient.getDatabase("eventSchedulerDB");
        this.events_collection = database.getCollection("events", CalendarActivity.class);
    }

    private String readURL() throws FileNotFoundException {
        // Read URL from file
        FileInputStream urlFile = new FileInputStream("src/main/resources/url.txt");
        return urlFile.toString();
    }
}
