package com.example.fxcalendar.Controleur;

import com.example.fxcalendar.CalendarApp;
import com.example.fxcalendar.Controleur.UserController;
import com.example.fxcalendar.Modele.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginMessage;

    private UserController userController = new UserController();
    private CalendarApp app; // Ajout de cette ligne

    public void setApp(CalendarApp app) {
        this.app = app;
    }

    @FXML
    protected void handleLogin() {
        UserModel user = userController.authenticate(usernameField.getText(), passwordField.getText());
        if (user != null) {
            app.loadCalendarView(); // Redirection vers le calendrier
        } else {
            loginMessage.setText("Identifiant ou mot de passe incorrect.");
        }
    }
}

