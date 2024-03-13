package com.example.fxcalendar;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Assurez-vous d'utiliser javafx.scene.layout.VBox

public class SearchAndFilterPanel {

    private VBox searchPanel; // Variable d'instance pour stocker le conteneur
    private final CalendarView calendarView; // Référence à CalendarView pour interaction

    public SearchAndFilterPanel(CalendarView calendarView) {
        this.calendarView = calendarView; // Stockez la référence à CalendarView
        configureSearchPanel(); // Configurez le panneau de recherche lors de l'initialisation
    }

    private void configureSearchPanel() {
        searchPanel = new VBox(5); // Initialisation de VBox

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un événement...");
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            calendarView.filterEvents(searchText);
        });

        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll( "Jour", "Semaine", "Mois");
        filterComboBox.setValue("Mois");
        filterComboBox.setOnAction(e -> {
            CalendarView.filterOption = filterComboBox.getValue();
            calendarView.applyFilter(CalendarView.filterOption);
        });




        searchPanel.getChildren().addAll(searchField, filterComboBox, searchButton);
    }


    public VBox getPanel() {
        // Retourne le VBox contenant les éléments de recherche et de filtre

        return searchPanel;
    }
}
