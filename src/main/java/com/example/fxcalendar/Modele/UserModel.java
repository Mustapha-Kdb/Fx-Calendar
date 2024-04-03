package com.example.fxcalendar.Modele;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private String username;
    private String password;
    private String role;
    private String formation;
    private String theme;

    private List<EventModel> events;



    @JsonCreator
    public UserModel(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") String role, @JsonProperty("formation") String formation, @JsonProperty("theme") String theme, @JsonProperty("events") List<EventModel> events) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.formation = formation;
        this.theme = theme;
        this.events = events == null ? new ArrayList<>() : events;

    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    // Getter et setter pour events
    public List<EventModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
    }

    public void addEvent(EventModel event) {
        System.out.println(this+"Ajout de l'événement " + event.getTitle() + " à l'utilisateur " + this.username);
        System.out.println(this+"L'utilisateur " + this.username + " a actuellement " + this.events.size() + " événements.");
        System.out.println(this.events);
        this.events.add(event);
        System.out.println(this+"L'utilisateur " + this.username + " a maintenant " + this.events.size() + " événements.");
        System.out.println(this.events);

    }

}
