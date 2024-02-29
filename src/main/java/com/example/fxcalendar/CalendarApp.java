package com.example.fxcalendar;

import biweekly.component.VEvent;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.property.Summary;

import java.util.List;

public class CalendarApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Calendar");

        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        // Use ICalendarReader with Biweekly
        ICalendarReader calendarReader = new ICalendarReader();
        List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData("https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200ff18615ca376c914d64718ba1c1b53f0e353a91e020e3db5ad510e3a1c6b2cddce1c75be4152b1f5a3e02d9afa7ac932694ecac8253a834133608548850e6039c8939b0c4a23636bb6d2a703da29063dd761acc6");

        for (VEvent event : events) {
            if (event.getSummary() != null) {
                items.add(event.getSummary().getValue());
            }
        }

        listView.setItems(items);
        Scene scene = new Scene(listView, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

