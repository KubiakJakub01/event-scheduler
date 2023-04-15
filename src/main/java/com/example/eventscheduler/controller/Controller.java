package com.example.eventscheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastNameTextField;

    @FXML
    protected void onHelloButtonClick() {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello, ");
        sb.append(nameTextField.getText());
        sb.append(" ");
        sb.append(lastNameTextField.getText());
        welcomeLabel.setText(sb.toString());
    }
}