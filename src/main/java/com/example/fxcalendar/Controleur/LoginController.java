package com.example.fxcalendar.Controleur;

import com.example.fxcalendar.CalendarApp;
import com.example.fxcalendar.Controleur.UserController;
import com.example.fxcalendar.Modele.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Locale;

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
        UserModel user = userController.authenticate(usernameField.getText().toLowerCase(Locale.ROOT), passwordField.getText().toLowerCase());
        if (user != null) {
            // Supposons que UserModel contient maintenant des champs pour `formation` et `theme`
            String formation = user.getFormation();
            String theme = user.getTheme();
            String role = user.getRole();
            String username = user.getUsername();// Assurez-vous que ces méthodes existent
            app.loadCalendarView(formation, theme, role, username); // Modifiez cette méthode pour accepter ces paramètres
        } else {
            loginMessage.setText("Identifiant ou mot de passe incorrect.");
        }
    }
}

