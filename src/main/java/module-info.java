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
    exports com.eventscheduler.controller.calendar;
    opens com.eventscheduler.controller.calendar to javafx.fxml;
    exports com.eventscheduler.controller.event;
    opens com.eventscheduler.controller.event to javafx.fxml;
    opens com.eventscheduler.controller.event.utils to javafx.fxml;
    exports com.eventscheduler.controller.event.utils;
    exports com.eventscheduler.observer;
    opens com.eventscheduler.observer to javafx.fxml;
    exports com.eventscheduler.view;
    opens com.eventscheduler.view to javafx.fxml;
    exports com.eventscheduler.model.dockuments;
    opens com.eventscheduler.model.dockuments to javafx.fxml;
    exports com.eventscheduler.model.dao;
    opens com.eventscheduler.model.dao to javafx.fxml;
}