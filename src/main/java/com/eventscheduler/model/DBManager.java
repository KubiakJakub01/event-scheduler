package com.eventscheduler.model;

import java.util.List;

public interface DBManager<Model> {

    public void addElement(Model model);
    public void removeElement(Model model);
    public void updateElement(Model model, Model updatedModel);
    public Model getElement(Model model);
    public List<Model> getAllElements();

}
