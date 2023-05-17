module com.example.eventscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens com.eventscheduler to javafx.fxml;
    exports com.eventscheduler;
    exports com.eventscheduler.model;
    opens com.eventscheduler.model to javafx.fxml;
}