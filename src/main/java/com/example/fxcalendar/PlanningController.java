package com.example.fxcalendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class PlanningController {

    @FXML
    private TableView<Event> tableView;

    @FXML
    public void initialize() {
        setupTableView();
        loadEvents();
        //loadEventsFromUrl("https://raw.githubusercontent.com/jasig/cas/master/cas-server-webapp/src/main/resources/services/edu-services-2012-11-16.ics");
    }

    private void setupTableView() {
        TableColumn<Event, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Event, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.getColumns().addAll(dateColumn, descriptionColumn);
    }
    public void loadEventsFromUrl(String urlString) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            // Now you have the content from the URL in the content variable
            // You can parse it with VCalendar.parse(content.toString())
        } catch (Exception e) {
            e.printStackTrace();
        }
        VCalendar vCalendar = VCalendar.parse(content.toString());
        ObservableList<Event> events = FXCollections.observableArrayList();
        for (VEvent vEvent : vCalendar.getVEvents()) {
            events.add(new Event(vEvent.getDateTimeStart().getValue().toString(), vEvent.getSummary().getValue()));
        }
        tableView.setItems(events);
    }
    private void loadEvents() {
        //ObservableList<Event> events = FXCollections.observableArrayList();
        // Replace with the correct path to your .ics file
        //content recupere le contenu du fichier txt et le met dans une chaine de caractere
        //String content = "";


        //VCalendar vCalendar = VCalendar.parse(content);
        String content =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                        "VERSION:2.0" + System.lineSeparator() +
                        "PRODID:-//example.com//EN" + System.lineSeparator() +
                        "BEGIN:VEVENT" + System.lineSeparator() +
                        "UID:20230215T080000Z-0001@example.com" + System.lineSeparator() +
                        "DTSTAMP:20230215T080000Z" + System.lineSeparator() +
                        "DTSTART:20230420T090000Z" + System.lineSeparator() +
                        "DTEND:20230420T170000Z" + System.lineSeparator() +
                        "SUMMARY:Annual Marketing Conference" + System.lineSeparator() +
                        "DESCRIPTION:Join us for our annual marketing conference." + System.lineSeparator() +
                        "LOCATION:Grand Hotel" + System.lineSeparator() +
                        "END:VEVENT" + System.lineSeparator() +
                        "BEGIN:VEVENT" + System.lineSeparator() +
                        "UID:20230215T080000Z-0002@example.com" + System.lineSeparator() +
                        "DTSTAMP:20230215T080000Z" + System.lineSeparator() +
                        "DTSTART:20230515" + System.lineSeparator() +
                        "SUMMARY:Alex's Birthday" + System.lineSeparator() +
                        "DESCRIPTION:Remember to buy a gift." + System.lineSeparator() +
                        "END:VEVENT" + System.lineSeparator() +
                        "BEGIN:VEVENT" + System.lineSeparator() +
                        "UID:20230215T080000Z-0003@example.com" + System.lineSeparator() +
                        "DTSTAMP:20230215T080000Z" + System.lineSeparator() +
                        "DTSTART:20230401T120000Z" + System.lineSeparator() +
                        "DTEND:20230401T130000Z" + System.lineSeparator() +
                        "SUMMARY:Project Deadline Reminder" + System.lineSeparator() +
                        "DESCRIPTION:Reminder that the project deadline is approaching." + System.lineSeparator() +
                        "LOCATION:Office" + System.lineSeparator() +
                        "END:VEVENT" + System.lineSeparator() +
                        "END:VCALENDAR";


        VCalendar vCalendar = VCalendar.parse(content);
        ObservableList<Event> events = FXCollections.observableArrayList();
        for (VEvent vEvent : vCalendar.getVEvents()) {
            events.add(new Event(vEvent.getDateTimeStart().getValue().toString(), vEvent.getSummary().getValue()));
        }
        tableView.setItems(events);


}
}
