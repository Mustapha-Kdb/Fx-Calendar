package com.example.fxcalendar;

import biweekly.component.VEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class CalendarView {

    private final GridPane calendarGrid;
    private YearMonth currentMonth = YearMonth.now();

    private List<VEvent> events = new ArrayList<>();; // Liste pour stocker les événements

    public CalendarView(List<VEvent> events) {
        calendarGrid = new GridPane();
        this.events = events;
        // Configuration initiale de GridPane, comme définir les espacements, etc.


    }

    public void displayEvents(List<VEvent> events,YearMonth currentMonth) {
        //vider le GridPane avant d'ajouter de nouveaux événements
        calendarGrid.getChildren().clear();

        List<VEvent> filteredSortedEvents = events.stream()
                .filter(e -> YearMonth.from(e.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).equals(currentMonth))
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
    public void setEvents(List<VEvent> events,YearMonth currentMonth) {
        this.events = events;
        // Vous pouvez également mettre à jour l'affichage ici pour montrer tous les événements dès qu'ils sont définis
        displayEvents(events,currentMonth);
    }


    public void setupNavigationButtons(Button prevMonthButton, Button nextMonthButton) {
        prevMonthButton.setOnAction(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendarView();
        });

        nextMonthButton.setOnAction(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendarView();
        });
    }
    void updateCalendarView() {
        // Mettez à jour l'affichage du calendrier avec les événements actuels
        displayEvents(this.events,currentMonth);
    }
        public void applyFilter(String filterOption) {
        switch (filterOption) {
            case "Aujourd'hui":
                filterEvents(LocalDate.now());
                break;
            case "Cette semaine":
                LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                filterEvents(startOfWeek, endOfWeek); // Ici vous passez deux arguments LocalDate
                break;
            case "Ce mois-ci":
                displayEvents(this.events,YearMonth.now());
                break;
            default:
                // Si aucune option de filtre n'est sélectionnée, afficher tous les événements
                displayAllEvents();
                break;
        }
    }

    private void filterEvents(LocalDate now) {
            // Trie les événements par date de début
            List<VEvent> nowEvents = events.stream()
                    .filter(event -> event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(now))
                    .collect(Collectors.toList());
        YearMonth currentMonth = YearMonth.from(now);
        displayEvents(nowEvents,currentMonth);

            }

    private void filterEvents(LocalDate startOfMonth, LocalDate endOfMonth) {
        // Trie les événements par date de début
        List<VEvent> monthEvents = events.stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(startOfMonth.minusDays(1)) && eventDate.isBefore(endOfMonth.plusDays(1));
                })
                .collect(Collectors.toList());
        YearMonth currentMonth = YearMonth.from(startOfMonth);
        displayEvents(monthEvents,currentMonth);

    }

    private void displayAllEvents() {
        // Affichez tous les événements
        YearMonth currentMonth = YearMonth.now();
        displayEvents(this.events,currentMonth);
    }
    }
