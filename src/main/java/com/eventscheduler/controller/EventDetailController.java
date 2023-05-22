package com.eventscheduler.controller;

import com.eventscheduler.model.CRUDOperationExecutor;
import com.eventscheduler.model.EventManager;
import com.eventscheduler.model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * The EventDetailController class is responsible for handling user interactions and managing event details in the event scheduler.
 * It controls the event detail window and communicates with the EventManager.
 */
public class EventDetailController implements Initializable {
    System.Logger logger = System.getLogger(EventController.class.getName());

    private EventManager eventManager;
    private EventModel eventModel;
    private boolean isUpdate = false;
    CRUDOperationExecutor crudOperationExecutor;

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

    /**
     * Initializes the controller.
     *
     * @param url            The URL location of the FXML file.
     * @param resourceBundle The ResourceBundle for the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set all components to be disabled
        setDisableComponents(true);
        // Initialize spinners
        initSpinners();
        setTimeField();
    }

    /**
     * Handles the OK button click event.
     * If in update mode, checks for changes and updates the event. Otherwise, closes the window.
     *
     * @param event The ActionEvent triggered by the OK button.
     */
    @FXML
    private void handleOK(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Ok button pressed");
        // Close the window
        if (isUpdate) {
            EventModel updatedEventModel = createEventModelFromComponents();
            if (updatedEventModel.equals(eventModel)) {
                logger.log(System.Logger.Level.INFO, "No changes detected");
                cancelUpdate();
            } else {
                updateEvent(updatedEventModel);
                disableUpdateMode();
            }
        } else {
            closeDetailEventWindow();
        }
    }

    /**
     * Handles the Update button click event.
     * Toggles between update mode and view mode.
     *
     * @param event The ActionEvent triggered by the Update button.
     */
    @FXML
    private void handleUpdate(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Update button pressed");
        if (isUpdate) {
            disableUpdateMode();
            cancelUpdate();
        } else {
            enableUpdateMode();
        }
    }

    /**
     * Handles the Delete button click event.
     * Removes the event from the event manager and closes the window.
     *
     * @param event The ActionEvent triggered by the Delete button.
     */
    @FXML
    private void handleDelete(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Delete button pressed");
        crudOperationExecutor.deleteWithProgress(eventModel);
        closeDetailEventWindow();
    }

    /**
     * Creates an EventModel object using the values from the form components.
     *
     * @return The created EventModel.
     */
    private EventModel createEventModelFromComponents() {
        String title = titleField.getText();
        int hour = (int) hourSpinner.getValue();
        int minute = (int) minuteSpinner.getValue();
        LocalDateTime date = LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(), datePicker.getValue().getDayOfMonth(), hour, minute);
        Double duration = Double.parseDouble(timeField.getText());
        String place = placeField.getText();
        String description = descriptionTextArea.getText();
        return new EventModel(title, date, duration, place, description);
    }

    /**
     * Updates the event with the provided updatedEventModel.
     *
     * @param updatedEventModel The updated EventModel.
     */
    private void updateEvent(EventModel updatedEventModel) {
        logger.log(System.Logger.Level.INFO, "Send request to update event");
        crudOperationExecutor.updateWithProgress(eventModel, updatedEventModel);
    }

    /**
     * Cancels the update operation and reverts the form components to the original event data.
     */
    private void cancelUpdate() {
        logger.log(System.Logger.Level.INFO, "Cancel update");
        fillComponentsWithData(eventModel);
    }

    /**
     * Enables the update mode by enabling the form components and changing button labels.
     */
    private void enableUpdateMode() {
        setDisableComponents(false);
        isUpdate = true;
        updateButton.setText("Cancel");
        okButton.setText("Save");
    }

    /**
     * Disables the update mode by disabling the form components and changing button labels.
     */
    private void disableUpdateMode() {
        setDisableComponents(true);
        isUpdate = false;
        updateButton.setText("Update");
        okButton.setText("OK");
    }

    /**
     * Closes the detail event window.
     */
    private void closeDetailEventWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
        logger.log(System.Logger.Level.INFO, "Detail event window closed");
    }

    /**
     * Initializes the controller with the provided EventManager and EventModel.
     *
     * @param eventManager The EventManager instance.
     * @param eventModel   The EventModel to display in the detail window.
     */
    public void initController(EventManager eventManager, EventModel eventModel) {
        this.eventManager = eventManager;
        crudOperationExecutor = new CRUDOperationExecutor(eventManager);
        if (eventModel != null) {
            fillComponentsWithData(eventModel);
        }
    }

    /**
     * Fills the form components with data from the provided EventModel.
     *
     * @param eventModel The EventModel containing the event data.
     */
    public void fillComponentsWithData(EventModel eventModel) {
        this.eventModel = eventModel;
        titleField.setText(eventModel.getTitle());
        datePicker.setValue(eventModel.getDate().toLocalDate());
        hourSpinner.getValueFactory().setValue(eventModel.getDate().getHour());
        minuteSpinner.getValueFactory().setValue(eventModel.getDate().getMinute());
        timeField.setText(String.valueOf(eventModel.getDuration()));
        placeField.setText(eventModel.getPlace());
        descriptionTextArea.setText(eventModel.getDescription());
    }

    /**
     * Initializes the spinners for hour and minute selection.
     */
    private void initSpinners() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0, 1));
        hourSpinner.setEditable(true);
        hourSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hourSpinner.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5));
        minuteSpinner.setEditable(true);
        minuteSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minuteSpinner.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Set disable property of all form components.
     *
     * @param disable If disable
     */
    private void setDisableComponents(boolean disable) {
        titleField.setDisable(disable);
        datePicker.setDisable(disable);
        hourSpinner.setDisable(disable);
        minuteSpinner.setDisable(disable);
        timeField.setDisable(disable);
        placeField.setDisable(disable);
        descriptionTextArea.setDisable(disable);
    }

    /**
     * Sets a listener for the time field to restrict input to a valid time format.
     */
    private void setTimeField() {
        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}([\\.]\\d{0,1})?")) {
                timeField.setText(oldValue);
            }
        });
    }

}
