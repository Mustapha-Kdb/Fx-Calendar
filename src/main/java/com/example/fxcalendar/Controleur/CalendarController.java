package com.example.fxcalendar.Controleur;

import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.util.DayOfWeek;
import com.example.fxcalendar.CalendarApp;
import com.example.fxcalendar.ICalendarReader;
import com.example.fxcalendar.Modele.EventModel;
import com.example.fxcalendar.Modele.UserModel;
import com.example.fxcalendar.Vue.CalendarView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CalendarController {
    private CalendarApp app;

    private UserModel user;
    @FXML
    public ChoiceBox<String> FormationClaAlt;
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
    private VBox mainContainer; // Assurez-vous que l'ID correspond à celui de votre FXML
    private boolean isLightTheme; // Un indicateur pour suivre le thème actuel

    @FXML
    private ChoiceBox<String> viewChoiceBox;

    private CalendarView calendarView;

    private YearMonth currentMonth = YearMonth.now();
    private LocalDate currentSemaine = LocalDate.now();

    private LocalDate currentJour = LocalDate.now();


    private String textformation;

    @FXML
    private Text formationText;



    private static String filterOption = "Mois";

    public ChoiceBox<String> getViewChoiceBox() {
        return viewChoiceBox;
    }

    public void setViewChoiceBox(String mode) {
        this.viewChoiceBox.setValue(mode);
    }
    public void setApp(CalendarApp app) {
        this.app = app;
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

    public String getTextformation() {
        return textformation;
    }

    public void setTextformation(String textformation) {
        this.textformation = textformation;
    }

    public void setCurrentJour(LocalDate currentJour) {
        this.currentJour = currentJour;
    }



        public void initialize(UserModel user) {
            this.user = user;
            textformation = user.getFormation();
            String formation = textformation;
            isLightTheme = user.getTheme().equals("LIGHT");
            // Initialiser la liste des suggestions de formations
            ObservableList<String> filters = FXCollections.observableArrayList(
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
            ObservableList<String> filterMatiereSelonUser = (formation.equals("M1 IA")) ? filtresIA : (formation.equals("M1 SICOM")) ? filtresSICOM : filtresILSEN;
            ObservableList<String> ClaAlt = FXCollections.observableArrayList(
                    "CLA", "ALT"
            );

            FormationSwitch.setItems(filters);
            FormationSwitch.setValue(formation);
            FormationFilter.setItems(filterMatiereSelonUser);
            FormationFilter.setValue("--par matière--");
            FormationClaAlt.setItems(ClaAlt);
            FormationClaAlt.setValue("CLA");

            // Définir un écouteur pour la sélection d'une formation dans la ComboBox
            FormationSwitch.setOnAction(event -> handleFormationSelection(filtresIA, filtresSICOM, filtresILSEN));
            FormationFilter.setOnAction(event -> handleFormationFiltreSelection());
            FormationClaAlt.setOnAction(event -> handleFormationClaAltSelection());


        updateDateText();

        ICalendarReader calendarReader = new ICalendarReader();
        List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation+ " " + FormationClaAlt.getValue());
        System.out.println(textformation);
        calendarView = new CalendarView(events);
        calendarView.setCalendarController(this);
        calendarGrid.getChildren().add(calendarView.getCalendarGrid());
//      calendarView.adjustGridPaneWidth(calendarScrollPane)
        viewChoiceBox.getItems().addAll("Jour", "Semaine", "Mois");
        viewChoiceBox.setValue("Semaine");
        setupNavigationButtons();
        updateCalendarView();

        }


    public void loadUserEvents(UserModel user) {
        List<EventModel> events = user.getEvents(); // Obtenez les événements de l'utilisateur
        String userFormation = user.getFormation();
        //print les événements de l'utilisateur
        System.out.println("Les événements de l'utilisateur " + user.getUsername() + " sont : ");
        for (EventModel event : events) {
            System.out.println(event.getDescription());
        }
        List<EventModel> filteredEvents = events.stream()
                .filter(event -> event.getFormation().equals(userFormation))
                .collect(Collectors.toList());

        //print les événements à afficher
        System.out.println("Les événements à afficher pour l'utilisateur " + user.getUsername() + " sont : ");
        for (EventModel event : filteredEvents) {
            System.out.println(event.getDescription());
        }

        for (EventModel event : filteredEvents) {
            // Ici, vous devriez intégrer chaque événement dans votre vue de calendrier
                calendarView.addEventToCalendarView(event);
        }
    }





    private void handleFormationClaAltSelection() {
        // Mettre à jour la valeur de textformation avec la formation sélectionnée concaténée avec Cla ou Alt
        textformation = FormationSwitch.getValue() ;
        // afficher le choix de filtre dans le log console
        System.out.println("Formation : " + textformation);
        FormationFilter.setValue("--par matière--");

        ICalendarReader calendarReader = new ICalendarReader();
        List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation+ " " + FormationClaAlt.getValue());
        calendarView.setEvents(events);
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

            System.out.println("Formation : " + textformation);
            boolean isFormation = textformation.equals("M1 IA") || textformation.equals("M1 SICOM") || textformation.equals("M1 ILSEN");
            FormationFilter.setDisable(!isFormation);
            FormationClaAlt.setDisable(!isFormation);
            if (!FormationClaAlt.isDisabled()) {
                FormationClaAlt.setValue("CLA");
            }
            // afficher le choix de filtre dans le log console

            // Vérifier si une formation valide est sélectionnée
            if (!textformation.equals("--par formation--") &&
                    !textformation.equals("--par prof--") &&
                    !textformation.equals("--par salle--")
            ) {
                updateDateText();

                ICalendarReader calendarReader = new ICalendarReader();
                List<biweekly.component.VEvent> events;
                if (!FormationFilter.isDisabled()) {
                    FormationFilter.setValue("--par matière--");
                    FormationClaAlt.setValue("CLA");
                    //todo filtrer une matière par formation : IA, SICOM, ILSEN par exemple pour IA : approches neuronales, prototypage, test
                    events = calendarReader.fetchAndParseCalendarData(textformation+ " " + FormationClaAlt.getValue());

                }
                else {
                    FormationClaAlt.setDisable(true);
                    events = calendarReader.fetchAndParseCalendarData(textformation);
                }
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
    @FXML
    public void handleAddEventButton(ActionEvent actionEvent) {
        // Créer le dialogue
        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un nouvel événement");
        dialog.setHeaderText("Saisissez les détails de l'événement");

        // Ajouter des boutons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Créer les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Description de l'événement");
        DatePicker datePicker = new DatePicker(LocalDate.now()); // La date par défaut est aujourd'hui
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("Heure de début (HH:mm)");
        TextField endTimeField = new TextField();
        endTimeField.setPromptText("Heure de fin (HH:mm)");
        TextField locationField = new TextField();
        locationField.setPromptText("Lieu");
        ColorPicker colorPicker = new ColorPicker(Color.BLUE); // Couleur par défaut

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Heure de début:"), 0, 2);
        grid.add(startTimeField, 1, 2);
        grid.add(new Label("Heure de fin:"), 0, 3);
        grid.add(endTimeField, 1, 3);
        grid.add(new Label("Lieu:"), 0, 4);
        grid.add(locationField, 1, 4);
        grid.add(new Label("Couleur:"), 0, 5);
        grid.add(colorPicker, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Demande de mise au point initiale sur le champ du titre
        Platform.runLater(() -> titleField.requestFocus());

        // Convertir les résultats lorsque le bouton OK est cliqué.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(titleField.getText(), datePicker.getValue());
            }
            return null;
        });

        // Afficher le dialogue et attendre la réponse de l'utilisateur
        Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
        result.ifPresent(eventDetails -> {
            String description = eventDetails.getKey();
            LocalDate date = eventDetails.getValue();

            // Définir un formateur pour les heures au format "H:mm"
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            // Utiliser le formateur pour parser les heures
            LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);
            String location = locationField.getText();
            Color color = colorPicker.getValue();
            // Convertir la date et les heures en LocalDateTime
            LocalDateTime eventStart = LocalDateTime.of(date, startTime);
            LocalDateTime eventEnd = LocalDateTime.of(date, endTime);

            // Calculer la durée
            Duration duration = Duration.between(startTime, endTime);
            // Convertir en un format de durée lisible, par exemple, heures et minutes
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            String durationStr = String.format("%dh %02dm", hours, minutes);

            System.out.println("Description: " + description + ", Date: " + date + ", Début: " + startTime + ", Fin: " + endTime + ", Lieu: " + location + ", Couleur: " + color + ", Durée: " + durationStr);
            // Vérifier si l'utilisateur est libre
            if (user.isUserFree(user, eventStart, eventEnd,date)) {
            // Création de l'événement VEvent
            VEvent vEvent = new VEvent();
            vEvent.setDescription(description);
            DateTime startDateTime = new DateTime(Date.from(date.atTime(startTime).atZone(ZoneId.systemDefault()).toInstant()));
            vEvent.setDateStart(new DateStart(startDateTime));

            DateTime endDateTime = new DateTime(Date.from(date.atTime(endTime).atZone(ZoneId.systemDefault()).toInstant()));
            vEvent.setDateEnd(new DateEnd(endDateTime));

            // Configurer la propriété 'Created' et 'LastModified'
            Date now = new Date(); // La date actuelle
            vEvent.setCreated(now);
            vEvent.setLastModified(now);

            vEvent.setLocation(location);
            vEvent.setColor(color.toString());

            // Création de l'objet EventModel avec la durée calculée
            EventModel eventModel = new EventModel(user.getUsername(),description, description+" Type : PERSONNEL", date.toString(), startTime.toString(), endTime.toString(), location, color.toString(), durationStr, user.getFormation());

            // Ajout de l'événement à la vue de calendrier
            calendarView.addEvent(vEvent);
            this.user.addEvent(eventModel);
            updateCalendarView();

            // Mettez à jour le fichier JSON pour enregistrer les modifications
            UserController userController = new UserController();
            userController.addEventToUser(this.user.getUsername(), vEvent);

            // Enregistrer les modifications dans le fichier users.json
            userController.updateUser(this.user);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("l'"+user.getRole()+" "+user.getUsername()+" n'est pas disponible");
                alert.setContentText("vous avez déjà un événement prévu pour cette période. Veuillez choisir une autre date ou heure.");
                alert.showAndWait();
            }
        });
    }



    public void handleLogoutButton(ActionEvent actionEvent) {
        //retourner à la page de connexion
        app.loadLoginView();

    }


    @FXML
    private void toggleTheme() {
        String cssFile = !isLightTheme ? "/com/example/fxcalendar/styles/light-theme.css" : "/com/example/fxcalendar/styles/dark-theme.css";
        URL resourceUrl = this.getClass().getResource(cssFile);
        if (resourceUrl == null) {
            System.err.println("Le fichier CSS n'a pas été trouvé: " + cssFile);
            return;
        }
        String resource = resourceUrl.toExternalForm();
        mainContainer.getStylesheets().clear();
        mainContainer.getStylesheets().add(resource);
        isLightTheme = !isLightTheme;
        // Mettre à jour le thème dans le fichier de configuration JSon users.json
        this.user.setTheme(isLightTheme ? "LIGHT" : "DARK");
        // Enregistrer les modifications dans le fichier users.json
        UserController userController = new UserController();
        userController.updateUser(user);

    }


    public UserModel getUser() {
        return user;
    }
    public void showEventOptionsDialog(EventModel eventModel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Options d'événement");
        alert.setHeaderText("Choisissez une action pour cet événement");
        alert.setContentText("Description: " + eventModel.getDescription());

        ButtonType deleteButton = new ButtonType("Supprimer");
        ButtonType modifyButton = new ButtonType("Modifier");
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton, modifyButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == deleteButton) {
            // Logique pour supprimer l'événement
            deleteEvent(eventModel);
        } else if (result.isPresent() && result.get() == modifyButton) {
            // Logique pour modifier l'événement
            modifyEvent(eventModel);
        }
        // Pas besoin de traiter le cas d'annulation explicitement
    }

    private void deleteEvent(EventModel eventModel) {
        // Exemple : Supprimer l'événement de la liste des événements de l'utilisateur
        this.user.getEvents().remove(eventModel);

        // Mettre à jour le fichier JSON ou la base de données, si nécessaire
        UserController userController = new UserController();
        userController.deleteEventFromUser(this.user.getUsername(), eventModel);


        // Rafraîchir l'affichage du calendrier pour refléter la suppression
        updateCalendarView();
    }


    private void modifyEvent(EventModel eventModel) {
        // Créer le dialogue de modification
        Dialog<EventModel> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'événement");
        dialog.setHeaderText("Modifier les détails de l'événement");

        // Configurer les boutons du dialogue
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Ajouter des champs pour modifier les détails de l'événement, pré-remplis avec les valeurs actuelles
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(eventModel.getTitle());
        TextField descriptionField = new TextField(eventModel.getDescription());
        DatePicker datePicker = new DatePicker(LocalDate.parse(eventModel.getDate()));
        TextField startTimeField = new TextField(eventModel.getStartHour());
        TextField endTimeField = new TextField(eventModel.getEndHour());
        TextField locationField = new TextField(eventModel.getLocation());
        ColorPicker colorPicker = new ColorPicker(Color.valueOf(eventModel.getColor()));

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Heure de début:"), 0, 3);
        grid.add(startTimeField, 1, 3);
        grid.add(new Label("Heure de fin:"), 0, 4);
        grid.add(endTimeField, 1, 4);
        grid.add(new Label("Lieu:"), 0, 5);
        grid.add(locationField, 1, 5);
        grid.add(new Label("Couleur:"), 0, 6);
        grid.add(colorPicker, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en EventModel quand l'utilisateur clique sur OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new EventModel(user.getUsername(), titleField.getText(), descriptionField.getText(), datePicker.getValue().toString(), startTimeField.getText(), endTimeField.getText(), locationField.getText(), colorPicker.getValue().toString(), "", eventModel.getFormation());
            }
            return null;
        });

        Optional<EventModel> result = dialog.showAndWait();

        // Mise à jour de l'événement avec les nouvelles valeurs saisies par l'utilisateur
        result.ifPresent(newEventModel -> {
            UserController userController = new UserController();
            userController.updateEventForUser(this.user.getUsername(), newEventModel);
            // Mettre à jour l'affichage du calendrier
            updateCalendarView();
        });
    }


}
