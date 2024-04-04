package com.example.fxcalendar.Controleur;

import com.example.fxcalendar.CalendarApp;
import com.example.fxcalendar.Modele.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Locale;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginMessage;

    private UserController userController = new UserController();
    private CalendarApp app; 

    public void setApp(CalendarApp app) {
        this.app = app;
    }

    @FXML
    protected void handleLogin() {
        UserModel user = userController.authenticate(usernameField.getText().toLowerCase(Locale.ROOT), passwordField.getText().toLowerCase());
        if (user != null) {
            

            app.loadCalendarView(user); 
        } else {
            loginMessage.setText("Identifiant ou mot de passe incorrect.");
        }
    }

    public void initialize() {
        usernameField.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());
    }

    public void resetView() {
        usernameField.setText("");
        passwordField.setText("");
        loginMessage.setText("");
    }

    public void setUser(UserModel userModel) {
        usernameField.setText(userModel.getUsername());
        passwordField.setText(userModel.getPassword());

    }
}

