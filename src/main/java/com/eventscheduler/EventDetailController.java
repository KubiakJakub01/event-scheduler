package com.eventscheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EventDetailController implements Initializable {
    System.Logger logger = System.getLogger(EventController.class.getName());

    private CalendarActivityObservable calendarActivityObservable;

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
    private void handleOK(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Ok button pressed");
        // Close the window
        if (isUpdate) {
            // TODO: Update the event
        }
        else {
            closeDetailEventWindow();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Update button pressed");
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Delete button pressed");
    }

    private void closeDetailEventWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
        logger.log(System.Logger.Level.INFO, "Detail event window closed");
    }

    public void fillComponentsWithData(CalendarActivity calendarActivity) {
        setCalendarActivityObservable(calendarActivityObservable);
        titleField.setText(calendarActivity.getTitle());
        datePicker.setValue(calendarActivity.getDate().toLocalDate());
        hourSpinner.getValueFactory().setValue(calendarActivity.getDate().getHour());
        minuteSpinner.getValueFactory().setValue(calendarActivity.getDate().getMinute());
        timeField.setText(String.valueOf(calendarActivity.getTime()));
        placeField.setText(calendarActivity.getPlace());
        descriptionTextArea.setText(calendarActivity.getDescription());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set all components to be disabled
        titleField.setDisable(true);
        datePicker.setDisable(true);
        hourSpinner.setDisable(true);
        minuteSpinner.setDisable(true);
        timeField.setDisable(true);
        placeField.setDisable(true);
        descriptionTextArea.setDisable(true);
    }

    public CalendarActivityObservable getCalendarActivityObservable() {
        return calendarActivityObservable;
    }

    public void setCalendarActivityObservable(CalendarActivityObservable calendarActivityObservable) {
        this.calendarActivityObservable = calendarActivityObservable;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    public Spinner getHourSpinner() {
        return hourSpinner;
    }

    public void setHourSpinner(Spinner hourSpinner) {
        this.hourSpinner = hourSpinner;
    }

    public Spinner getMinuteSpinner() {
        return minuteSpinner;
    }

    public void setMinuteSpinner(Spinner minuteSpinner) {
        this.minuteSpinner = minuteSpinner;
    }

    public TextField getTimeField() {
        return timeField;
    }

    public void setTimeField(TextField timeField) {
        this.timeField = timeField;
    }

    public TextField getPlaceField() {
        return placeField;
    }

    public void setPlaceField(TextField placeField) {
        this.placeField = placeField;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void setDescriptionTextArea(TextArea descriptionTextArea) {
        this.descriptionTextArea = descriptionTextArea;
    }

    public Button getOkButton() {
        return okButton;
    }

    public void setOkButton(Button okButton) {
        this.okButton = okButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

}