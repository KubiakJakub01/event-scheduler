package com.eventscheduler;

import com.eventscheduler.model.EventModel;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class EventController implements Initializable {
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
    private Button handleSubmitButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private void handleSubmit() {
        String title = titleField.getText();
        int hour = (int) hourSpinner.getValue();
        int minute = (int) minuteSpinner.getValue();
        LocalDateTime date = LocalDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(), datePicker.getValue().getDayOfMonth(), hour, minute);
        Double duration = Double.parseDouble(timeField.getText());
        String place = placeField.getText();
        String description = descriptionTextArea.getText();
        submitNewEvent(title, date, duration, place, description);
    }

    private void submitNewEvent(String title, LocalDateTime date, Double duration, String place, String description) {
        EventModel newEvent = new EventModel(title, date, duration, place, description);
        calendarActivityObservable.notifyObservers(newEvent);
        logger.log(System.Logger.Level.INFO, "New event submitted");
        closeEventWindow();
    }

    private void closeEventWindow() {
        Stage stage = (Stage) handleSubmitButton.getScene().getWindow();
        stage.close();
        logger.log(System.Logger.Level.INFO, "Event window closed");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize spinners
        initSpinners();
        // Allow only numbers in time field
        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timeField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        // Disable submit button until all fields are filled
        BooleanBinding booleanBinding = titleField.textProperty().isEmpty()
                .or(datePicker.valueProperty().isNull())
                .or(timeField.textProperty().isEmpty())
                .or(placeField.textProperty().isEmpty());
        handleSubmitButton.disableProperty().bind(booleanBinding);

    }

    public void setCalendarActivityObservable(CalendarActivityObservable calendarActivityObservable) {
        this.calendarActivityObservable = calendarActivityObservable;
    }
}
