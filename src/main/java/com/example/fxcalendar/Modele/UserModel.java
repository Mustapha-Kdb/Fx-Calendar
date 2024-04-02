package com.example.fxcalendar.Modele;

public class UserModel {
    private String username;
    private String password;
    private String role;
    private String formation;
    private String theme;

    public UserModel() {
        // Ceci est nécessaire pour la désérialisation JSON
    }
    public UserModel(String username, String password, String role, String formation, String theme) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.formation = formation;
        this.theme = theme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
