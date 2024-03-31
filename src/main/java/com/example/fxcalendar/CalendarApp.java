package com.example.fxcalendar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class CalendarApp extends Application {
    private static final String LIGHT_THEME = "light-theme.css";
    private static final String DARK_THEME = "dark-theme.css";

    private boolean isDarkTheme = false;

    private Scene scene;
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charge le fichier FXML de l'interface utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarView.fxml"));
            Parent root = loader.load();



            // Configure le titre de la fenêtre principale
            primaryStage.setTitle("University Calendar");

            // Obtient les dimensions de l'écran
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Définit la taille de la scène pour qu'elle s'adapte aux dimensions de l'écran
            scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            // Par défaut, charger le thème clair
            setTheme(scene, LIGHT_THEME);

            // Définit la scène sur la fenêtre principale
            primaryStage.setScene(scene);
            // Configure le titre de la fenêtre principale
            primaryStage.setTitle("University Calendar");

            // Affiche la fenêtre principale
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(theme).toExternalForm());
    }
    public void toggleTheme() {
        if (isDarkTheme) {
            setTheme(this.scene, LIGHT_THEME);
        } else {
            setTheme(this.scene, DARK_THEME);
        }
        isDarkTheme = !isDarkTheme;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
