package com.eventscheduler.controller.utils;

import com.eventscheduler.controller.CalendarController;
import com.eventscheduler.model.EventModel;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarUtils {
    private static final System.Logger logger = System.getLogger(CalendarUtils.class.getName());

    private CalendarController calendarController;

    public CalendarUtils(CalendarController calendarController){
        this.calendarController = calendarController;
    }
    /**
     * Draw calendar with month and year
     */
    public FlowPane drawCalendar() {
        FlowPane calendarPane = calendarController.getCalendarPane();
        LocalDateTime dateFocus = calendarController.getDateFocus();
        LocalDateTime today = LocalDateTime.now();

        double calendarWidth = calendarPane.getPrefWidth();
        double calendarHeight = calendarPane.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendarPane.getHgap();
        double spacingV = calendarPane.getVgap();

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
                calendarPane.getChildren().add(stackPane);
            }
        }
        return calendarPane;
    }

    /**
     * Create one day activity with a list of activities
     *
     * @param calendarActivities List of activities for a given day
     * @param rectangleHeight    Height of the rectangle
     * @param rectangleWidth     Width of the rectangle
     * @param stackPane          StackPane to add the activity to
     */
    private void createCalendarActivity(List<EventModel> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On Text clicked
                    logger.log(System.Logger.Level.INFO, "Event clicked");
                    calendarController.drawDayEvents(calendarActivities.get(0).getDate());
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
                calendarController.openDetailEventWindow(calendarActivities.get(finalK));
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    /**
     * Create a map of day and activities for a given month
     * The key is the day of the month and the value is a list of activities for that day
     *
     * @param calendarActivities List of activities for a given month
     * @return Map of day and activities for a given month
     */
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

    /**
     * Get all activities for a given month from the database
     *
     * @param dateFocus Date to focus on
     * @return Map of day and activities for a given month
     */
    private Map<Integer, List<EventModel>> getCalendarActivitiesMonth(LocalDateTime dateFocus) {
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        List<EventModel> monthEventModelList = calendarController.getEventManager().getEventsByMonth(year, month);

        return createCalendarMap(monthEventModelList);
    }
}
