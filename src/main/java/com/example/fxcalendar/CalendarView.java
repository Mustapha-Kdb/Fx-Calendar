package com.example.fxcalendar;

import biweekly.component.VEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class CalendarView {

    private final GridPane calendarGrid;

    public static String filterOption = "Mois";
    private YearMonth currentMonth = YearMonth.now();
    private LocalDate currentSemaine = LocalDate.now();
    private LocalDate currentJour = LocalDate.now();

    private List<VEvent> events ; // Liste pour stocker les événements

    public CalendarView(List<VEvent> events) {
        calendarGrid = new GridPane();
        this.events = events;
        // Configuration initiale de GridPane, comme définir les espacements, etc.
        calendarGrid.setHgap(10);
    }

    // displayEventsParMois
    // displayEventsParMois
    public void displayEventsParMois(List<VEvent> events, YearMonth Month) {
        //vider le GridPane avant d'ajouter de nouveaux événements
        calendarGrid.getChildren().clear();

        List<VEvent> filteredSortedEvents = events.stream()
                .filter(e -> YearMonth.from(e.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).equals(Month))
                .sorted(Comparator.comparing(e -> e.getDateStart().getValue().toInstant()))
                .collect(Collectors.toList());

        // Trie les événements chaque semaine a part pour les afficher dans l'ordre
        List<VEvent> sortedEvents = filteredSortedEvents.stream()
                .sorted(Comparator.comparing(e -> e.getDateStart().getValue().toInstant()))
                .collect(Collectors.toList());




// Supposons que vous avez un tableau ou une structure de données pour suivre les VBox de chaque jour
        VBox[] dayCells = new VBox[31]; // Pour simplifier, supposons un mois de 31 jours

// Initialisation des VBox pour chaque jour
        for (int i = 0; i < dayCells.length; i++) {
            dayCells[i] = new VBox(2); // 5 est l'espacement entre les éléments dans le VBox
            // Configurez ici si besoin (styles, etc.)
            dayCells[i].setPrefSize(200, 200); // Taille préférée pour chaque VBox
            dayCells[i].setStyle("-fx-border-color: black; -fx-border-width: 1px;"); // Bordure pour chaque VBox
        }

        for (VEvent event : sortedEvents) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Calculer la position dans le GridPane basé sur la date
            String dayOfWeek = eventDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE); // Pour le jour de la semaine en français
            int dayOfMonth = eventDate.getDayOfMonth();

            // Formater la date pour l'affichage et l'heure
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.FRANCE);
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


    // displayEventsParSemaine
    public void displayEventsParSemaine(List<VEvent> events, LocalDate semaine) {
        // Clear the GridPane before adding new events
        calendarGrid.getChildren().clear();

        // Create an array to store VBox for each day of the week
        VBox[] dayCells = new VBox[5]; // For Monday to Friday

        // Initialize VBox for each day of the week
        for (int i = 0; i < dayCells.length; i++) {
            dayCells[i] = new VBox(5); // Spacing between elements in the VBox
            dayCells[i].setPrefSize(50, 20); // Preferred size for each VBox
            dayCells[i].setStyle("-fx-border-color: black; -fx-border-width: 1px;"); // Border for each VBox
            // Add day label for each day of the week
            String displayDay = semaine.plusDays(i).format(DateTimeFormatter.ofPattern("EEE dd/MM", Locale.FRANCE));
            dayCells[i].getChildren().add(new Text(displayDay));
            // Add each VBox to the grid in the correct column
            calendarGrid.add(dayCells[i], i + 1, 0); // Add each VBox to row 0
        }

        // Add hours label column
        VBox hoursLabelColumn = new VBox(2);
        for (int i = 8; i <= 19; i++) {
            hoursLabelColumn.getChildren().add(new Text(String.format("%02d:00", i)));
            hoursLabelColumn.getChildren().add(new Text(String.format("%02d:30", i)));

        }
        calendarGrid.add(hoursLabelColumn, 0, 1, 1, 5); // Add hours label column to cover all days

        // Add events to the appropriate time slots for each day
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (eventDate.isAfter(semaine.minusDays(1)) && eventDate.isBefore(semaine.plusDays(5))) {
                int columnIndex = (int) ChronoUnit.DAYS.between(semaine, eventDate) + 1; // Calculate the column index for the event
                // Calculate the starting row based on the start time
                int startRowIndex = (startTime.getHour() - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0);
                int endRowIndex = (endTime.getHour() - 8) * 2 + (endTime.getMinute() >= 30 ? 1 : 0);
                // Calculate the number of half-hour slots to span
                long durationInHalfHours = ChronoUnit.MINUTES.between(startTime, endTime) / 30;

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
                GridPane.setRowSpan(eventBox, (int) durationInHalfHours);

        calendarGrid.add(eventBox, columnIndex, startRowIndex); // Add the event box to the grid
            }
        }
    }

    // displayEventsParJour
    public void displayEventsParJour(List<VEvent> events, LocalDate jour) {
        // Clear the GridPane before adding new events
        calendarGrid.getChildren().clear();

        // Create an array to store VBox for each time slot
        VBox[] hourCells = new VBox[25]; // For 8 AM to 8 PM in 30-minute intervals

        // Add hours label column
        VBox hoursLabelColumn = new VBox(2);
        for (int i = 8; i <= 19; i++) {
            hoursLabelColumn.getChildren().add(new Text(String.format("%02d:00", i)));
            hoursLabelColumn.getChildren().add(new Text(String.format("%02d:30", i)));
        }
        calendarGrid.add(hoursLabelColumn, 0, 1, 1, 5); // Add hours label column to cover all days


        // Add events to the appropriate time slots
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            if (eventDate.equals(jour)) {
                // Calculate the starting row based on the start time
                int startRowIndex = (startTime.getHour() - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0);
                // Calculate the number of half-hour slots to span
                long durationInHalfHours = ChronoUnit.MINUTES.between(startTime, endTime) / 30;

                String summary = event.getSummary() != null ? event.getSummary().getValue() : "No summary provided";
                String description = event.getDescription() != null ? event.getDescription().getValue() : "No description provided";
                String location = event.getLocation() != null ? event.getLocation().getValue() : "No location provided";
                //TODO: j'ai pas trouver comment getter la categorie et le nom d'enseignant
//                String displayText = String.format("%s\n%s\nEnseignant: %s\nSalle: %s\nType: %s",
//                        summary,
//                        description,
//                        categories,
//                        location,
//                        transparencyValue);
                String displayText = String.format("%s\n%s",
                        summary
                ,description);
                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200); // Wrap text within the specified width
                eventText.setStyle("-fx-font-size: 10;"); // Set the font size if necessary
                // Create the event box and style it
                VBox eventBox = new VBox(eventText);
                eventBox.setStyle("-fx-background-color: #ADD8E6; -fx-border-color: #000000;");
                eventBox.setPrefWidth(200);
                // Span the event across the correct number of rows
                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, (int) durationInHalfHours);
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
    public void setEvents(List<VEvent> events,YearMonth Month) {
        this.events = events;
        // Vous pouvez également mettre à jour l'affichage ici pour montrer tous les événements dès qu'ils sont définis
        displayEventsParMois(events,Month);
    }


    public void setupNavigationButtons(Button prevButton, Button nextButton) {
        prevButton.setOnAction(e -> {
            switch (filterOption) {
                case "Mois":
                    currentMonth = currentMonth.minusMonths(1);
                    break;
                case "Semaine":
                    currentSemaine = currentSemaine.minusWeeks(1);
                    break;
                case "Jour":
                    // Si le jour actuel est un lundi, soustrayez 3 jours pour sauter le week-end
                    if (currentJour.getDayOfWeek() == DayOfWeek.MONDAY) {
                        currentJour = currentJour.minusDays(3);
                    } else {
                        // Sinon, soustrayez simplement un jour
                        currentJour = currentJour.minusDays(1);
                    }
                    break;
            }
            updateCalendarView();
        });

        nextButton.setOnAction(e -> {
            switch (filterOption) {
                case "Mois":
                    currentMonth = currentMonth.plusMonths(1);
                    break;
                case "Semaine":
                    currentSemaine = currentSemaine.plusWeeks(1);
                    break;
                case "Jour":
                    // Si le jour actuel est un vendredi, ajoutez 3 jours pour sauter le week-end
                    if (currentJour.getDayOfWeek() == DayOfWeek.FRIDAY) {
                        currentJour = currentJour.plusDays(3);
                    } else {
                        // Sinon, ajoutez simplement un jour
                        currentJour = currentJour.plusDays(1);
                    }
                    break;
            }
            updateCalendarView();
        });
    }
    void updateCalendarView() {
        // Mettez à jour l'affichage du calendrier avec les événements actuels
//        displayEvents(this.events,currentMonth);
        if(filterOption.equals("Semaine")){
            currentSemaine = currentSemaine.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            filterEventsWeek(currentSemaine);
        }
        else if(filterOption.equals("Jour")){
            filterEventsDay(currentJour);
        }
        else if(filterOption.equals("Mois")) {
            filterEventsMonth(currentMonth.atDay(1), currentMonth.atEndOfMonth());         }

        }
        public void applyFilter(String filterOption) {
            switch (filterOption) {
                case "Jour" -> {
                    currentJour = LocalDate.now();
                    filterEventsDay(currentJour);
                }
                case "Semaine" -> {
                    currentSemaine= LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    displayEventsParSemaine(this.events,currentSemaine);
                }
                case "Mois" -> {
                    YearMonth currentMonth = YearMonth.now();
                    displayEventsParMois(this.events, currentMonth);
                }
                default ->
                    // Si aucune option de filtre n'est sélectionnée, afficher tous les événements
                        displayAllEvents();
            }
    }

    private void filterEventsDay(LocalDate now) {
            // Trie les événements par date de début
            List<VEvent> nowEvents = events.stream()
                    .filter(event -> event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(now))
                    .collect(Collectors.toList());
            displayEventsParJour(nowEvents,now);
            }

    private void filterEventsWeek(LocalDate currentSemaine) {
        // Trie les événements par date de début
        List<VEvent> weekEvents = events.stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(currentSemaine.minusDays(1)) && eventDate.isBefore(currentSemaine.plusDays(7));
                })
                .collect(Collectors.toList());
        displayEventsParSemaine(weekEvents,currentSemaine);
    }
    private void filterEventsMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        // Trie les événements par date de début
        List<VEvent> monthEvents = events.stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(startOfMonth.minusDays(0)) && eventDate.isBefore(endOfMonth.plusDays(0));
                })
                .collect(Collectors.toList());
        YearMonth currentMonth = YearMonth.from(startOfMonth);
        displayEventsParMois(monthEvents,currentMonth);
    }


        private void displayAllEvents() {
        // Affichez tous les événements
        YearMonth currentMonth = YearMonth.now();
        displayEventsParMois(this.events,currentMonth);
    }
    }
