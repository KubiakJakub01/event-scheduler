package com.eventscheduler;

import com.eventscheduler.model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EventDetailController implements Initializable {
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
    private void handleOK(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Ok button pressed");
        // Close the window
        if (isUpdate) {
            // TODO: Update the event
            updateComponents();
            disableUpdateMode();
        }
        else {
            closeDetailEventWindow();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Update button pressed");
        if (isUpdate) {
            disableUpdateMode();
        }
        else {
            setDisableComponents(false);
            isUpdate = true;
            updateButton.setText("Cancel");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        logger.log(System.Logger.Level.INFO, "Delete button pressed");
    }

    private void updateComponents() {
        logger.log(System.Logger.Level.INFO, "Update components");
//        titleField.setText(calendarActivityObservable.getTitle());
//        datePicker.setValue(calendarActivityObservable.getDate().toLocalDate());
//        hourSpinner.getValueFactory().setValue(calendarActivityObservable.getDate().getHour());
//        minuteSpinner.getValueFactory().setValue(calendarActivityObservable.getDate().getMinute());
//        timeField.setText(String.valueOf(calendarActivityObservable.getTime()));
//        placeField.setText(calendarActivityObservable.getPlace());
//        descriptionTextArea.setText(calendarActivityObservable.getDescription());
    }

    private void cancelUpdate() {
        logger.log(System.Logger.Level.INFO, "Cancel update");
    }

    private void disableUpdateMode(){
        setDisableComponents(true);
        isUpdate = false;
        updateButton.setText("Update");
    }

    private void closeDetailEventWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
        logger.log(System.Logger.Level.INFO, "Detail event window closed");
    }

    public void fillComponentsWithData(EventModel eventModel) {
        titleField.setText(eventModel.getTitle());
        datePicker.setValue(eventModel.getDate().toLocalDate());
        hourSpinner.getValueFactory().setValue(eventModel.getDate().getHour());
        minuteSpinner.getValueFactory().setValue(eventModel.getDate().getMinute());
        timeField.setText(String.valueOf(eventModel.getDuration()));
        placeField.setText(eventModel.getPlace());
        descriptionTextArea.setText(eventModel.getDescription());
    }

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

    private void setDisableComponents(boolean disable) {
        titleField.setDisable(disable);
        datePicker.setDisable(disable);
        hourSpinner.setDisable(disable);
        minuteSpinner.setDisable(disable);
        timeField.setDisable(disable);
        placeField.setDisable(disable);
        descriptionTextArea.setDisable(disable);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set all components to be disabled
        setDisableComponents(true);
        // Initialize spinners
        initSpinners();
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
