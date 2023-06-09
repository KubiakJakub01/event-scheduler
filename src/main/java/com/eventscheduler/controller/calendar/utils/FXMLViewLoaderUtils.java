package com.eventscheduler.controller.calendar.utils;

import com.eventscheduler.controller.event.EventController;
import com.eventscheduler.controller.event.EventDetailController;
import com.eventscheduler.model.dao.EventManager;
import com.eventscheduler.model.dockuments.EventModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLViewLoaderUtils {
    private static final String FXML_RESOURCE_PATH = "/com/eventscheduler/";
    private final System.Logger logger = System.getLogger(FXMLViewLoaderUtils.class.getName());

    private FXMLLoader getFXMLLoader(String fxmlFileName) {
        String fxmlFilePath = FXML_RESOURCE_PATH + fxmlFileName;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlFilePath));
        return fxmlLoader;
    }

    private void setStage(Stage stage, Parent root, String title) {
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            stage.close();
        });
        stage.setResizable(false);
        stage.show();
    }

    public void openDetailEventWindow(EventModel eventModel,
                                      EventManager eventManager) {
        try {
            FXMLLoader fxmlLoader = getFXMLLoader("EventDetailView.fxml");
            Parent root = fxmlLoader.load();
            EventDetailController eventDetailController = fxmlLoader.getController();
            eventDetailController.initController(eventManager, eventModel);
            setStage(new Stage(), root, "Event detail");
            logger.log(System.Logger.Level.INFO, "Event detail window opened");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, e.getMessage());
        }
    }

    public void openNewEventWindow(EventManager eventManager) {
        try {
            FXMLLoader fxmlLoader = getFXMLLoader("EventForm.fxml");
            Parent root = fxmlLoader.load();
            EventController eventController = fxmlLoader.getController();
            eventController.initController(eventManager);
            setStage(new Stage(), root, "New event");
            logger.log(System.Logger.Level.INFO, "Event window opened");
        } catch (IOException e) {
            logger.log(System.Logger.Level.ERROR, e.getMessage());
        }
    }

}
