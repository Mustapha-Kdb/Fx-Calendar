package com.example.fxcalendar;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Assurez-vous d'utiliser javafx.scene.layout.VBox

public class SearchAndFilterPanel {

    private VBox searchPanel; // Variable d'instance pour stocker le conteneur
    private CalendarView calendarView; // Référence à CalendarView pour interaction

    public SearchAndFilterPanel(CalendarView calendarView) {
        this.calendarView = calendarView; // Stockez la référence à CalendarView
        configureSearchPanel(); // Configurez le panneau de recherche lors de l'initialisation
    }

    private void configureSearchPanel() {
        searchPanel = new VBox(5); // Initialisation de VBox

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un événement...");

        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("Tous", "Aujourd'hui", "Cette semaine", "Ce mois-ci");
        filterComboBox.setValue("Tous");

        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> {
            // Ici, ajoutez la logique pour effectuer la recherche et appliquer les filtres
            // Cette logique interagira probablement avec calendarView pour mettre à jour l'affichage
        });

        searchPanel.getChildren().addAll(searchField, filterComboBox, searchButton);
    }

    public VBox getPanel() {
        // Retourne le VBox contenant les éléments de recherche et de filtre
        return searchPanel;
    }
}
