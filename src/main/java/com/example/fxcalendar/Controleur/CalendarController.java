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
    @FXML
    public Button reserverSalle;
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
    private VBox mainContainer; 
    private boolean isLightTheme; 

    @FXML
    private ChoiceBox<String> viewChoiceBox;

    private CalendarView calendarView;

    private YearMonth currentMonth = YearMonth.now();
    private LocalDate currentSemaine = LocalDate.now();

    private LocalDate currentJour = LocalDate.now();
    @FXML

    private Button ThemeButton;
    private String textformation;

    @FXML
    private Text formationText;



    private String filterOption = "Mois";

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
            if(user.getRole().equalsIgnoreCase("etudiant")){
                reserverSalle.setVisible(false);
            }

            textformation = user.getFormation();
            String formation = textformation;
            isLightTheme = user.getTheme().equals("LIGHT");

            
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

            
            FormationSwitch.setOnAction(event -> handleFormationSelection());
            FormationFilter.setOnAction(event -> handleFormationFiltreSelection());
            FormationClaAlt.setOnAction(event -> handleFormationClaAltSelection());


        updateDateText();

        ICalendarReader calendarReader = new ICalendarReader();
        List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation+ " " + FormationClaAlt.getValue());
        calendarView = new CalendarView(events);
        calendarView.setCalendarController(this);
        calendarGrid.getChildren().add(calendarView.getCalendarGrid());

        viewChoiceBox.getItems().addAll("Jour", "Semaine", "Mois");
        viewChoiceBox.setValue("Semaine");
        setupNavigationButtons();
        updateCalendarView();

        }


    public void loadUserEvents(UserModel user) {
        List<EventModel> events = user.getEvents(); 
        String userFormation = user.getFormation();
        

        List<EventModel> filteredEvents = events.stream()
                .filter(event -> event.getFormation().equals(userFormation))
                .collect(Collectors.toList());

        

        for (EventModel event : filteredEvents) {
                calendarView.addEventToCalendarView(event);
        }
    }
    private void handleFormationClaAltSelection() {
        
        textformation = FormationSwitch.getValue() ;
        
        FormationFilter.setValue("--par matière--");
        updateDateText();
        textformation = FormationSwitch.getValue() ;
        ICalendarReader calendarReader = new ICalendarReader();
        List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation+ " " + FormationClaAlt.getValue());
        calendarView.setEvents(events);
        updateCalendarView();


    }

    private void handleFormationFiltreSelection() {

            textformation = FormationFilter.getValue();


            if (!textformation.equals("--par matière--")) {

                updateDateText();

                ICalendarReader calendarReader = new ICalendarReader();
                List<biweekly.component.VEvent> events = calendarReader.fetchAndParseCalendarData(textformation);
                calendarView.setEvents(events);
                updateCalendarView();
            }
        }

        private void handleFormationSelection() {

            textformation = FormationSwitch.getValue();

            boolean isFormation = textformation.equals("M1 IA") || textformation.equals("M1 SICOM") || textformation.equals("M1 ILSEN");
            FormationFilter.setDisable(!isFormation);
            FormationClaAlt.setDisable(!isFormation);
            if (!FormationClaAlt.isDisabled()) {
                FormationClaAlt.setValue("CLA");
            }
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
                    textformation = FormationSwitch.getValue();
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

        ThemeButton.setOnAction(this::toggleTheme);

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
    public void handleAddEventButton() {
        
        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un nouvel événement");
        dialog.setHeaderText("Saisissez les détails de l'événement");

        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Description de l'événement");
        DatePicker datePicker = new DatePicker(LocalDate.now()); 
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("Heure de début (HH:mm)");
        TextField endTimeField = new TextField();
        endTimeField.setPromptText("Heure de fin (HH:mm)");
        TextField locationField = new TextField();
        locationField.setPromptText("Lieu");
        ColorPicker colorPicker = new ColorPicker(Color.BLUE); 

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

        
        Platform.runLater(titleField::requestFocus);

        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(titleField.getText(), datePicker.getValue());
            }
            return null;
        });

        
        Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
        result.ifPresent(eventDetails -> {
            String description = eventDetails.getKey();
            LocalDate date = eventDetails.getValue();

            
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            
            LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);
            String location = locationField.getText();
            Color color = colorPicker.getValue();
            
            LocalDateTime eventStart = LocalDateTime.of(date, startTime);
            LocalDateTime eventEnd = LocalDateTime.of(date, endTime);

            
            Duration duration = Duration.between(startTime, endTime);
            
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            String durationStr = String.format("%dh %02dm", hours, minutes);


            if (user.isUserFree(user, eventStart, eventEnd,eventStart, eventEnd,date)) {
            
            VEvent vEvent = new VEvent();
            vEvent.setDescription(description);
            DateTime startDateTime = new DateTime(Date.from(date.atTime(startTime).atZone(ZoneId.systemDefault()).toInstant()));
            vEvent.setDateStart(new DateStart(startDateTime));

            DateTime endDateTime = new DateTime(Date.from(date.atTime(endTime).atZone(ZoneId.systemDefault()).toInstant()));
            vEvent.setDateEnd(new DateEnd(endDateTime));

            
            Date now = new Date(); 
            vEvent.setCreated(now);
            vEvent.setLastModified(now);

            vEvent.setLocation(location);
            vEvent.setColor(color.toString());

            
            EventModel eventModel = new EventModel(user.getUsername(),description, description+" Type : PERSONNEL", date.toString(), startTime.toString(), endTime.toString(), location, color.toString(), durationStr, user.getFormation());

            
            calendarView.addEvent(vEvent);
            this.user.addEvent(eventModel);
            
            if (textformation.equals(user.getFormation())) {
                updateCalendarView();
            }

            
            UserController userController = new UserController();
            userController.addEventToUser(this.user.getUsername(), vEvent);

            
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


    @FXML
    public void handleLogoutButton() {
        
        calendarView.clearEvents();
        
        UserController userController = new UserController();
        userController.updateUser(this.user);

        
        app.loadLoginView();

    }

    @FXML
    private void toggleTheme(ActionEvent actionEvent) {

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
        
        this.user.setTheme(isLightTheme ? "LIGHT" : "DARK");
        
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
            
            deleteEvent(eventModel);
        } else if (result.isPresent() && result.get() == modifyButton) {
            
            modifyEvent(eventModel);
        }
        
    }

    private void deleteEvent(EventModel eventModel) {
        
        this.user.getEvents().remove(eventModel);

        UserController userController = new UserController();
        userController.deleteEventFromUser(this.user.getUsername(), eventModel);


        
        updateCalendarView();
    }


    private void modifyEvent(EventModel eventModel) {
        
        Dialog<EventModel> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'événement");
        dialog.setHeaderText("Modifier les détails de l'événement");

        
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        
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
        LocalDateTime eventStartOld;
        LocalDateTime eventEndOld;
        eventStartOld = LocalDateTime.of(LocalDate.parse(eventModel.getDate()), LocalTime.parse(eventModel.getStartHour()));
        eventEndOld = LocalDateTime.of(LocalDate.parse(eventModel.getDate()), LocalTime.parse(eventModel.getEndHour()));

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

        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new EventModel(user.getUsername(), titleField.getText(), descriptionField.getText(), datePicker.getValue().toString(), startTimeField.getText(), endTimeField.getText(), locationField.getText(), colorPicker.getValue().toString(), "", eventModel.getFormation());
            }
            return null;
        });

        Optional<EventModel> result = dialog.showAndWait();

        
        result.ifPresent(newEventModel -> {
            LocalDate date = LocalDate.parse(newEventModel.getDate());
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);

            LocalDateTime eventStart = LocalDateTime.of(date, startTime);
            LocalDateTime eventEnd = LocalDateTime.of(date, endTime);
            if (user.isUserFree(user, eventStart, eventEnd,eventStartOld, eventEndOld,date)) {
                this.user.getEvents().remove(newEventModel);

                
                UserController userController = new UserController();
                userController.deleteEventFromUser(this.user.getUsername(), newEventModel);

                VEvent vEvent = new VEvent();
                vEvent.setDescription(newEventModel.getDescription());
                DateTime startDateTime = new DateTime(Date.from(date.atTime(startTime).atZone(ZoneId.systemDefault()).toInstant()));
                vEvent.setDateStart(new DateStart(startDateTime));

                DateTime endDateTime = new DateTime(Date.from(date.atTime(endTime).atZone(ZoneId.systemDefault()).toInstant()));
                vEvent.setDateEnd(new DateEnd(endDateTime));

                
                Date now = new Date(); 
                vEvent.setCreated(now);
                vEvent.setLastModified(now);

                vEvent.setLocation(newEventModel.getLocation());
                vEvent.setColor(newEventModel.getColor());

                
                calendarView.addEvent(vEvent);
                this.user.addEvent(newEventModel);
                
                updateCalendarView();


                
                userController.addEventToUser(this.user.getUsername(), newEventModel);

                
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


    @FXML
    public void handleReserverSalle() {
        
        RoomManager roomManager = new RoomManager();

        Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Réservation de Salle");
        dialog.setHeaderText("Saisissez les détails de la réservation");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Description de l'événement");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("Heure de début (HH:mm)");
        TextField endTimeField = new TextField();
        endTimeField.setPromptText("Heure de fin (HH:mm)");

        ComboBox<String> roomSelection = new ComboBox<>();
        roomSelection.setPromptText("Sélectionnez une salle");
        roomSelection.setItems(FXCollections.observableArrayList("STAT 1", "S2", "S3"));

        ColorPicker colorPicker = new ColorPicker(Color.BLUE);

        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Heure de début:"), 0, 2);
        grid.add(startTimeField, 1, 2);
        grid.add(new Label("Heure de fin:"), 0, 3);
        grid.add(endTimeField, 1, 3);
        grid.add(new Label("Salle:"), 0, 4);
        grid.add(roomSelection, 1, 4);
        grid.add(new Label("Couleur:"), 0, 5);
        grid.add(colorPicker, 1, 5);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(titleField::requestFocus);

        dialog.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? new Pair<>(titleField.getText(), datePicker.getValue()) : null);

        Optional<Pair<String, LocalDate>> result = dialog.showAndWait();
        result.ifPresent(eventDetails -> {
            String description = eventDetails.getKey();
            LocalDate date = eventDetails.getValue();
            String roomName = roomSelection.getValue();
            
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            
            LocalTime startTime = LocalTime.parse(startTimeField.getText(), timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeField.getText(), timeFormatter);
            Color color = colorPicker.getValue();
            
            LocalDateTime eventStart = LocalDateTime.of(date, startTime);
            LocalDateTime eventEnd = LocalDateTime.of(date, endTime);

            
            Duration duration = Duration.between(startTime, endTime);
            
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            String durationStr = String.format("%dh %02dm", hours, minutes);

            if (roomManager.isRoomAvailable(roomName, eventStart, eventEnd) && user.isUserFree(user, eventStart, eventEnd,eventStart, eventEnd,date)) {
                if (roomManager.bookRoom(roomName, eventStart, eventEnd, description)) {
                    
                    VEvent vEvent = new VEvent();
                    vEvent.setDescription(description);
                    DateTime startDateTime = new DateTime(Date.from(date.atTime(startTime).atZone(ZoneId.systemDefault()).toInstant()));
                    vEvent.setDateStart(new DateStart(startDateTime));

                    DateTime endDateTime = new DateTime(Date.from(date.atTime(endTime).atZone(ZoneId.systemDefault()).toInstant()));
                    vEvent.setDateEnd(new DateEnd(endDateTime));

                    
                    Date now = new Date(); 
                    vEvent.setCreated(now);
                    vEvent.setLastModified(now);

                    vEvent.setLocation(roomName);
                    vEvent.setColor(color.toString());

                    
                    EventModel eventModel = new EventModel(user.getUsername(),description, description+" Type : PERSONNEL", date.toString(), startTime.toString(), endTime.toString(), roomName, color.toString(), durationStr, user.getFormation());

                    
                    calendarView.addEvent(vEvent);
                    this.user.addEvent(eventModel);
                    
                    if (textformation.equals(user.getFormation())) {
                        updateCalendarView();
                    }

                    
                    UserController userController = new UserController();
                    userController.addEventToUser(this.user.getUsername(), vEvent);

                    
                    userController.updateUser(this.user);

                    
                } else {
                    
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de Réservation");
                    alert.setHeaderText("Impossible de réserver la salle");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Réservation Impossible");
                alert.setHeaderText("La salle sélectionnée n'est pas disponible ou le professeur n'est pas libre.");
                alert.setContentText("Veuillez choisir un autre créneau horaire, une autre salle, ou vérifier la disponibilité du professeur.");
                alert.showAndWait();
            }
        });
    }

    }
