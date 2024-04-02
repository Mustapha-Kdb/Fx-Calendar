package com.example.fxcalendar.Controleur;

import biweekly.util.DayOfWeek;
import com.example.fxcalendar.CalendarApp;
import com.example.fxcalendar.ICalendarReader;
import com.example.fxcalendar.Vue.CalendarView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class CalendarController {
    private CalendarApp calendarApp;

    @FXML
    public ChoiceBox<String> FormationSwitch;
    @FXML
    public ChoiceBox<String> FormationFilter;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    @FXML
    private ChoiceBox<String> viewChoiceBox;

    private CalendarView calendarView;

    private YearMonth currentMonth = YearMonth.now();
    private LocalDate currentSemaine = LocalDate.now();

    private LocalDate currentJour = LocalDate.now();


    private String textformation = "M1 ILSEN";

    @FXML
    private Text formationText;



    private static String filterOption = "Mois";

    public ChoiceBox<String> getViewChoiceBox() {
        return viewChoiceBox;
    }

    public void setViewChoiceBox(String mode) {
        this.viewChoiceBox.setValue(mode);
    }

    public YearMonth getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(YearMonth currentMonth) {
        this.currentMonth = currentMonth;
    }

    public LocalDate getCurrentSemaine() {
        return currentSemaine;
    }

    public void setCurrentSemaine(LocalDate currentSemaine) {
        this.currentSemaine = currentSemaine;
    }

    public LocalDate getCurrentJour() {
        return currentJour;
    }

    public void setCurrentJour(LocalDate currentJour) {
        this.currentJour = currentJour;
    }

    public void setCalendarApp(CalendarApp calendarApp) {
        this.calendarApp = calendarApp;
    }
        public void initialize() {
            // Initialiser la liste des suggestions de formations
            ObservableList<String> formations = FXCollections.observableArrayList(
                    "--par formation--" , "M1 IA", "M1 ILSEN", "M1 SICOM",
                    "--par salle--", "STAT 1", "S2", "S3" ,
                    "--par prof--","NOE"
            );
            ObservableList<String> filtresIA = FXCollections.observableArrayList(
                    "--par matière--", "approches neuronales", "prototypage", "test"
                    );
            ObservableList<String> filtresSICOM = FXCollections.observableArrayList(
                    "--par matière--", "test"
                    );
            ObservableList<String> filtresILSEN = FXCollections.observableArrayList(
                    "--par matière--", "prototypage", "test"
                    );
            FormationSwitch.setItems(formations);
            FormationSwitch.setValue("--par formation--");
            FormationFilter.setItems(filtresILSEN);
            FormationFilter.setValue("--par matière--");

            // Définir un écouteur pour la sélection d'une formation dans la ComboBox
            FormationSwitch.setOnAction(event -> handleFormationSelection(filtresIA, filtresSICOM, filtresILSEN));
            FormationFilter.setOnAction(event -> handleFormationFiltreSelection());

            updateDateText();

            ICalendarReader calendarReader = new ICalendarReader();

            List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation);
            calendarView = new CalendarView(events);
            calendarView.setCalendarController(this);
            calendarGrid.getChildren().add(calendarView.getCalendarGrid());
            viewChoiceBox.getItems().addAll("Jour", "Semaine", "Mois");
            viewChoiceBox.setValue("Semaine");
            setupNavigationButtons();
            updateCalendarView();
        }

        private void handleFormationFiltreSelection() {
            // Mettre à jour la valeur de textformation avec la formation sélectionnée
            textformation = FormationFilter.getValue();

            // Vérifier si une formation valide est sélectionnée
            if (!textformation.equals("--par matière--")) {

                updateDateText();

                ICalendarReader calendarReader = new ICalendarReader();
                List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation);
                calendarView.setEvents(events);
                updateCalendarView();
            }
        }

        private void handleFormationSelection(ObservableList<String> filtresIA, ObservableList<String> filtresSICOM, ObservableList<String> filtresILSEN) {
            // Mettre à jour la valeur de textformation avec la formation sélectionnée
            textformation = FormationSwitch.getValue();
            // afficher le choix de filtre dans le log console
            System.out.println("Formation : " + textformation);

            // Vérifier si une formation valide est sélectionnée
            if (!textformation.equals("--par formation--") &&
                    !textformation.equals("--par prof--") &&
                    !textformation.equals("--par salle--")
            ) {
                FormationFilter.setDisable(!textformation.equals("M1 IA") && !textformation.equals("M1 ILSEN") && !textformation.equals("M1 SICOM"));
                updateDateText();
//                FormationFilter.setItems(textformation.equals("M1 IA") ? filtresIA : textformation.equals("M1 SICOM") ? filtresSICOM : textformation.equals("M1 ILSEN") ? filtresILSEN : filtresILSEN);
//                FormationFilter.setValue("--par matière--");

                ICalendarReader calendarReader = new ICalendarReader();
                System.out.println("Formation : " + textformation);
                List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation);
                calendarView.setEvents(events);

                updateCalendarView();
            }
        }


    private void updateDateText() {
        // Mettre à jour le texte avec la valeur de currentJour
        formationText.setText("Formation : " + textformation);

    }

    public String getFilterOption() {
        return this.filterOption;
    }

    public void setFilterOption(String filterOption) {
        this.filterOption = filterOption;
    }

    private void setupNavigationButtons() {
        previousButton.setOnAction(this::handlePreviousButton);
        nextButton.setOnAction(this::handleNextButton);
    }

    @FXML
    private void handlePreviousButton(ActionEvent actionEvent) {
        switch (filterOption) {
            case "Mois":
                currentMonth = currentMonth.minusMonths(1);
                calendarView.filterEventsMonth(currentMonth);
                break;
            case "Semaine":
                currentSemaine = currentSemaine.minusWeeks(1);
                calendarView.filterEventsWeek(currentSemaine);
                break;
            case "Jour":
                if (currentJour.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                    currentJour = currentJour.minusDays(3);
                } else {
                    currentJour = currentJour.minusDays(1);
                }

                calendarView.filterEventsDay(currentJour);
                break;
        }
    }

    @FXML
    private void handleNextButton(ActionEvent actionEvent) {
        switch (getFilterOption()) {
            case "Mois":
                currentMonth = currentMonth.plusMonths(1);
                break;
            case "Semaine":
                currentSemaine = currentSemaine.plusWeeks(1);
                break;
            case "Jour":
                currentJour = currentJour.plusDays(1);

                break;
        }
        updateCalendarView();
    }

    void updateCalendarView() {
        // Mettez à jour l'affichage du calendrier avec les événements actuels
        if(filterOption.equals("Semaine")){
            currentSemaine = currentSemaine.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            calendarView.filterEventsWeek(currentSemaine);
        }
        else if(filterOption.equals("Jour")){
            calendarView.filterEventsDay(currentJour);
        }
        else if(filterOption.equals("Mois")) {
            calendarView.filterEventsMonth(currentMonth);         }

    }

    public void applyFilter() {
        switch (getFilterOption()) {
            case "Jour" -> {
                calendarView.filterEventsDay(currentJour);
            }
            case "Semaine" -> {
                calendarView.displayEventsParSemaine(calendarView.getEvents(), currentSemaine);
            }
            case "Mois" -> {
                calendarView.displayEventsParMois(calendarView.getEvents(), currentMonth);
            }
            default -> calendarView.displayAllEvents();
        }
    }

    @FXML
    public void handleChangeView(ActionEvent actionEvent) {
        this.setFilterOption (((ChoiceBox<?>) actionEvent.getSource()).getValue().toString());
    applyFilter();
    }

    public void handleAjourdhuiButton(ActionEvent actionEvent) {
        if (filterOption.equals("Jour")) {
            currentJour = LocalDate.now();
        } else if (filterOption.equals("Semaine")) {
            currentSemaine = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        } else if (filterOption.equals("Mois")) {
            currentMonth = YearMonth.now();
        }
        applyFilter();
    }

    public void handleAddEventButton(ActionEvent actionEvent) {

    }

    public void handleSearchEventButton(ActionEvent actionEvent) {
    }

    public void handlePlanningButton(ActionEvent actionEvent) {
    }

    public void handleLogoutButton(ActionEvent actionEvent) {
    }

    public void handleExitButton(ActionEvent actionEvent) {
    }

    public void handleHelpButton(ActionEvent actionEvent) {
    }

    public void handleAboutButton(ActionEvent actionEvent) {
    }

    @FXML
    public void toggleTheme(ActionEvent actionEvent) {
        if (calendarApp != null) {
            calendarApp.toggleTheme();
        }
    }

}
