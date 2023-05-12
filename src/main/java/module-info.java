module com.example.eventscheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.eventscheduler to javafx.fxml;
    exports com.eventscheduler;
}