package com.example.fxcalendar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

public class HelloController {
    @FXML
    private Label welcomeText;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void onPlanningButtonClick(javafx.event.ActionEvent actionEvent) {
        try {
            // Charge la nouvelle vue FXML pour l'emploi du temps
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("planning-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400); // Adaptez les dimensions selon votre besoin
            Stage stage = new Stage();
            stage.setTitle("Emploi du Temps");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}