package com.eventscheduler.model;

import com.mongodb.ConnectionString;
import com.eventscheduler.CalendarActivity;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ConnectionDB {
    private static final System.Logger logger = System.getLogger(ConnectionDB.class.getName());

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<CalendarActivity> events;
    private static final String propertiesPath = "src/main/resources/db.properties";
    private static final String databaseName = "eventSchedulerDB";
    private static final String documentName = "events";


    public ConnectionDB() {
        // Create connection to mongoDB
        logger.log(System.Logger.Level.INFO, "Creating connection to database");
        Properties properties = readPropertiesFile(propertiesPath);
        logger.log(System.Logger.Level.INFO, "Properties file read " + properties.getProperty("mongodb.uri"));
        ConnectionString connectionString = new ConnectionString(properties.getProperty("mongodb.uri"));
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        this.mongoClient = MongoClients.create(clientSettings);
        logger.log(System.Logger.Level.INFO, "Connection to database created");
        this.db = mongoClient.getDatabase(databaseName);
        logger.log(System.Logger.Level.INFO, "Database " + databaseName + " selected");
        this.events = db.getCollection(documentName, CalendarActivity.class);
        logger.log(System.Logger.Level.INFO, "Collection " + documentName + " selected");
//        List<CalendarActivity> eventsList = this.events.find().into(new ArrayList<>());
//        logger.log(System.Logger.Level.INFO, "Events list size: " + eventsList.size());
    }


    private Properties readPropertiesFile(String propertiesPath) {
        Properties dbProps = new Properties();
        try {
            dbProps.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbProps;
    }


    public MongoDatabase getDatabase() {
        return db;
    }

    public MongoCollection<CalendarActivity> getEvents() {
        return events;
    }
}
