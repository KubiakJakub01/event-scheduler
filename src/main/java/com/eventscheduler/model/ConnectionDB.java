package com.eventscheduler.model;

import com.eventscheduler.model.dockument.EventModel;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * The ConnectionDB class represents a connection to a MongoDB database.
 * It provides methods to connect to the database, access the database and collection objects,
 * and close the database connection.
 */
public class ConnectionDB {
    private static final System.Logger logger = System.getLogger(ConnectionDB.class.getName());
    private static final String propertiesPath = "src/main/resources/db.properties";
    private static final String databaseName = "eventSchedulerDB";
    private static final String documentName = "events";
    private static ConnectionDB instance;
    private final MongoClient mongoClient;
    private final MongoDatabase db;
    private final MongoCollection<EventModel> events;

    /**
     * Creates a new ConnectionDB object and establishes a connection to the MongoDB database.
     */
    private ConnectionDB() {
        // Create connection to mongoDB
        logger.log(System.Logger.Level.INFO, "Creating connection to database");
        Properties properties = readPropertiesFile(propertiesPath);
        logger.log(System.Logger.Level.INFO, "Properties file read " + properties.getProperty("mongodb.uri"));
        ConnectionString connectionString = new ConnectionString(properties.getProperty("mongodb.uri"));
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().register(EventModel.class).build())
        );
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();
        this.mongoClient = MongoClients.create(clientSettings);
        logger.log(System.Logger.Level.INFO, "Connection to database created");
        this.db = mongoClient.getDatabase(databaseName);
        logger.log(System.Logger.Level.INFO, "Database " + databaseName + " selected");
        this.events = db.getCollection(documentName, EventModel.class);
        logger.log(System.Logger.Level.INFO, "Collection " + documentName + " selected");
    }

    /**
     * Returns the instance of ConnectionDB (Singleton pattern).
     *
     * @return The instance of ConnectionDB.
     */
    public static ConnectionDB getInstance() {
        if (instance == null) {
            synchronized (ConnectionDB.class) {
                if (instance == null) {
                    instance = new ConnectionDB();
                }
            }
        }
        return instance;
    }

    /**
     * Closes the connection to the MongoDB database.
     */
    public void closeConnection() {
        logger.log(System.Logger.Level.INFO, "Closing connection to database");
        mongoClient.close();
        logger.log(System.Logger.Level.INFO, "Connection to database closed");
    }

    /**
     * Reads the properties file at the specified path and returns the properties as a Properties object.
     *
     * @param propertiesPath The path to the properties file.
     * @return The Properties object containing the properties from the file.
     */
    private Properties readPropertiesFile(String propertiesPath) {
        Properties dbProps = new Properties();
        try {
            dbProps.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbProps;
    }

    /**
     * Returns the MongoDatabase object representing the selected database.
     *
     * @return The MongoDatabase object.
     */
    public MongoDatabase getDatabase() {
        return db;
    }

    /**
     * Returns the MongoCollection object representing the selected collection.
     *
     * @return The MongoCollection object.
     */
    public MongoCollection<EventModel> getEvents() {
        return events;
    }
}
