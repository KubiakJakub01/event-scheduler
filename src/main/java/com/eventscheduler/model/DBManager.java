package com.eventscheduler.model;

import java.util.List;

public interface DBManager<Model> {

    void addElement(Model model);

    void removeElement(Model model);

    void updateElement(Model model, Model updatedModel);

    Model getElement(Model model);

    List<Model> getAllElements();

}
