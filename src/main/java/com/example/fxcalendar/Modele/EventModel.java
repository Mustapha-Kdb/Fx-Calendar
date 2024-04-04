package com.example.fxcalendar.Modele;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventModel {
    private String id;
    private String title;
    private String description;
    private String location;
    private String date;
    private String startHour;
    private String endHour;
    private String duration;
    private String color;
    private String formation;

    
    @JsonCreator
    public EventModel(
        
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("date") String date,
        @JsonProperty("startTime") String startHour,
        @JsonProperty("endTime") String endHour,
        @JsonProperty("location") String location,
        @JsonProperty("color") String color,
        @JsonProperty("duration") String duration,
        @JsonProperty("formation") String formation
        )
    {
        this.id=id;
        this.title=title;
        this.description = description;
        this.date= date;
        this.startHour=startHour;
        this.endHour=endHour;
        this.location=location;
        this.color=color;
        this.duration=duration;
        this.formation=formation;

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

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
