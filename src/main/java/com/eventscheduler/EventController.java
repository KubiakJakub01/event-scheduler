package com.eventscheduler;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EventController implements Initializable {
    System.Logger logger = System.getLogger(EventController.class.getName());;

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeField;

    @FXML
    private TextField placeField;

    @FXML
    private Button handleSubmitButton;

    @FXML
    private void handleSubmit() {
        String title = titleField.getText();
        String date = datePicker.getValue().toString();
        double time = Double.parseDouble(timeField.getText());
        String place = placeField.getText();
        submitNewEvent(title, date, time, place);
    }

    private void submitNewEvent(String title, String date, double time, String place) {
        StringBuffer sb = new StringBuffer();
        sb.append("New event submitted. ");
        sb.append("Title: ");
        sb.append(title);
        sb.append(", Date: ");
        sb.append(date);
        sb.append(", Time: ");
        sb.append(time);
        sb.append(", Place: ");
        sb.append(place);
        logger.log(System.Logger.Level.INFO, sb.toString());
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable submit button until all fields are filled
        BooleanBinding booleanBinding = titleField.textProperty().isEmpty()
                .or(datePicker.valueProperty().isNull())
                .or(timeField.textProperty().isEmpty())
                .or(placeField.textProperty().isEmpty());
        handleSubmitButton.disableProperty().bind(booleanBinding);
    }
}
