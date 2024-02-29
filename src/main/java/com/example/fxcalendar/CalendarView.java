package com.example.fxcalendar;

import biweekly.component.VEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
public class CalendarView {

    private final GridPane calendarGrid;

    public CalendarView() {
        calendarGrid = new GridPane();
        // Configuration initiale de GridPane, comme définir les espacements, etc.

    }

    public void displayEvents(List<VEvent> events) {
        // Trie les événements par année
        List<VEvent> sortedEvents = events.stream()
                .sorted(Comparator.comparing(event -> event.getDateStart().getValue()))
                .toList();



// Supposons que vous avez un tableau ou une structure de données pour suivre les VBox de chaque jour
        VBox[] dayCells = new VBox[31]; // Pour simplifier, supposons un mois de 31 jours

// Initialisation des VBox pour chaque jour
        for (int i = 0; i < dayCells.length; i++) {
            dayCells[i] = new VBox(5); // 5 est l'espacement entre les éléments dans le VBox
            // Configurez ici si besoin (styles, etc.)
        }

        for (VEvent event : sortedEvents) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Calculer la position dans le GridPane basé sur la date
            String dayOfWeek = eventDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE); // Pour le jour de la semaine en français
            int dayOfMonth = eventDate.getDayOfMonth();

            // Formater la date pour l'affichage
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy", Locale.FRANCE);
            String formattedDate = eventDate.format(formatter);


            // Créer le texte à afficher pour cet événement
            String displayText = String.format("%s\n%s - %s", dayOfWeek, formattedDate, event.getSummary().getValue());
            Text eventText = new Text(displayText);
            dayCells[dayOfMonth - 1].getChildren().add(eventText);


            // Assurez-vous de placer correctement le VBox dans le GridPane, si ce n'est pas déjà fait
        }
        // Supposons que vous voulez afficher le calendrier pour le mois courant
        LocalDate today = LocalDate.now();
        YearMonth month = YearMonth.from(today);
        LocalDate firstDayOfMonth = month.atDay(1);
        DayOfWeek dayOfWeek = firstDayOfMonth.getDayOfWeek();

        // Java retourne 1 pour lundi, 7 pour dimanche. Si vous voulez que dimanche soit 0, ajustez en conséquence
        int startDayOfWeek = dayOfWeek.getValue() % 7; // Ajustez cette ligne si votre semaine commence par dimanche

// Ajoutez les VBox au GridPane à leurs positions appropriées
        for (int i = 0; i < dayCells.length; i++) {
            int column = (i + startDayOfWeek) % 7; // Calculez la colonne, en supposant startDayOfWeek comme le jour de la semaine du 1er du mois
            int row = (i + startDayOfWeek) / 7; // Calculez la ligne
            calendarGrid.add(dayCells[i], column, row);

    }
    }

    public GridPane getCalendarGrid() {
        return calendarGrid;
    }
}
