package com.eventscheduler.controller;

import com.eventscheduler.model.Model;

public interface Observer {
    void update(Model model);
}
