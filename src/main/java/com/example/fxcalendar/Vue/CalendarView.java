package com.example.fxcalendar.Vue;

import biweekly.component.VEvent;
import com.example.fxcalendar.Controleur.CalendarController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CalendarView {

    private final GridPane calendarGrid;
    private CalendarController calendarController;

    private List<biweekly.component.VEvent> events;

    public CalendarView(List<biweekly.component.VEvent> events) {
        calendarGrid = new GridPane();
        this.events = events;
    }

    public List<VEvent> getEvents() {
        return this.events;
    }

    public void setEvents(List<biweekly.component.VEvent> events) {
        this.events = events;
    }



    // displayEventsParMois
    public void displayEventsParMois(List<biweekly.component.VEvent> events, YearMonth Month) {
        //vider le GridPane avant d'ajouter de nouveaux événements
        calendarGrid.getChildren().clear();


        // Nombre total de jours dans le mois
        int totalDaysInMonth = Month.lengthOfMonth();

        // Nombre de colonnes et de lignes dans la grille (GridPane)
        int numColumns = 7; // 7 jours dans une semaine
        int numRows = (totalDaysInMonth + numColumns - 1) / numColumns; // Arrondi vers le haut

        // Déclaration et initialisation des variables columnIndex et rowIndex
        int columnIndex = 0;
        int rowIndex = 0;

        // Créer un tableau pour suivre le nombre de séances pour chaque jour du mois
        int[] sessionCountPerDay = new int[totalDaysInMonth];
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dayOfMonth = eventDate.getDayOfMonth();
            sessionCountPerDay[dayOfMonth - 1]++;
        }

        // Ajouter un texte pour chaque jour dans le calendrier avec le nombre de séances
        for (int i = 0; i < totalDaysInMonth; i++) {
            // Créer un label pour afficher le jour du mois
            Label dayLabel = new Label(Integer.toString(i + 1));
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

            // Créer un label pour afficher le nombre de séances pour ce jour
            Label sessionCountLabel = new Label(Integer.toString(sessionCountPerDay[i]));

            // Créer un VBox pour contenir les labels du jour et du nombre de séances
            VBox dayVBox = new VBox();
            dayVBox.setAlignment(Pos.CENTER); // Aligner le contenu au centre
            dayVBox.getChildren().addAll(dayLabel, sessionCountLabel);

            // Ajouter un style CSS au VBox pour un meilleur aspect visuel
            dayVBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");

            // Ajouter un gestionnaire d'événements pour le survol de la souris
            int finalI = i;
            dayVBox.setOnMouseEntered(e -> {
                // Gérer l'événement de survol
                // Afficher les détails des séances pour ce jour
                // par exemple, en affichant une info-bulle avec les détails
                String sessionDetails = getSessionDetailsForDay(finalI + 1);
                Tooltip tooltip = new Tooltip(sessionDetails);
                Tooltip.install(dayVBox, tooltip);
            });

            // Ajouter un gestionnaire d'événements pour le clic de souris
            int finalI1 = i;
            dayVBox.setOnMouseClicked(e -> {
                // Gérer l'événement de clic
                // Afficher la semaine ou le jour cliqué
                // par exemple, en mettant à jour l'affichage avec les séances pour cette semaine ou ce jour
                LocalDate clickedDate = LocalDate.of(Month.getYear(), Month.getMonthValue(), finalI1 + 1);
                displayEventsParJour(events, clickedDate);
            });

            // Ajouter le VBox à la grille (GridPane) à la position appropriée
            calendarGrid.add(dayVBox, columnIndex, rowIndex);

            // Incrémenter columnIndex et rowIndex
            columnIndex++;
            if (columnIndex == numColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        // Redimensionner les cellules de la grille pour s'adapter à la taille de la grille
        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / numColumns);
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / numRows);
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
    }

    public String getSessionDetailsForDay(int dayOfMonth) {
        // Implémentez la logique pour récupérer les détails des séances pour le jour donné

        return "Details for day " + dayOfMonth;
    }


    // displayEventsParSemaine
    public void displayEventsParSemaine(List<biweekly.component.VEvent> events, LocalDate semaine) {
        // Clear the GridPane before adding new events
        calendarGrid.getChildren().clear();

        // Add headers for days at the top row, column 1-5
        for (int i = 0; i < 5; i++) { // Monday to Friday
            LocalDate dayDate = semaine.plusDays(i);
            String dayLabel = dayDate.format(DateTimeFormatter.ofPattern("EEE dd/MM", Locale.FRANCE));
            Text dayText = new Text(dayLabel);
            dayText.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Ajuster la taille de la police
            calendarGrid.add(dayText, i + 1, 0);
        }


        // Add time labels in the first column, rows 1-24
        for (int i = 8; i <= 19; i++) {
            // For each hour and half-hour slot
            calendarGrid.add(new Text(String.format("%02d:00", i)), 0, (i - 8) * 2 + 1);
            calendarGrid.add(new Text(String.format("%02d:30", i)), 0, (i - 8) * 2 + 2);
        }
        // Ensure that days are fixed and visible, even if there are no events
        for (int i = 0; i < 5; i++) { // Monday to Friday
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / 5); // Répartir la largeur de la colonne uniformément
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }
        // Add events to the appropriate time slots for each day
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (eventDate.isAfter(semaine.minusDays(1)) && eventDate.isBefore(semaine.plusDays(5))) {
                int columnIndex = (int) ChronoUnit.DAYS.between(semaine, eventDate) + 1; // Calculate the column index for the event
                // Calculate the starting row based on the start time
                int startHour = Math.max(startTime.getHour(), 8); // Start at 8 AM if event starts before 8 AM
                int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0);
                // Ensure startRowIndex is non-negative
                startRowIndex = Math.max(startRowIndex, 0);

                // Calculate the ending row based on the end time
                int endHour = Math.min(endTime.getHour(), 19); // End at 7 PM if event ends after 7 PM
                int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() >= 30 ? 1 : 0);
                // Ensure endRowIndex is within bounds
                endRowIndex = Math.min(endRowIndex, 23); // Assuming grid has 24 half-hour rows

                // Calculate the number of half-hour slots to span
                int durationInHalfHours = endRowIndex - startRowIndex + 1;

                // Ensure durationInHalfHours is positive
                durationInHalfHours = Math.max(durationInHalfHours, 1); // At least one half-hour slot

                String summary = event.getSummary() != null ? event.getSummary().getValue() : "No summary provided";
                String description = event.getDescription() != null ? event.getDescription().getValue() : "No description provided";
                // Create the event box and style it
                String displayText = String.format("%s\n%s", summary, description);
                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200); // Wrap text within the specified width
                eventText.setStyle("-fx-font-size: 10;"); // Set the font size if necessary
                VBox eventBox = new VBox(eventText);
                eventBox.setStyle("-fx-background-color: #ADD8E6; -fx-border-color: #000000;");
                eventBox.setPrefWidth(200);
                // Span the event across the correct number of rows from startRowIndex to endRowIndex
                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, durationInHalfHours);

        calendarGrid.add(eventBox, columnIndex, startRowIndex); // Add the event box to the grid
            }
        }
    }

    // displayEventsParJour
    public void displayEventsParJour(List<biweekly.component.VEvent> events, LocalDate jour) {
        // Clear the GridPane before adding new events
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); // Clear existing column constraints

        // Add headers for day at the top row, column 1
        String dayLabel = jour.format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.FRANCE));
        Text dayLabelText = new Text(dayLabel);
        dayLabelText.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Adjust font if needed
        VBox dayHeader = new VBox(dayLabelText);
        dayHeader.setAlignment(Pos.CENTER);
        calendarGrid.add(dayHeader, 1, 0);


        // Set column constraints to make the event column occupy the entire width
        ColumnConstraints eventColumnConstraints = new ColumnConstraints();
        eventColumnConstraints.setPercentWidth(100);
        calendarGrid.getColumnConstraints().add(eventColumnConstraints);


        // Add hours label column
        VBox hoursLabelColumn = new VBox(2);
        for (int i = 8; i <= 19; i++) {
            hoursLabelColumn.getChildren().add(new Text(String.format("%02d:00", i)));
            hoursLabelColumn.getChildren().add(new Text(String.format("%02d:30", i)));
        }
        hoursLabelColumn.setAlignment(Pos.CENTER); // Center align hours labels
        calendarGrid.add(hoursLabelColumn, 0, 1, 1, 5); // Add hours label column to cover all days

        // Add events to the appropriate time slots
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            if (eventDate.equals(jour)) {
                // Calculate the starting row based on the start time
                int startRowIndex = Math.max((startTime.getHour() - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0), 0); // Ensure non-negative
                // Calculate the number of half-hour slots to span
                long durationInHalfHours = ChronoUnit.MINUTES.between(startTime, endTime) / 30;

                String summary = event.getSummary() != null ? event.getSummary().getValue() : "No summary provided";
                String description = event.getDescription() != null ? event.getDescription().getValue() : "No description provided";
                String displayText = String.format("%s\n%s", summary, description);
                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200); // Wrap text within the specified width
                eventText.setStyle("-fx-font-size: 10;"); // Set the font size if necessary
                // Create the event box and style it
                VBox eventBox = new VBox(eventText);
                eventBox.setStyle("-fx-background-color: #ADD8E6; -fx-border-color: #000000;");
                eventBox.setPrefWidth(200);
                eventBox.setAlignment(Pos.CENTER); // Center align event details
                // Span the event across the correct number of rows
                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, (int) Math.max(durationInHalfHours, 1)); // Ensure positive duration
                calendarGrid.add(eventBox, 1, startRowIndex); // Add the event box to the grid
            }
        }
    }

    public GridPane getCalendarGrid() {
        return calendarGrid;
    }

    public void filterEvents(String searchText) {
        // Implémentez la logique pour filtrer les événements en fonction du texte de recherche
        // Exemple de filtrage basique - adaptez selon vos besoins
        List<VEvent> filteredEvents = this.events.stream()
                .filter(e -> e.getSummary().getValue().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Mettez à jour l'affichage avec filteredEvents
        setEvents(filteredEvents,YearMonth.now());
    }
    // Méthode pour ajouter ou définir les événements dans CalendarView
    public void setEvents(List<biweekly.component.VEvent> events,YearMonth Month) {
        this.events = events;
        // Vous pouvez également mettre à jour l'affichage ici pour montrer tous les événements dès qu'ils sont définis
        displayEventsParMois(events,Month);
    }

    public void displayAllEvents() {
        // Affichez tous les événements
        YearMonth currentMonth = YearMonth.now();
        displayEventsParMois(this.events,currentMonth);
    }

    public CalendarController getCalendarController() {
        return calendarController;
    }

    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }

    public void filterEventsDay(LocalDate now) {
        // Trie les événements par date de début
        List<VEvent> nowEvents = getEvents().stream()
                .filter(event -> event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(now))
                .collect(Collectors.toList());
        displayEventsParJour(nowEvents,now);
    }

    public void filterEventsWeek(LocalDate currentSemaine) {
        // Trie les événements par date de début
        List<VEvent> weekEvents = getEvents().stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(currentSemaine.minusDays(1)) && eventDate.isBefore(currentSemaine.plusDays(7));
                })
                .collect(Collectors.toList());
        displayEventsParSemaine(weekEvents,currentSemaine);
    }

    public void filterEventsMonth(YearMonth month) {
        // Déterminer la première et la dernière date du mois
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        // Trie les événements par date de début
        List<VEvent> monthEvents = getEvents().stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(startOfMonth.minusDays(0)) && eventDate.isBefore(endOfMonth.plusDays(0));
                })
                .collect(Collectors.toList());
        YearMonth currentMonth = YearMonth.from(startOfMonth);
        displayEventsParMois(monthEvents, currentMonth);
    }
}