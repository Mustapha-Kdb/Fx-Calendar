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
import java.time.LocalTime;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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



    // displayEventsParMois
    public void displayEventsParMois(List<biweekly.component.VEvent> events, YearMonth Month) {
        //vider le GridPane avant d'ajouter de nouveaux événements
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); // Effacer les contraintes de colonne existantes
        calendarGrid.getRowConstraints().clear(); // Effacer aussi les contraintes de ligne existantes

        // Nombre total de jours dans le mois
        int totalDaysInMonth = Month.lengthOfMonth();

        // Nombre de colonnes et de lignes dans la grille (GridPane)
        int numColumns = 5; // 7 jours dans une semaine
        int numRows = (totalDaysInMonth + numColumns - 1) / numColumns; // Arrondi vers le haut

        // Déclaration et initialisation des variables columnIndex et rowIndex
        int columnIndex = 0;
        int rowIndex = 1;


        // Créer un tableau pour suivre le nombre de séances pour chaque jour du mois
        int[] sessionCountPerDay = new int[totalDaysInMonth];
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dayOfMonth = eventDate.getDayOfMonth();
            if (dayOfMonth >= 1 && dayOfMonth <= totalDaysInMonth) {
                sessionCountPerDay[dayOfMonth - 1]++;
            } else {
                // Gérer le cas où dayOfMonth est en dehors de la plage valide
                System.err.println("Warning: dayOfMonth is out of range: " + dayOfMonth);
            }

        }

        String[] joursDeLaSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        RowConstraints headerRowConstraints = new RowConstraints(40,40,40); // Hauteur fixe pour les en-têtes
        calendarGrid.getRowConstraints().add(headerRowConstraints);
        calendarGrid.setStyle("-fx-border-width: 1px; -fx-border-color: black; -fx-border-style: solid; -fx-border-radius: 5px; -fx-padding: 5px; -fx-background-color: #f4f4f4;");

        for (int i = 0; i < 5; i++) { // Monday to Friday
            // Création d'un label avec le jour de la semaine
            Label dayLabel = new Label(joursDeLaSemaine[i]);
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Ajustement de la taille de la police
            dayLabel.setAlignment(Pos.CENTER); // Centre le texte dans le Label
            // Optionnel: Si vous utilisez CSS pour styliser, vous pouvez ajouter une classe CSS
            // dayLabel.getStyleClass().add("day-header");

            // Création d'un VBox pour contenir le label (facultatif si vous n'ajoutez pas d'autres éléments dans le VBox)
            VBox dayHeader = new VBox(dayLabel);
            dayHeader.setAlignment(Pos.CENTER); // Centre le contenu du VBox

            // Ajout du VBox à votre GridPane
            dayHeader.getStyleClass().add("grid-cell");
            calendarGrid.add(dayHeader, i, 0);
        }




        // Ajouter un texte pour chaque jour dans le calendrier avec le nombre de séances
        for (int i = 0; i < totalDaysInMonth; i++) {
            // Créer un label pour afficher le jour du mois sans saturday et sunday
            LocalDate day = LocalDate.of(Month.getYear(), Month.getMonthValue(), i + 1);
            if (day.getDayOfWeek().getValue() == 6 || day.getDayOfWeek().getValue() == 7) {
                continue; // Skip Saturday and Sunday
            }


            int dayOfMonth = i + 1;
            String emoji = "\uD83D\uDCC6"; // Emoji de calendrier

// Création du texte du label
            String labelText = "Le " + dayOfMonth + ", " + Month.getMonth().toString() + " " + emoji;

// Création du label avec le texte
            Label dayLabel = new Label(labelText); // Emoji for calendar
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

            // Créer un label pour afficher le nombre de séances pour ce jour
            int sessionCount = sessionCountPerDay[i];
            String emoji2 = "\uD83D\uDCFA"; // Emoji pour l'exemple

// Construction du texte pour le label
            String labelText2 = sessionCount + " séance" + (sessionCount > 1 ? "s " : " ") + emoji2;

// Création du label avec le texte
            Label sessionCountLabel = new Label(labelText2);
            // Créer un VBox pour contenir les labels du jour et du nombre de séances
            VBox dayVBox = new VBox();
            dayVBox.setAlignment(Pos.CENTER); // Aligner le contenu au centre
            dayVBox.getChildren().addAll(dayLabel, sessionCountLabel);

            // Ajouter un style au dayVbox
            dayVBox.setStyle("-fx-background-color: #ee974b; " +
                    "-fx-border-color: #000000; " +
                    "-fx-border-width: 0.3px; " +
                    "-fx-background-radius: 7px; " +
                    "-fx-border-radius: 7px;");




            // Ajouter une marge intérieure pour le VBox
            GridPane.setMargin(dayVBox, new Insets(3));

            // ajouter un effect lorsque hover




            // Ajouter un gestionnaire d'événements pour le survol de la souris
            int finalI = i;
            dayVBox.setOnMouseEntered(e -> {
                // Gérer l'événement de survol
                // Afficher les détails des séances pour ce jour
                // par exemple, en affichant une info-bulle avec les détails
                String sessionDetails = getSessionDetailsForDay(finalI + 1, Month);
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
                calendarController.setFilterOption("Semaine");
                calendarController.setViewChoiceBox("Semaine");
                // Mettre à jour la semaine actuelle pour afficher la semaine du jour cliqué
                LocalDate currentSemaine = clickedDate.minusDays(clickedDate.getDayOfWeek().getValue() - 1);
                calendarController.setCurrentSemaine(currentSemaine);
                calendarController.applyFilter();
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
            ColumnConstraints columnConstraints = new ColumnConstraints(250, 250, 250); // minWidth, prefWidth, maxWidth
            columnConstraints.setHgrow(Priority.ALWAYS); // Permettre à ces colonnes de s'étendre si nécessaire
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < numRows; i++) {

            RowConstraints rowConstraints = new RowConstraints(100, 100, 100);
            rowConstraints.setVgrow(Priority.ALWAYS); // Permettre à ces lignes de s'étendre si nécessaire
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
    }
    private VBox createStyledCell(Color color) {
        VBox cell = new VBox();
        cell.getStyleClass().add("grid-cell"); // Utilisez une classe CSS comme expliqué précédemment
        cell.setBackground(Background.fill(color));
        return cell;
    }
    public String getSessionDetailsForDay(int dayOfMonth, YearMonth Month) {
        // Implémentez la logique pour récupérer les détails des séances pour le jour donné

        return "Détails pour le " + dayOfMonth + ", " +  Month.getMonth().toString();
    }


    // displayEventsParSemaine
    public void displayEventsParSemaine(List<biweekly.component.VEvent> events, LocalDate semaine) {
        // Clear the GridPane before adding new events
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); // Effacer les contraintes de colonne existantes
        calendarGrid.getRowConstraints().clear(); // Effacer aussi les contraintes de ligne existantes


        // Définir les contraintes pour la colonne des heures à 50px
        ColumnConstraints hourColumnConstraints = new ColumnConstraints(80, 80, 80); // minWidth, prefWidth, maxWidth
        calendarGrid.getColumnConstraints().add(hourColumnConstraints);

        // Définir les contraintes pour les colonnes des jours (Lundi à Vendredi) à 400px
        for (int i = 0; i < 5; i++) {
            ColumnConstraints dayColumnConstraints = new ColumnConstraints(250, 250, 250); // minWidth, prefWidth, maxWidth
            dayColumnConstraints.setHgrow(Priority.ALWAYS); // Permettre à ces colonnes de s'étendre si nécessaire
            calendarGrid.getColumnConstraints().add(dayColumnConstraints);
        }
        // Configuration des contraintes de ligne pour fixer la hauteur de chaque demi-heure à 20px
        for (int i = 0; i < (19 - 8) * 2 + 3; i++) { // Pour chaque demi-heure de 8h à 19h
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(20); // Définir la hauteur préférée à 20px
            rowConstraints.setPercentHeight(100.0 / 25);
            rowConstraints.setValignment(VPos.CENTER); // Centrer le contenu verticalement
            calendarGrid.getRowConstraints().add(rowConstraints);
        }
        // Add headers for days at the top row, column 1-5
        for (int i = 0; i < 5; i++) { // Monday to Friday
            LocalDate dayDate = semaine.plusDays(i);
            String dayLabel = dayDate.format(DateTimeFormatter.ofPattern("EEE dd/MM", Locale.FRANCE));

            Text dayText = new Text(dayLabel);
            dayText.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Ajuster la taille de la police

            VBox dayHeader = new VBox(dayText);
            dayHeader.setAlignment(Pos.CENTER);
            dayHeader.getStyleClass().add("grid-cell");


            calendarGrid.add(dayHeader, i + 1, 0); // Notez que le dernier 1,1 signifie qu'il ne s'étend que sur une cellule
        }
        // Calculate the total number of half-hour slots in a day
        int totalHalfHourSlots = (19 - 8) * 2 + 2;

        // Add time labels in the first column, rows 1-24
        Color color=Color.LIGHTGRAY;

        for (int i = 8; i <= 19; i++) {
            // For each hour and half-hour slot
            Text hourLabel1 = new Text(String.format("%02d:00", i));
            Text hourLabel2 = new Text(String.format("%02d:30", i));
            VBox hourLabels1 = new VBox(hourLabel1);
            VBox hourLabels2 = new VBox(hourLabel2);
            hourLabels1.setBackground(Background.fill(color));
            hourLabels2.setBackground(Background.fill(color));
            hourLabels1.setAlignment(Pos.CENTER);
            // border the hour labels for better visibility
            hourLabels1.setStyle("-fx-border-color: black; -fx-border-width: 0.1px; ");
            hourLabels2.setStyle("-fx-border-color: black; -fx-border-width: 0.1px;");

            // fonte en gras pour les heures
            hourLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            hourLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            // diminuer le width de la cellule pour les heures
            hourLabels2.setAlignment(Pos.CENTER);
            calendarGrid.add(hourLabels1, 0, (i - 8) * 2 + 1);
            calendarGrid.add(hourLabels2, 0, (i - 8) * 2 + 2);
            color=(color == Color.LIGHTGRAY) ? Color.WHITE: (Color.LIGHTGRAY);
        }
        color = Color.WHITE;
        for (int i = 1; i <= totalHalfHourSlots; i++) {
            if (i % 2 == 1) { // Changez la couleur après chaque deux rangées (en utilisant 4 car il y a deux cellules par rangée d'heure)
                color = (color.equals(Color.LIGHTGRAY)) ? Color.WHITE : Color.LIGHTGRAY;
            }

            for (int j = 1; j < 6; j++) {
                VBox cell = createStyledCell(color);
                calendarGrid.add(cell, j, i);
            }
        }
        // Redimensionner les cellules de la grille pour s'adapter à la taille de la grille
        for (int i = 0; i < 5; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / 5);
            columnConstraints.setHalignment(HPos.CENTER); // Centrer le contenu horizontalement
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }





        // Sort events by start time
        events.sort(Comparator.comparing(event -> event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));

        // Add events to the appropriate time slots for each day
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (eventDate.isAfter(semaine.minusDays(1)) && eventDate.isBefore(semaine.plusDays(5))) {
                int columnIndex = (int) ChronoUnit.DAYS.between(semaine, eventDate) + 1; // Calculate the column index for the event
                // Calculate the starting row based on the start time
                // Start at 8:30 AM if event starts before 8:30 AM
                int startHour = Math.max(startTime.getHour(), 8);
                int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0)+1;
                // Ensure startRowIndex is non-negative
                startRowIndex = Math.max(startRowIndex, 1);

                // Calculate the ending row based on the end time
                int endHour = Math.min(endTime.getHour(), 19); // End at 7 PM if event ends after 7 PM
                int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() == 30 ? 1 : 0);
                // Ensure endRowIndex is within bounds
                endRowIndex = Math.min(endRowIndex, 23); // Assuming grid has 24 half-hour rows

                // Calculate the number of half-hour slots to span
                int durationInHalfHours = endRowIndex - startRowIndex + 1;

                // Ensure durationInHalfHours is positive
                durationInHalfHours = Math.max(durationInHalfHours, 1); // At least one half-hour slot
                if (durationInHalfHours == 1) {
                    durationInHalfHours = totalHalfHourSlots-2;
                }
                // String summary = event.getSummary() != null ? event.getSummary().getValue() : "No summary provided";
                String description = event.getDescription() != null ? event.getDescription().getValue() : "Jour Férié";
                String type = getTypesFromDescription(description);
                System.out.println(type);
                // Create the event box and style it
                String displayText = String.format("%s", description);
                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200); // Wrap text within the specified width

                eventText.setStyle("-fx-font-size: 10;"); // Set the font size if necessary
                VBox eventBox = new VBox(eventText);


                // make the border radius of the event box and the color only covering the box
                String eventBoxStyle = stylishEventBox(type);
                eventBox.setStyle(eventBoxStyle);
                // inner margin for the text inside the event box
                eventBox.setPadding(new Insets(5));
                // inner margin for the event box inside the grid cell
