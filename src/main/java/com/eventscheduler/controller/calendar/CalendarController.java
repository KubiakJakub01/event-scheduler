package com.eventscheduler.controller.calendar;

import com.eventscheduler.controller.calendar.utils.CalendarUtils;
import com.eventscheduler.controller.calendar.utils.DrawEventsUtils;
import com.eventscheduler.controller.calendar.utils.FXMLViewLoaderUtils;
import com.eventscheduler.model.dao.EventManager;
import com.eventscheduler.model.dockuments.EventModel;
import com.eventscheduler.observer.Observer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for the calendar view.
 * Contains methods for drawing the calendar view.
 * It also manages the event creation window and the event detail window.
 */
public class CalendarController implements Initializable, Observer {
    private static final System.Logger logger = System.getLogger(EventModel.class.getName());
    private int LIMIT_UPCOMING_EVENT = 5;

    private EventManager eventManager;
    private LocalDateTime dateFocus;
    private LocalDateTime selectedDate;
    private FXMLViewLoaderUtils fxmlViewLoaderUtils;
    private CalendarUtils calendarUtils;
    private DrawEventsUtils drawEventsUtils;

    @FXML
    private Text yearText;

    @FXML
    private Text monthText;

    @FXML
    private Label timeLabel;

    @FXML
    private FlowPane calendarPane;

    @FXML
    private FlowPane eventListPane;

    @FXML
    private FlowPane dayEventListPane;

    @FXML
    private Spinner eventLimitSpinner;

    /**
     * Initialize the calendar view
     *
     * @param url            URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = LocalDateTime.now();
        selectedDate = LocalDateTime.now();
        initSpinners();
        initTimeLabel();
        fxmlViewLoaderUtils = new FXMLViewLoaderUtils();
        calendarUtils = new CalendarUtils(this);
        drawEventsUtils = new DrawEventsUtils(this);
    }

    /**
     * Update the calendar view
     */
    @Override
    public void update() {
        drawCalendarView();
        logger.log(System.Logger.Level.INFO, "Calendar updated");
    }

    /**
     * Back one month
     *
     * @param event ActionEvent
     */
    @FXML
    public void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendarPane.getChildren().clear();
        drawCalendar();
    }

    /**
     * Forward one month
     *
     * @param event ActionEvent
     */
    @FXML
    public void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendarPane.getChildren().clear();
        drawCalendar();
    }

    /**
     * Open new event creation window
     *
     * @param event ActionEvent
     */
    @FXML
    public void openNewEventWindow(ActionEvent event) {
        fxmlViewLoaderUtils.openNewEventWindow(eventManager);
    }

    /**
     * Init controller with event manager
     *
     * @param eventManager Manager for connecting to the event document in the database
     */
    public void initController(EventManager eventManager) {
        this.eventManager = eventManager;
        drawCalendarView();
    }

    /**
     * Init hour and minute spinner
     */
    private void initSpinners() {
        eventLimitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, LIMIT_UPCOMING_EVENT));
        eventLimitSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            LIMIT_UPCOMING_EVENT = (int) newValue;
            drawUpcomingEvents();
        });
        eventLimitSpinner.setEditable(false);
    }

    /**
     * Init time label to display current time and update it every second
     */
    private void initTimeLabel() {
        timeLabel.setStyle("-fx-font-size: 24px;");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Update the time label with the current time
     */
    private void updateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        timeLabel.setText(formattedTime);
    }

    /**
     * Open the event detail window
     *
     * @param eventModel
     */
    public void openDetailEventWindow(EventModel eventModel) {
        fxmlViewLoaderUtils.openDetailEventWindow(eventModel, eventManager);
    }

    /**
     * Draw the calendar view with upcoming events and day events
     */
    private void drawCalendarView() {
        drawCalendar();
        drawUpcomingEvents();
        if (selectedDate != null) {
            drawDayEvents(selectedDate);
        }
    }

    /**
     * Draw calendar with month and year
     */
    private void drawCalendar() {
        yearText.setText(String.valueOf(dateFocus.getYear()));
        monthText.setText(dateFocus.getMonth().toString());

        calendarPane.getChildren().clear();
        calendarUtils.drawCalendar();
    }

    /**
     * Create and show pane with upcoming events
     * The pane is a list of events sorted by date. The number of events is limited by LIMIT_UPCOMING_EVENT
     */
    private void drawUpcomingEvents() {
        eventListPane.getChildren().clear();
        double width = eventListPane.getPrefWidth();
        double height = eventListPane.getPrefHeight();
        eventListPane.getChildren().add(drawEventsUtils.drawUpcomingEvents(width, height));
    }

    /**
     * Create and show pane with events for a given day
     * The pane is a list of events sorted by date.
     *
     * @param dateFocus Date to focus on
     */
    public void drawDayEvents(LocalDateTime dateFocus) {
        dayEventListPane.getChildren().clear();
        double width = dayEventListPane.getPrefWidth();
        double height = dayEventListPane.getPrefHeight();
        dayEventListPane.getChildren().add(drawEventsUtils.drawDayEvents(dateFocus, width, height));
    }

    public int getLIMIT_UPCOMING_EVENT() {
        return LIMIT_UPCOMING_EVENT;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public LocalDateTime getDateFocus() {
        return dateFocus;
    }

    public FlowPane getCalendarPane() {
        return calendarPane;
    }

}