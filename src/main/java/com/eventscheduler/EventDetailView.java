package com.eventscheduler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EventDetailView {
    System.Logger logger = System.getLogger(EventController.class.getName());

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner hourSpinner;

    @FXML
    private Spinner minuteSpinner;

    @FXML
    private TextField timeField;

    @FXML
    private TextField placeField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button okButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private boolean isUpdate = false;

    @FXML
    private void handleOk() {
        logger.log(System.Logger.Level.INFO, "Ok button pressed");
        // Close the window
        if (isUpdate) {
            // TODO: Update the event
        }
        else {
            closeDetailEventWindow();
        }
    }

    private void closeDetailEventWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
        logger.log(System.Logger.Level.INFO, "Detail event window closed");
    }


}
