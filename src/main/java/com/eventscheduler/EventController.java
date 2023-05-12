package com.eventscheduler;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class EventController implements Initializable {
    System.Logger logger = System.getLogger(EventController.class.getName());;

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
        ZonedDateTime date = ZonedDateTime.of(datePicker.getValue().getYear(), datePicker.getValue().getMonthValue(), datePicker.getValue().getDayOfMonth(), hour, minute, 0, 0, ZonedDateTime.now().getZone());
        double time = Double.parseDouble(timeField.getText());
        String place = placeField.getText();
        String description = descriptionTextArea.getText();
        submitNewEvent(title, date, time, place, description);
    }

    private void submitNewEvent(String title, ZonedDateTime date, double time, String place, String description) {
        CalendarActivity newEvent = new CalendarActivity(title, date, time, place, description);
        logger.log(System.Logger.Level.INFO, "New event submitted");
        closeEventWindow();
    }

    public void showEventWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("EventForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
            logger.log(System.Logger.Level.INFO, "Event window opened");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, e.getMessage());
        }
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
        // Disable submit button until all fields are filled
        BooleanBinding booleanBinding = titleField.textProperty().isEmpty()
                .or(datePicker.valueProperty().isNull())
                .or(timeField.textProperty().isEmpty())
                .or(placeField.textProperty().isEmpty());
        handleSubmitButton.disableProperty().bind(booleanBinding);
    }
}
