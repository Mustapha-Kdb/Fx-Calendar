package com.example.fxcalendar.Vue;

import biweekly.component.VEvent;
import com.example.fxcalendar.Controleur.CalendarController;
import com.example.fxcalendar.Modele.EventModel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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



    
    public void displayEventsParMois(List<biweekly.component.VEvent> events, YearMonth Month) {
        
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); 
        calendarGrid.getRowConstraints().clear(); 

        
        int totalDaysInMonth = Month.lengthOfMonth();

        
        int numColumns = 5; 
        int numRows = (totalDaysInMonth + numColumns - 1) / numColumns; 

        
        int columnIndex = 0;
        int rowIndex = 1;


        
        
        Map<LocalDate, Integer> sessionCounts = new HashMap<>();
        for (VEvent event : events) {
            LocalDate date = LocalDate.ofInstant(event.getDateStart().getValue().toInstant(), ZoneId.systemDefault());
            if (date.getMonth() == Month.getMonth() && date.getYear() == Month.getYear()) {
                sessionCounts.put(date, sessionCounts.getOrDefault(date, 0) + 1);
            }
        }



        String[] joursDeLaSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        RowConstraints headerRowConstraints = new RowConstraints(40,40,40); 
        calendarGrid.getRowConstraints().add(headerRowConstraints);
        calendarGrid.setStyle("-fx-border-width: 1px; -fx-border-color: black; -fx-border-style: solid; -fx-border-radius: 5px; -fx-padding: 5px; -fx-background-color: #f4f4f4;");

        for (int i = 0; i < 5; i++) { 
            
            Label dayLabel = new Label(joursDeLaSemaine[i]);
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 
            dayLabel.setAlignment(Pos.CENTER); 
            
            

            
            VBox dayHeader = new VBox(dayLabel);
            dayHeader.setAlignment(Pos.CENTER); 

            
            dayHeader.getStyleClass().add("grid-cell");
            calendarGrid.add(dayHeader, i, 0);
        }




        
        for (int i = 0; i < totalDaysInMonth; i++) {
            
            LocalDate day = LocalDate.of(Month.getYear(), Month.getMonthValue(), i + 1);
            if (day.getDayOfWeek().getValue() == 6 || day.getDayOfWeek().getValue() == 7) {
                continue; 
            }


            int dayOfMonth = i + 1;
            String emoji = "\uD83D\uDCC6"; 


            String labelText = "Le " + dayOfMonth + ", " + Month.getMonth().toString() + " " + emoji;


            Label dayLabel = new Label(labelText); 
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

            
            LocalDate currentDate = LocalDate.of(Month.getYear(), Month.getMonthValue(), dayOfMonth);
            int sessionCount = sessionCounts.getOrDefault(currentDate, 0);
            String emoji2 = "\uD83D\uDCFA"; 


            String labelText2 = sessionCount + " séance" + (sessionCount > 1 ? "s " : " ") + emoji2;


            Label sessionCountLabel = new Label(labelText2);
            
            VBox dayVBox = new VBox();
            dayVBox.setAlignment(Pos.CENTER); 
            dayVBox.getChildren().addAll(dayLabel, sessionCountLabel);

            
            dayVBox.setStyle("-fx-background-color: #ee974b; " +
                    "-fx-border-color: #000000; " +
                    "-fx-border-width: 0.3px; " +
                    "-fx-background-radius: 7px; " +
                    "-fx-border-radius: 7px;");




            
            GridPane.setMargin(dayVBox, new Insets(3));

            




            
            int finalI = i;
            dayVBox.setOnMouseEntered(e -> {
                
                
                
                String sessionDetails = getSessionDetailsForDay(finalI + 1, Month);
                Tooltip tooltip = new Tooltip(sessionDetails);
                Tooltip.install(dayVBox, tooltip);
            });

            
            int finalI1 = i;
            dayVBox.setOnMouseClicked(e -> {
                
                
                
                LocalDate clickedDate = LocalDate.of(Month.getYear(), Month.getMonthValue(), finalI1 + 1);
                calendarController.setFilterOption("Semaine");
                calendarController.setViewChoiceBox("Semaine");
                
                LocalDate currentSemaine = clickedDate.minusDays(clickedDate.getDayOfWeek().getValue() - 1);
                calendarController.setCurrentSemaine(currentSemaine);
                calendarController.applyFilter();
            });

            
            calendarGrid.add(dayVBox, columnIndex, rowIndex);

            
            columnIndex++;
            if (columnIndex == numColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        
        for (int i = 0; i < numColumns; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(250, 250, 250); 
            columnConstraints.setHgrow(Priority.ALWAYS); 
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < numRows; i++) {

            RowConstraints rowConstraints = new RowConstraints(100, 100, 100);
            rowConstraints.setVgrow(Priority.ALWAYS); 
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
    }
    private VBox createStyledCell(Color color) {
        VBox cell = new VBox();
        cell.getStyleClass().add("grid-cell"); 
        cell.setBackground(Background.fill(color));
        return cell;
    }
    public String getSessionDetailsForDay(int dayOfMonth, YearMonth Month) {
        

        return "Détails pour le " + dayOfMonth + ", " +  Month.getMonth().toString();
    }


    
    public void displayEventsParSemaine(List<biweekly.component.VEvent> events, LocalDate semaine) {
        
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); 
        calendarGrid.getRowConstraints().clear(); 


        
        ColumnConstraints hourColumnConstraints = new ColumnConstraints(80, 80, 80); 
        calendarGrid.getColumnConstraints().add(hourColumnConstraints);

        
        for (int i = 0; i < 5; i++) {
            ColumnConstraints dayColumnConstraints = new ColumnConstraints(250, 250, 250); 
            dayColumnConstraints.setHgrow(Priority.ALWAYS); 
            calendarGrid.getColumnConstraints().add(dayColumnConstraints);
        }
        
        for (int i = 0; i < (19 - 8) * 2 + 3; i++) { 
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(20); 
            rowConstraints.setPercentHeight(100.0 / 25);
            rowConstraints.setValignment(VPos.CENTER); 
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
        
        for (int i = 0; i < 5; i++) { 
            LocalDate dayDate = semaine.plusDays(i);
            String dayLabel = dayDate.format(DateTimeFormatter.ofPattern("EEE dd/MM", Locale.FRANCE));

            Text dayText = new Text(dayLabel);
            dayText.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 

            VBox dayHeader = new VBox(dayText);
            dayHeader.setAlignment(Pos.CENTER);
            dayHeader.getStyleClass().add("grid-cell");


            calendarGrid.add(dayHeader, i + 1, 0); 
        }
        
        int totalHalfHourSlots = (19 - 8) * 2 + 2;

        
        Color color=Color.LIGHTGRAY;

        for (int i = 8; i <= 19; i++) {
            
            Text hourLabel1 = new Text(String.format("%02d:00", i));
            Text hourLabel2 = new Text(String.format("%02d:30", i));
            VBox hourLabels1 = new VBox(hourLabel1);
            VBox hourLabels2 = new VBox(hourLabel2);
            hourLabels1.setBackground(Background.fill(color));
            hourLabels2.setBackground(Background.fill(color));
            hourLabels1.setAlignment(Pos.CENTER);
            
            hourLabels1.setStyle("-fx-border-color: black; -fx-border-width: 0.1px; ");
            hourLabels2.setStyle("-fx-border-color: black; -fx-border-width: 0.1px;");

            
            hourLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            hourLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            
            hourLabels2.setAlignment(Pos.CENTER);
            calendarGrid.add(hourLabels1, 0, (i - 8) * 2 + 1);
            calendarGrid.add(hourLabels2, 0, (i - 8) * 2 + 2);
            color=(color == Color.LIGHTGRAY) ? Color.WHITE: (Color.LIGHTGRAY);
        }
        color = Color.WHITE;
        for (int i = 1; i <= totalHalfHourSlots; i++) {
            if (i % 2 == 1) { 
                color = (color.equals(Color.LIGHTGRAY)) ? Color.WHITE : Color.LIGHTGRAY;
            }

            for (int j = 1; j < 6; j++) {
                VBox cell = createStyledCell(color);
                calendarGrid.add(cell, j, i);
            }
        }
        
        for (int i = 0; i < 5; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / 5);
            columnConstraints.setHalignment(HPos.CENTER); 
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }
        events.sort(Comparator.comparing(event -> event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (eventDate.isAfter(semaine.minusDays(1)) && eventDate.isBefore(semaine.plusDays(5))) {
                int columnIndex = (int) ChronoUnit.DAYS.between(semaine, eventDate) + 1;
                int startHour = Math.max(startTime.getHour(), 8);
                int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0)+1;
                startRowIndex = Math.max(startRowIndex, 1);
                int endHour = Math.min(endTime.getHour(), 19); 
                int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() == 30 ? 1 : 0);
                endRowIndex = Math.min(endRowIndex, 23);
                int durationInHalfHours = endRowIndex - startRowIndex + 1;
                durationInHalfHours = Math.max(durationInHalfHours, 1); 
                if (durationInHalfHours == 1) {
                    durationInHalfHours = totalHalfHourSlots-2;
                }
                String description = event.getDescription() != null ? event.getDescription().getValue() : "Jour Férié";
                String type = getTypesFromDescription(description);
                // Tronquer le texte si nécessaire
                final int MAX_CHAR_COUNT = 35*durationInHalfHours; // Nombre maximal de caractères à afficher
                String displayText = description.length() > MAX_CHAR_COUNT ? description.substring(0, MAX_CHAR_COUNT) + "..." : description;

                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200);
                eventText.setStyle("-fx-font-size: 10;");

                VBox eventBox = new VBox(eventText);
                String eventBoxStyle = stylishEventBox(type);
                eventBox.setStyle(eventBoxStyle);
                eventBox.setPadding(new Insets(5));
                String tooltipText = String.format("Description: %s", description  + "Heure de debut: " + event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime() + "\n" + "Heure de fin: " + event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(eventBox, tooltip);
                tooltip.setShowDuration(Duration.INDEFINITE);
                tooltip.setShowDelay(Duration.millis(100));
                List<String[]> enseignants = getNomsEnseignantsFromDescription(description);

                StringBuilder noms = new StringBuilder();
                StringBuilder emails = new StringBuilder();
                for (String[] enseignant : enseignants) {
                    if (noms.length() > 0) noms.append(", ");
                    noms.append(enseignant[0]);
                    if (emails.length() > 0) emails.append(",");
                    emails.append(enseignant[1]);
                }
                String enseignantEmails = emails.toString();
                String subject = "Sujet de l'email";
                String body = "Corps de l'email. Rendez-vous concernant: " ;
                if (!enseignants.isEmpty() && !"Inconnu".equals(enseignants.get(0)[0]))
                    eventBox.setOnMouseClicked(mouseEvent -> {
                    try {
                        String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8);
                        String encodedBody = URLEncoder.encode(body, StandardCharsets.UTF_8);
                        String mailto = "mailto:" + enseignantEmails + "?subject=" + encodedSubject + "&body=" + encodedBody;
                        java.awt.Desktop.getDesktop().mail(new URI(mailto));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    });

                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, durationInHalfHours);
                GridPane.setMargin(eventBox, new Insets(2));
                VBox.setMargin(eventBox, new Insets(2));
                eventBox.setMinHeight(durationInHalfHours * 20); // Exemple d'ajustement basé sur la durée
                calendarGrid.add(eventBox, columnIndex, startRowIndex,1, durationInHalfHours);
            }
        }
        if(calendarController.getUser().getFormation().equals(calendarController.getTextformation()))
            calendarController.loadUserEvents(calendarController.getUser());
    }
    private String stylishEventBox(String type) {
        String color = "#ee974b"; 
        
        String[] parts = type.split("/");
        type = parts[0];
        
        String color1 = parts.length > 1 ? convertHexToCssColor(parts[1]) : "#f44336";
        switch (type.toLowerCase()) {
            case "cours":
                color = "#f4a261"; 
                break;
            case "td":
                color = "#08C5D1"; 
                break;
            case "tp":
                color = "#08C5D1"; 
                break;
                case "cm/td":
                color = "#08C5D1"; 
                break;
            case "evaluation":
                color = "#CF4146"; 
                break;
            case "personnel":
                color = color1; 
                break;
            default:
                color = "#f4a261"; 
                break;
        }



        return "-fx-background-color: " + color + "; -fx-border-color: #000000; -fx-border-width: 0.3px; -fx-background-radius: 7px; -fx-border-radius: 7px;";
    }

    
    private String convertHexToCssColor(String hex) {
        
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }

        
        while (hex.length() < 7) {
            hex = hex.substring(0, 1) + "0" + hex.substring(1);
        }

        return hex;
    }

    private String getTypesFromDescription(String description) {
        
        Pattern pattern = Pattern.compile("Type\\s*:\\s*(.*)");
        Matcher matcher = pattern.matcher(description);

        
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "Type inconnu";
    }

    private List<String[]>  getNomsEnseignantsFromDescription(String description) {
        
        List<String[]>  enseignants = new ArrayList<>();

        
        Pattern pattern = Pattern.compile("Enseignants?\\s*:\\s*(.*)");
        Matcher matcher = pattern.matcher(description);

        
        if (matcher.find()) {
            
            String[] noms = matcher.group(1).trim().split(",");

            
            for (String nom : noms) {
                String nomEmail = nom.trim().toLowerCase().replaceAll("\\s+", "."); 
                enseignants.add(new String[]{nom, nomEmail + "@univ-avignon.fr"});
            }
        }

        if (enseignants.isEmpty()) {
            enseignants.add(new String[]{"Inconnu", "Inconnu"});
        }
        
        return enseignants;
    }



    
    public void displayEventsParJour(List<biweekly.component.VEvent> events, LocalDate jour) {
        
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); 

        
        ColumnConstraints hourColumnConstraints = new ColumnConstraints(80, 80, 80); 
        hourColumnConstraints.setHgrow(Priority.NEVER); 
        calendarGrid.getColumnConstraints().add(hourColumnConstraints);

        
        ColumnConstraints eventColumnConstraints = new ColumnConstraints(400, 400, Double.MAX_VALUE); 
        eventColumnConstraints.setHgrow(Priority.ALWAYS); 
        
        calendarGrid.getColumnConstraints().add(eventColumnConstraints);


        
        String dayLabel = jour.format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.FRANCE));
        Text dayLabelText = new Text(dayLabel);
        dayLabelText.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 
        VBox dayHeader = new VBox(dayLabelText);
        dayHeader.setAlignment(Pos.CENTER);
        calendarGrid.add(dayHeader, 1, 0);




        
        VBox hoursLabelColumn = new VBox(2);
        
        int totalHalfHourSlots = (19 - 8) * 2 + 2;

        
        Color color=Color.LIGHTGRAY;

        for (int i = 8; i <= 19; i++) {
            
            Text hourLabel1 = new Text(String.format("%02d:00", i));
            Text hourLabel2 = new Text(String.format("%02d:30", i));
            VBox hourLabels1 = new VBox(hourLabel1);
            VBox hourLabels2 = new VBox(hourLabel2);
            hourLabels1.setBackground(Background.fill(color));
            hourLabels2.setBackground(Background.fill(color));
            hourLabels1.setAlignment(Pos.CENTER);
            
            hourLabels1.setStyle("-fx-border-color: black; -fx-border-width: 0.1px; ");
            hourLabels2.setStyle("-fx-border-color: black; -fx-border-width: 0.1px;");
            
            hourLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            hourLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            
            hourLabels2.setAlignment(Pos.CENTER);
            calendarGrid.add(hourLabels1, 0, (i - 8) * 2 + 1);
            calendarGrid.add(hourLabels2, 0, (i - 8) * 2 + 2);
            color=(color == Color.LIGHTGRAY) ? Color.WHITE: (Color.LIGHTGRAY);
        }
        color = Color.WHITE;
        for (int i = 1; i <= totalHalfHourSlots; i++) {
            if (i % 2 == 1) { 
                color = (color.equals(Color.LIGHTGRAY)) ? Color.WHITE : Color.LIGHTGRAY;
            }

            for (int j = 1; j < 2; j++) {
                VBox cell = createStyledCell(color);
                calendarGrid.add(cell, j, i);
            }
        }

        
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (eventDate.isAfter(jour.minusDays(1)) && eventDate.isBefore(jour.plusDays(5))) {
                int columnIndex = (int) ChronoUnit.DAYS.between(jour, eventDate) + 1; 
                
                
                int startHour = Math.max(startTime.getHour(), 8);
                int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0)+1;
                
                startRowIndex = Math.max(startRowIndex, 1);

                
                int endHour = Math.min(endTime.getHour(), 19); 
                int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() == 30 ? 1 : 0);
                
                endRowIndex = Math.min(endRowIndex, 23); 

                
                int durationInHalfHours = endRowIndex - startRowIndex + 1;

                
                durationInHalfHours = Math.max(durationInHalfHours, 1); 
                if (durationInHalfHours == 1) {
                    durationInHalfHours = totalHalfHourSlots;
                }
                
                String description = event.getDescription() != null ? event.getDescription().getValue() : "No description provided";
                
                String displayText = String.format("%s", description);
                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200); 

                eventText.setStyle("-fx-font-size: 10;"); 
                VBox eventBox = new VBox(eventText);
                
                eventBox.setStyle("-fx-background-color: #f4a261; -fx-border-color: #000000; -fx-border-width: 0.3px; -fx-background-radius: 7px; -fx-border-radius: 7px;");
                
                eventBox.setPadding(new Insets(5));
                

                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, durationInHalfHours);
                GridPane.setMargin(eventBox, new Insets(2));
                
                VBox.setMargin(eventBox, new Insets(2)); 

                calendarGrid.add(eventBox, columnIndex, startRowIndex); 
            }
        }
    }

    public GridPane getCalendarGrid() {
        return calendarGrid;
    }

    public void filterEvents(String searchText) {
        
        
        List<VEvent> filteredEvents = this.events.stream()
                .filter(e -> e.getSummary().getValue().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        
        setEvents(filteredEvents,YearMonth.now());
    }
    
    public void setEvents(List<biweekly.component.VEvent> events,YearMonth Month) {
        this.events = events;
        
        displayEventsParMois(events,Month);
    }

    public void displayAllEvents() {
        
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
        
        List<VEvent> nowEvents = getEvents().stream()
                .filter(event -> event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(now))
                .collect(Collectors.toList());
        displayEventsParJour(nowEvents,now);
    }

    public void filterEventsWeek(LocalDate currentSemaine) {
        
        List<VEvent> weekEvents = getEvents().stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(currentSemaine.minusDays(1)) && eventDate.isBefore(currentSemaine.plusDays(7));
                })
                .collect(Collectors.toList());
        displayEventsParSemaine(weekEvents,currentSemaine);
    }

    public void filterEventsMonth(YearMonth month) {
        
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        
        List<VEvent> monthEvents = getEvents().stream()
                .filter(event -> {
                    LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDate.isAfter(startOfMonth.minusDays(0)) && eventDate.isBefore(endOfMonth.plusDays(0));
                })
                .collect(Collectors.toList());
        YearMonth currentMonth = YearMonth.from(startOfMonth);
        displayEventsParMois(monthEvents, currentMonth);
    }


    public void addEvent(VEvent newEvent) {
        
        this.events.add(newEvent);
    }
    public void addEventToCalendarView(EventModel eventModel) {
        
        LocalDate eventDate = LocalDate.parse(eventModel.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        
        LocalTime startTime = LocalTime.parse(eventModel.getStartHour(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(eventModel.getEndHour(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate semaine = this.calendarController.getCurrentSemaine();
        if (eventDate.isAfter(semaine.minusDays(1)) && eventDate.isBefore(semaine.plusDays(5))) {
            int columnIndex = (int) ChronoUnit.DAYS.between(semaine, eventDate) + 1; 
            int startHour = Math.max(startTime.getHour(), 8);
            int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0) + 1;
            startRowIndex = Math.max(startRowIndex, 1);

            
            int endHour = Math.min(endTime.getHour(), 19); 
            int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() == 30 ? 1 : 0);
            
            endRowIndex = Math.min(endRowIndex, 23); 

            
            int durationInHalfHours = endRowIndex - startRowIndex + 1;

            
            durationInHalfHours = Math.max(durationInHalfHours, 1); 
            if (durationInHalfHours == 1) {
                durationInHalfHours = 24 - 2;
            }
            
            String description = eventModel.getDescription() != null ? eventModel.getDescription() : "Jour Férié";
            String type = getTypesFromDescription(description)+"/"+eventModel.getColor();

            String displayText = String.format("%s", description);
            Text eventText = new Text(displayText);
            eventText.setWrappingWidth(200);

            eventText.setStyle("-fx-font-size: 10;");
            VBox eventBox = new VBox(eventText);
            
            String eventBoxStyle = stylishEventBox(type);
            eventBox.setStyle(eventBoxStyle);

            eventBox.setPadding(new Insets(5));

            String tooltipText = String.format("Description: %s", description);
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(eventBox, tooltip);
            
            tooltip.setShowDuration(Duration.INDEFINITE);


            
            tooltip.setShowDelay(Duration.millis(100)); 
            GridPane.setRowIndex(eventBox, startRowIndex);
            GridPane.setRowSpan(eventBox, durationInHalfHours);
            GridPane.setMargin(eventBox, new Insets(2));
            
            VBox.setMargin(eventBox, new Insets(2)); 
            eventBox.setOnMouseClicked(mouseEvent -> {
                
                calendarController.showEventOptionsDialog(eventModel);
            });

                calendarGrid.add(eventBox, columnIndex, startRowIndex, 1, durationInHalfHours);
        }
    }

    public void clearEvents() {
        
        this.events.clear();
        this.calendarGrid.getChildren().clear();
    }
}
