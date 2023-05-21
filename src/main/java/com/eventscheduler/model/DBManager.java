package com.eventscheduler.model;

import java.util.List;

/**
 * Interface for the database manager
 *
 * @param <Model> The model to be managed
 */
public interface DBManager<Model> {

    /**
     * Adds an element to the database
     *
     * @param model
     */
    void addElement(Model model);

    /**
     * Removes an element from the database
     *
     * @param model
     */
    void removeElement(Model model);

    /**
     * Updates an element in the database
     *
     * @param model
     * @param updatedModel
     */
    void updateElement(Model model, Model updatedModel);

    /**
     * Gets an element from the database
     *
     * @param model
     * @return
     */
    Model getElement(Model model);

    /**
     * Gets all elements from the database
     *
     * @return
     */
    List<Model> getAllElements();

}
