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
    private String id; // Assumant que l'ID est une chaîne

    private String username;
    private String password;
    private String role;
    private String formation;
    private String theme;

    private List<EventModel> events;
    private List<EventModel> AllEvents;


    @JsonCreator
    public UserModel(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") String role, @JsonProperty("formation") String formation, @JsonProperty("theme") String theme, @JsonProperty("events") List<EventModel> events) {
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

    // Getter et setter pour events
    public List<EventModel> getEvents() {
        return events;
    }

    public List<EventModel> getAllEventsDay(LocalDate day) {
        List<EventModel> userEvents = this.getEvents(); // Événements personnels
        List<EventModel> formationEvents = this.getFormationEventsday(this.getFormation(),day); // Événements de la formation
        // Combinez les deux listes d'événements
        List<EventModel> allEvents = new ArrayList<>(userEvents);
        allEvents.addAll(formationEvents);

        return allEvents; // Retournez la liste locale allEvents
    }


    private List<EventModel> getFormationEventsday(String formation,LocalDate day) {
        // Création d'une instance de ICalendarReader pour accéder à la méthode fetchAndParseCalendarData
        ICalendarReader calendarReader = new ICalendarReader();

        // Appel de fetchAndParseCalendarData en utilisant l'instance de ICalendarReader
        List<VEvent> vEvents = calendarReader.fetchAndParseCalendarData(formation);

        // Convertir la liste de VEvent en liste d'EventModel
        List<EventModel> formationEvents = convertVEventsToEventModels(vEvents,day);

        return formationEvents;
    }

    public List<EventModel> convertVEventsToEventModels(List<VEvent> vEvents,LocalDate day) {
        return vEvents.stream()
                .filter(vEvent -> vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(day)) // Filtrer les événements pour le jour spécifié
                .map(vEvent -> {
            // Exemple de conversion, adaptez selon la structure de VEvent et EventModel
            String title = vEvent.getSummary().getValue();
            String description = vEvent.getDescription() != null ? vEvent.getDescription().getValue() : ""; // Vérification de nullité
            // Vous devez ajouter des manipulations pour convertir les dates et heures correctement
            String date = vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(); // Exemple
            String startHour = vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString();
            String endHour = vEvent.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString();
            String location = "";
            String color = ""; // Exemple
            String duration = ""; // Exemple
            String formation = this.formation; // Exemple

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
        System.out.println(this+"Ajout de l'événement " + event.getTitle() + " à l'utilisateur " + this.username);
        System.out.println(this+"L'utilisateur " + this.username + " a actuellement " + this.events.size() + " événements.");
        System.out.println(this.events);
        this.events.add(event);
        System.out.println(this+"L'utilisateur " + this.username + " a maintenant " + this.events.size() + " événements.");
        System.out.println(this.events);

    }

    public boolean isUserFree(UserModel user, LocalDateTime eventStart, LocalDateTime eventEnd, LocalDateTime eventStartOld, LocalDateTime eventEndOld,LocalDate day) {
        for (EventModel event : user.getAllEventsDay(day)) {
            // Assume that event.getStartHour() and event.getEndHour() return times in the format "HH:mm"
            LocalTime eventStartTime = LocalTime.parse(event.getStartHour());
            LocalTime eventEndTime = LocalTime.parse(event.getEndHour());
            LocalDate eventDate = LocalDate.parse(event.getDate()); // Assumes that getDate() returns a date in the format "YYYY-MM-DD"
            System.out.println("Event Date: " + eventDate);
            System.out.println("Event Start Time: " + eventStartTime);
            System.out.println("Event End Time: " + eventEndTime);
            // Combine date and time for comparison
            LocalDateTime eventStartDateTime = LocalDateTime.of(eventDate, eventStartTime);
            LocalDateTime eventEndDateTime = LocalDateTime.of(eventDate, eventEndTime);



            if (eventStartDateTime.isBefore(eventEnd) && eventEndDateTime.isAfter(eventStart)
            && !eventStartDateTime.isEqual(eventStartOld) && !eventEndDateTime.isEqual(eventEndOld)
            ) {
                System.out.println("t'as un cours de " + event.getTitle() + " à cette heure là");
                return false;
            }
        }
        return true;
    }

}
