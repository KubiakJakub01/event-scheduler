package com.eventscheduler.controller;

import com.eventscheduler.model.EventManager;
import com.eventscheduler.model.EventModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarController implements Initializable, Observer {
    private static final System.Logger logger = System.getLogger(EventModel.class.getName());
    private static final int MAX_EVENT_PER_DAY = 3;
    private int LIMIT_UPCOMING_EVENT = 5;

    private EventManager eventManager;
    private LocalDateTime dateFocus;
    private LocalDateTime today;
    private LocalDateTime selectedDate;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private Label timeLabel;

    @FXML
    private FlowPane calendar;

    @FXML
    private FlowPane eventListPane;

    @FXML
    private FlowPane dayEventListPane;

    @FXML
    private Spinner eventLimitSpinner;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = LocalDateTime.now();
        today = LocalDateTime.now();
        selectedDate = LocalDateTime.now();
        initSpinners();
        initTimeLabel();
    }

    @Override
    public void update() {
        drawCalendarView();
        logger.log(System.Logger.Level.INFO, "Calendar updated");
    }

    @FXML
    public void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    public void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    public void openNewEventWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/eventscheduler/EventForm.fxml"));
            Parent root = fxmlLoader.load();
            EventController eventController = fxmlLoader.getController();
            eventController.setEventManager(eventManager);
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Add new event");
            stage.setScene(scene);
            stage.show();
            logger.log(System.Logger.Level.INFO, "Event window opened");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, e.getMessage());
        }
    }

    private void initSpinners() {
        // Init event limit spinner
        eventLimitSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, LIMIT_UPCOMING_EVENT));
        eventLimitSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            LIMIT_UPCOMING_EVENT = (int) newValue;
            drawUpcomingEvents();
        });
        eventLimitSpinner.setEditable(false);
    }

    private void initTimeLabel() {
        timeLabel.setStyle("-fx-font-size: 24px;");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateTime() {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();

        // Format the time as HH:mm:ss
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Update the label with the current time
        timeLabel.setText(formattedTime);
    }


    private void openDetailEventWindow(EventModel eventModel) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/eventscheduler/EventDetailView.fxml"));
            Parent root = fxmlLoader.load();
            EventDetailController eventDetailController = fxmlLoader.getController();
            eventDetailController.initController(eventManager, eventModel);
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Event detail");
            stage.setScene(scene);
            stage.show();
            logger.log(System.Logger.Level.INFO, "Event detail window opened");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, e.getMessage());
        }
    }

    public void initController(EventManager eventManager) {
        this.eventManager = eventManager;
        drawCalendarView();
    }

    private void drawCalendarView() {
        drawCalendar();
        drawUpcomingEvents();
        if (selectedDate != null) {
            drawDayEvents(selectedDate);
        }
    }

    private void drawCalendar() {
        calendar.getChildren().clear();
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<EventModel>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = LocalDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<EventModel> calendarActivities = calendarActivityMap.get(currentDate);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<EventModel> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On Text clicked
                    logger.log(System.Logger.Level.INFO, "Event clicked");
                    selectedDate = calendarActivities.get(0).getDate();
                    drawDayEvents(selectedDate);
                });
                break;
            }
            StringBuffer sb = new StringBuffer();
            // Take maximal 8 characters for the place
            sb.append(calendarActivities.get(k).getPlace(), 0, Math.min(calendarActivities.get(k).getPlace().length(), 8));
            sb.append(", ");
            sb.append(calendarActivities.get(k).getDate().toLocalTime());
            Text text = new Text(sb.toString());
            calendarActivityBox.getChildren().add(text);
            int finalK = k;
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                logger.log(System.Logger.Level.INFO, "Event clicked");
                openDetailEventWindow(calendarActivities.get(finalK));
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<EventModel>> createCalendarMap(List<EventModel> calendarActivities) {
        Map<Integer, List<EventModel>> calendarActivityMap = new HashMap<>();

        for (EventModel activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<EventModel> OldListByDate = calendarActivityMap.get(activityDate);

                List<EventModel> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<EventModel>> getCalendarActivitiesMonth(LocalDateTime dateFocus) {
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        List<EventModel> monthEventModelList = eventManager.getEventsByMonth(year, month);

        return createCalendarMap(monthEventModelList);
    }

    private void drawUpcomingEvents() {
        eventListPane.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        VBox dayEventList = new VBox();
        // Sort by date
        List<EventModel> eventModelList = eventManager.getNearestEvents(LIMIT_UPCOMING_EVENT);

        for (EventModel eventModel : eventModelList) {
            StringBuffer sb = new StringBuffer();
            // Get date in format dd/MM/yyyy HH:mm
            sb.append(eventModel.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            sb.append(": ");
            sb.append(eventModel.getPlace());
            sb.append(", ");
            sb.append(eventModel.getDuration());
            Text text = new Text(sb.toString());
            dayEventList.getChildren().add(text);
            // Add break line
            dayEventList.getChildren().add(new Text("\n"));
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                logger.log(System.Logger.Level.INFO, "Event " + eventModel.getId() + " clicked");
                openDetailEventWindow(eventModel);
            });
        }
        // Add scroll bar
        scrollPane.setPrefHeight(eventListPane.getPrefHeight());
        scrollPane.setPrefWidth(eventListPane.getPrefWidth());
        scrollPane.setContent(dayEventList);
        eventListPane.getChildren().add(scrollPane);
    }

    private void drawDayEvents(LocalDateTime dateFocus) {
        dayEventListPane.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        VBox dayEventList = new VBox();
        // Sort by date
        List<EventModel> eventModelList = eventManager.getEventsByDay(dateFocus.getYear(), dateFocus.getMonth().getValue(), dateFocus.getDayOfMonth());

        for (EventModel eventModel : eventModelList) {
            StringBuffer sb = new StringBuffer();
            // Get date in format dd/MM/yyyy HH:mm
            sb.append(eventModel.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            sb.append(": ");
            sb.append(eventModel.getPlace());
            sb.append(", ");
            sb.append(eventModel.getDuration());
            Text text = new Text(sb.toString());
            dayEventList.getChildren().add(text);
            // Add break line
            dayEventList.getChildren().add(new Text("\n"));
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                logger.log(System.Logger.Level.INFO, "Event clicked");
                openDetailEventWindow(eventModel);
            });
        }
        // Add scroll bar
        scrollPane.setPrefHeight(dayEventListPane.getPrefHeight());
        scrollPane.setPrefWidth(dayEventListPane.getPrefWidth());
        scrollPane.setContent(dayEventList);
        dayEventListPane.getChildren().add(scrollPane);
    }

}