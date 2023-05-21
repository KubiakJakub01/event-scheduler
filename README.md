# event-scheduler

## Overview

The Event Scheduler project is a simple software application designed to help users manage their events and schedules effectively. It provides a user-friendly interface for creating, viewing, and managing events in a calendar format. The project is implemented using Java and JavaFX for the graphical user interface.

The key components of the Event Scheduler project are as follows:

* EventManager: This class serves as the central component for managing events. 
It handles operations such as creating new events, retrieving events based on different criteria (e.g., month, day), and maintaining a list of upcoming events.

* EventModel: The EventModel class represents an individual event. 
It contains information about the event, including the date and time, location, duration, and any additional details. 
It also provides methods for accessing and modifying the event attributes.

* CalendarController: The CalendarController class acts as the controller in the Model-View-Controller (MVC) architecture. 
It handles user interactions with the calendar view, including navigating between months, creating new events, and displaying event details. 
It also updates the calendar view based on changes in the event data.

* EventController: The EventController class is responsible for managing the event creation window. 
It communicates with the EventManager to add new events to the system and updates the event list accordingly.

* EventDetailController: The EventDetailController class handles the event detail window, which displays information about a specific event. 
It allows users to view and modify event details, such as the date, time, location, and duration.

* ConnectionDB: The ConnectionDB class is responsible for connecting to the mongoDB database.

## The technology stack

This project is implemented with Java version 17 and JavaFX version 17.0.1. 
The mongoDB database is used to store event data. Database connection is established using the mongo-java-driver version 4.9.0.
The project is built using Maven. For more information about the dependencies, please refer to the pom.xml file.
