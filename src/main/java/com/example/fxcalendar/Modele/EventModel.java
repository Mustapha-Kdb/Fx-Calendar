package com.example.fxcalendar.Modele;

import biweekly.property.DateEnd;
import biweekly.property.DateStart;

public class EventModel {
    private String title;
    private String description;
    private String location;
    private String date;
    private DateStart startHour;

    private DateEnd endHour;
    private String duration;

    public EventModel(String title, String description, DateStart startHour,String endHour,String location, String date, String duration) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.startHour = startHour;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DateStart getStartHour() {
        return startHour;
    }

    public void setStartHour(DateStart startHour) {
        this.startHour = startHour;
    }

    public DateEnd getEndHour() {
        return endHour;
    }

    public void setEndHour(DateEnd endHour) {
        this.endHour = endHour;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


}
