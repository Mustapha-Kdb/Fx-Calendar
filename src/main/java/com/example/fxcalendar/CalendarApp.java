package com.example.fxcalendar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.layout.HBox;

import java.time.YearMonth;
import java.util.List;

public class CalendarApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("University Calendar");
        ICalendarReader calendarReader = new ICalendarReader();
        List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData("https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200ff18615ca376c914d64718ba1c1b53f0e353a91e020e3db5ad510e3a1c6b2cddce1c75be4152b1f5a3e02d9afa7ac932694ecac8253a834133608548850e6039c8939b0c4a23636bb6d2a703da29063dd761acc6");

        CalendarView calendarView = new CalendarView(events);
        SearchAndFilterPanel searchPanel = new SearchAndFilterPanel(calendarView);
        // ajouter les boutons de suivant et précédent pour changer le mois
        Button previousMonthButton = new Button("Précédent");
        Button nextMonthButton = new Button("Suivant");
        calendarView.setupNavigationButtons(previousMonthButton, nextMonthButton);
        // ajouter les boutons horizontalement
        HBox navigationButtonBox = new HBox(previousMonthButton, nextMonthButton);
        searchPanel.getPanel().getChildren().add(navigationButtonBox);
        calendarView.displayEventsParMois(events, YearMonth.now());


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(calendarView.getCalendarGrid());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox rootLayout = new VBox(searchPanel.getPanel(), scrollPane);

        Scene scene = new Scene(rootLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
