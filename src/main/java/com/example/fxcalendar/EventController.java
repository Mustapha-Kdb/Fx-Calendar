package com.example.fxcalendar;

import javafx.geometry.Bounds;

public class EventController {
    private  String date;
    private String description;

    public EventController(String date, String description) {
        this.date = date;
        this.description = description;
    }

    public static Bounds load(String content) {
        return null;

    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

}
