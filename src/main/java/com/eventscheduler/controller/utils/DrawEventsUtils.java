package com.eventscheduler.controller.utils;

import com.eventscheduler.controller.CalendarController;
import com.eventscheduler.model.EventModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DrawEventsUtils {
    private static final System.Logger logger = System.getLogger(DrawEventsUtils.class.getName());

    private CalendarController calendarController;

    public DrawEventsUtils(CalendarController calendarController){
        this.calendarController = calendarController;
    }

    /**
     * Create and show pane with upcoming events
     * The pane is a list of events sorted by date. The number of events is limited by LIMIT_UPCOMING_EVENT
     */
    public ScrollPane drawUpcomingEvents(double width, double height) {
        ScrollPane scrollPane = new ScrollPane();
        VBox dayEventList = new VBox();
        List<EventModel> eventModelList = calendarController.getEventManager().getNearestEvents(calendarController.getLIMIT_UPCOMING_EVENT());

        for (EventModel eventModel : eventModelList) {
            StringBuffer sb = new StringBuffer();
            sb.append(eventModel.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            sb.append(": ");
            sb.append(eventModel.getPlace());
            sb.append(", ");
            sb.append(eventModel.getDuration());
            Text text = new Text(sb.toString());
            dayEventList.getChildren().add(text);
            dayEventList.getChildren().add(new Text("\n"));
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                logger.log(System.Logger.Level.INFO, "Event " + eventModel.getId() + " clicked");
                calendarController.openDetailEventWindow(eventModel);
            });
        }
        scrollPane.setPrefHeight(height);
        scrollPane.setPrefWidth(width);
        scrollPane.setContent(dayEventList);
        return scrollPane;
    }

    /**
     * Create and show pane with events for a given day
     * The pane is a list of events sorted by date.
     *
     * @param dateFocus Date to focus on
     */
    public ScrollPane drawDayEvents(LocalDateTime dateFocus, double width, double height) {
        ScrollPane scrollPane = new ScrollPane();
        VBox dayEventList = new VBox();
        // Sort by date
        List<EventModel> eventModelList = calendarController.getEventManager().getEventsByDay(dateFocus.getYear(), dateFocus.getMonth().getValue(), dateFocus.getDayOfMonth());

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
                calendarController.openDetailEventWindow(eventModel);
            });
        }
        // Add scroll bar
        scrollPane.setPrefHeight(height);
        scrollPane.setPrefWidth(width);
        scrollPane.setContent(dayEventList);
        return scrollPane;
    }
}
