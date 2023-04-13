module com.example.eventscheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.eventscheduler to javafx.fxml;
    exports com.example.eventscheduler;
}