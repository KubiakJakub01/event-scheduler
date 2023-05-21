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
     * @param model Model to be added
     */
    void addElement(Model model);

    /**
     * Removes an element from the database
     *
     * @param model Model to be removed
     */
    void removeElement(Model model);

    /**
     * Updates an element in the database
     *
     * @param model       Model to be updated
     * @param updatedModel Updated model
     */
    void updateElement(Model model, Model updatedModel);

    /**
     * Gets an element from the database
     *
     * @param model Model to be retrieved
     * @return Model from the database
     */
    Model getElement(Model model);

    /**
     * Gets all elements from the database
     *
     * @return List of all elements from the database
     */
    List<Model> getAllElements();

}
