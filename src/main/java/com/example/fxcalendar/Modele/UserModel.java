package com.example.fxcalendar.Modele;

import biweekly.component.VEvent;
import com.example.fxcalendar.ICalendarReader;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserModel {
    private String id; 
    private String username;
    private String password;
    private String role;
    private String formation;
    private String theme;

    private List<EventModel> events;
    private List<EventModel> AllEvents;


    @JsonCreator
    public UserModel(@JsonProperty("id") String id,@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") String role, @JsonProperty("formation") String formation, @JsonProperty("theme") String theme, @JsonProperty("events") List<EventModel> events) {
        this.id=id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.formation = formation;
        this.theme = theme;
        this.events = events == null ? new ArrayList<>() : events;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    
    public List<EventModel> getEvents() {
        return events;
    }

    public List<EventModel> getAllEventsDay(LocalDate day) {
        List<EventModel> userEvents = this.getEvents(); 
        List<EventModel> formationEvents = this.getFormationEventsday(this.getFormation(),day); 
        
        List<EventModel> allEvents = new ArrayList<>(userEvents);
        allEvents.addAll(formationEvents);

        return allEvents; 
    }


    private List<EventModel> getFormationEventsday(String formation,LocalDate day) {
        
        ICalendarReader calendarReader = new ICalendarReader();

        
        List<VEvent> vEvents = calendarReader.fetchAndParseCalendarData(formation);

        
        List<EventModel> formationEvents = convertVEventsToEventModels(vEvents,day);

        return formationEvents;
    }

    public List<EventModel> convertVEventsToEventModels(List<VEvent> vEvents,LocalDate day) {
        return vEvents.stream()
                .filter(vEvent -> vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(day)) 
                .map(vEvent -> {
            
            String title = vEvent.getSummary().getValue();
            String description = vEvent.getDescription() != null ? vEvent.getDescription().getValue() : ""; 
            
            String date = vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(); 
            String startHour = vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString();
            String endHour = vEvent.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString();
            String location = "";
            String color = ""; 
            String duration = ""; 
            String formation = this.formation; 

            return new EventModel(username,title, description, date, startHour, endHour, location, color, duration, formation);
        }).collect(Collectors.toList());
    }

    public void setAllEvents(List<EventModel> AllEvents) {
        this.AllEvents = AllEvents;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
    }

    public void addEvent(EventModel event) {
        this.events.add(event);

    }

    public boolean isUserFree(UserModel user, LocalDateTime eventStart, LocalDateTime eventEnd, LocalDateTime eventStartOld, LocalDateTime eventEndOld,LocalDate day) {
        for (EventModel event : user.getAllEventsDay(day)) {
            
            LocalTime eventStartTime = LocalTime.parse(event.getStartHour());
            LocalTime eventEndTime = LocalTime.parse(event.getEndHour());
            LocalDate eventDate = LocalDate.parse(event.getDate()); 

            LocalDateTime eventStartDateTime = LocalDateTime.of(eventDate, eventStartTime);
            LocalDateTime eventEndDateTime = LocalDateTime.of(eventDate, eventEndTime);



            if (eventStartDateTime.isBefore(eventEnd) && eventEndDateTime.isAfter(eventStart)
            && !eventStartDateTime.isEqual(eventStartOld) && !eventEndDateTime.isEqual(eventEndOld)
            ) {
                return false;
            }
        }
        return true;
    }

}