//                double eventDurationInMinutes = Duration.between(startTime, endTime).toMinutes();
//                double heightPer30Min = 20; // Hauteur pour 30 minutes
//                double totalEventHeight = (eventDurationInMinutes / 30.0) * heightPer30Min;

//                eventBox.setMaxHeight(totalEventHeight); // Définir l'hauteur maximale basée sur la durée

                // Création d'une infobulle avec le contenu complet de l'événement
                String tooltipText = String.format("Description: %s", description  + "Heure de debut: " + event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime() + "\n" + "Heure de fin: " + event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(eventBox, tooltip);
                // Ajuster la durée pour que la Tooltip ne se cache pas automatiquement
                tooltip.setShowDuration(Duration.INDEFINITE);

                tooltip.setShowDelay(Duration.millis(100)); // Délai avant d'afficher la Tooltip


                List<String[]> enseignants = getNomsEnseignantsFromDescription(description);


                // Pour l'affichage et l'envoi d'e-mails groupés
                StringBuilder noms = new StringBuilder();
                StringBuilder emails = new StringBuilder();

                for (String[] enseignant : enseignants) {
                    if (noms.length() > 0) noms.append(", ");
                    noms.append(enseignant[0]);
                    if (emails.length() > 0) emails.append(",");
                    emails.append(enseignant[1]);
                }

                String enseignantNoms = noms.toString();
                String enseignantEmails = emails.toString();

                System.out.println(enseignantNoms + " <" + enseignantEmails + ">");

                String subject = "Sujet de l'email";
                String body = "Corps de l'email. Rendez-vous concernant: " ;
                if (!enseignants.isEmpty() && !"Inconnu".equals(enseignants.get(0)[0]))
                    eventBox.setOnMouseClicked(mouseEvent -> {
                    try {
                        // Encoder les valeurs de subject et body
                        String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8);
                        String encodedBody = URLEncoder.encode(body, StandardCharsets.UTF_8);

                        String mailto = "mailto:" + enseignantEmails + "?subject=" + encodedSubject + "&body=" + encodedBody;
                        // Utiliser Desktop pour ouvrir le client de messagerie avec l'URI mailto
                        java.awt.Desktop.getDesktop().mail(new URI(mailto));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    });

                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, durationInHalfHours);
                GridPane.setMargin(eventBox, new Insets(2));
                // Add a small gap between events
                VBox.setMargin(eventBox, new Insets(2)); // Adjust as needed

                calendarGrid.add(eventBox, columnIndex, startRowIndex); // Add the event box to the grid

            }
        }
        if(calendarController.getUser().getFormation().equals(calendarController.getTextformation()))
            calendarController.loadUserEvents(calendarController.getUser());
    }

    private String stylishEventBox(String type) {
        String color = "#ee974b"; // Default color
        //exemple pour parser la couleur type="personnel/blue"
        String[] parts = type.split("/");
        type = parts[0];
        //switch color from par ex "personnel/0xf00" to "#f44336"
        String color1 = parts.length > 1 ? convertHexToCssColor(parts[1]) : "#f44336";
        switch (type.toLowerCase()) {
            case "cours":
                color = "#f4a261"; // Orange
                break;
            case "td":
                color = "#08C5D1"; // Green
                break;
            case "tp":
                color = "#08C5D1"; // Blue
                break;
                case "cm/td":
                color = "#08C5D1"; // Blue
                break;
            case "evaluation":
                color = "#CF4146"; // Red
                break;
            case "personnel":
                color = color1; // Purple
                break;
            default:
                color = "#f4a261"; // Grey
                break;
        }



        return "-fx-background-color: " + color + "; -fx-border-color: #000000; -fx-border-width: 0.3px; -fx-background-radius: 7px; -fx-border-radius: 7px;";
    }

    // Méthode pour convertir une valeur hexadécimale en couleur CSS
    private String convertHexToCssColor(String hex) {
        // Supprimer le préfixe "0x" si présent
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        // Ajouter un préfixe "#" si absent
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }

        // Si la longueur de la chaîne est inférieure à 7, ajouter des zéros à gauche pour obtenir une chaîne de 7 caractères
        while (hex.length() < 7) {
            hex = hex.substring(0, 1) + "0" + hex.substring(1);
        }

        return hex;
    }

    private String getTypesFromDescription(String description) {
        // Utiliser une expression régulière pour extraire la ligne contenant les types de séances
        Pattern pattern = Pattern.compile("Type\\s*:\\s*(.*)");
        Matcher matcher = pattern.matcher(description);

        // Si l'expression trouve une correspondance, extraire la liste des types de séances
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "Type inconnu";
    }

    private List<String[]>  getNomsEnseignantsFromDescription(String description) {
        // Liste pour stocker les noms d'enseignants formatés
        List<String[]>  enseignants = new ArrayList<>();

        // Utiliser une expression régulière pour extraire la ligne contenant les noms des enseignants
        Pattern pattern = Pattern.compile("Enseignants?\\s*:\\s*(.*)");
        Matcher matcher = pattern.matcher(description);

        // Si l'expression trouve une correspondance, extrayez la liste des enseignants
        if (matcher.find()) {
            // Séparer les noms des enseignants par virgule
            String[] noms = matcher.group(1).trim().split(",");

            // Formater chaque nom et l'ajouter à la liste
            for (String nom : noms) {
                String nomEmail = nom.trim().toLowerCase().replaceAll("\\s+", "."); // Remplacer les espaces par des points
                enseignants.add(new String[]{nom, nomEmail + "@univ-avignon.fr"});
            }
        }

        if (enseignants.isEmpty()) {
            enseignants.add(new String[]{"Inconnu", "Inconnu"});
        }
        // Retourner la liste des enseignants formatés
        return enseignants;
    }



    // displayEventsParJour
    public void displayEventsParJour(List<biweekly.component.VEvent> events, LocalDate jour) {
        // Clear the GridPane before adding new events
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear(); // Clear existing column constraints

        // Définir les contraintes pour la colonne des heures
        ColumnConstraints hourColumnConstraints = new ColumnConstraints(80, 80, 80); // minWidth, prefWidth, maxWidth
        hourColumnConstraints.setHgrow(Priority.NEVER); // La colonne des heures ne doit pas s'étendre
        calendarGrid.getColumnConstraints().add(hourColumnConstraints);

        // Définir les contraintes pour les autres colonnes (400px)
        ColumnConstraints eventColumnConstraints = new ColumnConstraints(400, 400, Double.MAX_VALUE); // minWidth, prefWidth, maxWidth
        eventColumnConstraints.setHgrow(Priority.ALWAYS); // Permettre à ces colonnes de s'étendre si nécessaire
        // Ajouter les contraintes pour chaque colonne des événements. Supposons qu'il y en ait une seule pour cet exemple.
        calendarGrid.getColumnConstraints().add(eventColumnConstraints);


        // Add headers for day at the top row, column 1
        String dayLabel = jour.format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.FRANCE));
        Text dayLabelText = new Text(dayLabel);
        dayLabelText.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Adjust font if needed
        VBox dayHeader = new VBox(dayLabelText);
        dayHeader.setAlignment(Pos.CENTER);
        calendarGrid.add(dayHeader, 1, 0);




        // Add hours label column
        VBox hoursLabelColumn = new VBox(2);
        // Calculate the total number of half-hour slots in a day
        int totalHalfHourSlots = (19 - 8) * 2 + 2;

        // Add time labels in the first column, rows 1-24
        Color color=Color.LIGHTGRAY;

        for (int i = 8; i <= 19; i++) {
            // For each hour and half-hour slot
            Text hourLabel1 = new Text(String.format("%02d:00", i));
            Text hourLabel2 = new Text(String.format("%02d:30", i));
            VBox hourLabels1 = new VBox(hourLabel1);
            VBox hourLabels2 = new VBox(hourLabel2);
            hourLabels1.setBackground(Background.fill(color));
            hourLabels2.setBackground(Background.fill(color));
            hourLabels1.setAlignment(Pos.CENTER);
            // border the hour labels for better visibility
            hourLabels1.setStyle("-fx-border-color: black; -fx-border-width: 0.1px; ");
            hourLabels2.setStyle("-fx-border-color: black; -fx-border-width: 0.1px;");
            // fonte en gras pour les heures
            hourLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            hourLabel2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            // diminuer le width de la cellule pour les heures
            hourLabels2.setAlignment(Pos.CENTER);
            calendarGrid.add(hourLabels1, 0, (i - 8) * 2 + 1);
            calendarGrid.add(hourLabels2, 0, (i - 8) * 2 + 2);
            color=(color == Color.LIGHTGRAY) ? Color.WHITE: (Color.LIGHTGRAY);
        }
        color = Color.WHITE;
        for (int i = 1; i <= totalHalfHourSlots; i++) {
            if (i % 2 == 1) { // Changez la couleur après chaque deux rangées (en utilisant 4 car il y a deux cellules par rangée d'heure)
                color = (color.equals(Color.LIGHTGRAY)) ? Color.WHITE : Color.LIGHTGRAY;
            }

            for (int j = 1; j < 2; j++) {
                VBox cell = createStyledCell(color);
                calendarGrid.add(cell, j, i);
            }
        }

        // Add events to the appropriate time slots
        for (VEvent event : events) {
            LocalDate eventDate = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = event.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime = event.getDateEnd().getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (eventDate.isAfter(jour.minusDays(1)) && eventDate.isBefore(jour.plusDays(5))) {
                int columnIndex = (int) ChronoUnit.DAYS.between(jour, eventDate) + 1; // Calculate the column index for the event
                // Calculate the starting row based on the start time
                // Start at 8:30 AM if event starts before 8:30 AM
                int startHour = Math.max(startTime.getHour(), 8);
                int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0)+1;
                // Ensure startRowIndex is non-negative
                startRowIndex = Math.max(startRowIndex, 1);

                // Calculate the ending row based on the end time
                int endHour = Math.min(endTime.getHour(), 19); // End at 7 PM if event ends after 7 PM
                int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() == 30 ? 1 : 0);
                // Ensure endRowIndex is within bounds
                endRowIndex = Math.min(endRowIndex, 23); // Assuming grid has 24 half-hour rows

                // Calculate the number of half-hour slots to span
                int durationInHalfHours = endRowIndex - startRowIndex + 1;

                // Ensure durationInHalfHours is positive
                durationInHalfHours = Math.max(durationInHalfHours, 1); // At least one half-hour slot
                if (durationInHalfHours == 1) {
                    durationInHalfHours = totalHalfHourSlots;
                }
                // String summary = event.getSummary() != null ? event.getSummary().getValue() : "No summary provided";
                String description = event.getDescription() != null ? event.getDescription().getValue() : "No description provided";
                // Create the event box and style it
                String displayText = String.format("%s", description);
                Text eventText = new Text(displayText);
                eventText.setWrappingWidth(200); // Wrap text within the specified width

                eventText.setStyle("-fx-font-size: 10;"); // Set the font size if necessary
                VBox eventBox = new VBox(eventText);
                // make the border radius of the event box and the color only covering the box
                eventBox.setStyle("-fx-background-color: #f4a261; -fx-border-color: #000000; -fx-border-width: 0.3px; -fx-background-radius: 7px; -fx-border-radius: 7px;");
                // inner margin for the text inside the event box
                eventBox.setPadding(new Insets(5));
                // inner margin for the event box inside the grid cell

                GridPane.setRowIndex(eventBox, startRowIndex);
                GridPane.setRowSpan(eventBox, durationInHalfHours);
                GridPane.setMargin(eventBox, new Insets(2));
                // Add a small gap between events
                VBox.setMargin(eventBox, new Insets(2)); // Adjust as needed

                calendarGrid.add(eventBox, columnIndex, startRowIndex); // Add the event box to the grid
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


    public void addEvent(VEvent newEvent) {
        // Ajoutez un nouvel événement à la liste des événements
        this.events.add(newEvent);
    }
    public void addEventToCalendarView(EventModel eventModel) {
        // Convertir la date de l'événement en LocalDate
        LocalDate eventDate = LocalDate.parse(eventModel.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        // Convertir le début et la fin de l'événement en LocalTime
        LocalTime startTime = LocalTime.parse(eventModel.getStartHour(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(eventModel.getEndHour(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate semaine = this.calendarController.getCurrentSemaine();
        if (eventDate.isAfter(semaine.minusDays(1)) && eventDate.isBefore(semaine.plusDays(5))) {
            int columnIndex = (int) ChronoUnit.DAYS.between(semaine, eventDate) + 1; // Calculate the column index for the event
            int startHour = Math.max(startTime.getHour(), 8);
            int startRowIndex = (startHour - 8) * 2 + (startTime.getMinute() >= 30 ? 1 : 0) + 1;
            startRowIndex = Math.max(startRowIndex, 1);

            // Calculate the ending row based on the end time
            int endHour = Math.min(endTime.getHour(), 19); // End at 7 PM if event ends after 7 PM
            int endRowIndex = (endHour - 8) * 2 + (endTime.getMinute() == 30 ? 1 : 0);
            // Ensure endRowIndex is within bounds
            endRowIndex = Math.min(endRowIndex, 23); // Assuming grid has 24 half-hour rows

            // Calculate the number of half-hour slots to span
            int durationInHalfHours = endRowIndex - startRowIndex + 1;

            // Ensure durationInHalfHours is positive
            durationInHalfHours = Math.max(durationInHalfHours, 1); // At least one half-hour slot
            if (durationInHalfHours == 1) {
                durationInHalfHours = 24 - 2;
            }
            // String summary = event.getSummary() != null ? event.getSummary().getValue() : "No summary provided";
            String description = eventModel.getDescription() != null ? eventModel.getDescription() : "Jour Férié";
            String type = getTypesFromDescription(description)+"/"+eventModel.getColor();
            System.out.println(type);
            // Create the event box and style it
            String displayText = String.format("%s", description);
            Text eventText = new Text(displayText);
            eventText.setWrappingWidth(200); // Wrap text within the specified width

            eventText.setStyle("-fx-font-size: 10;"); // Set the font size if necessary
            VBox eventBox = new VBox(eventText);
            // make the border radius of the event box and the color only covering the box
            String eventBoxStyle = stylishEventBox(type);
            eventBox.setStyle(eventBoxStyle);
            // inner margin for the text inside the event box
            eventBox.setPadding(new Insets(5));
            // inner margin for the event box inside the grid cell
            // Création d'une infobulle avec le contenu complet de l'événement
            String tooltipText = String.format("Description: %s", description);
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(eventBox, tooltip);
            // Ajuster la durée pour que la Tooltip ne se cache pas automatiquement
            tooltip.setShowDuration(Duration.INDEFINITE);


            // Cette méthode nécessite JavaFX 9 ou supérieur
            tooltip.setShowDelay(Duration.millis(100)); // Délai avant d'afficher la Tooltip
            GridPane.setRowIndex(eventBox, startRowIndex);
            GridPane.setRowSpan(eventBox, durationInHalfHours);
            GridPane.setMargin(eventBox, new Insets(2));
            // Add a small gap between events
            VBox.setMargin(eventBox, new Insets(2)); // Adjust as needed
            eventBox.setOnMouseClicked(mouseEvent -> {
                // Appel à showEventOptionsDialog avec l'EventModel
                calendarController.showEventOptionsDialog(eventModel);
            });

                calendarGrid.add(eventBox, columnIndex, startRowIndex); // Add the event box to the grid
        }
    }
}
