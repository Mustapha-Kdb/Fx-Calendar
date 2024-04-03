package com.example.fxcalendar.Modele;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserModel {
    private String username;
    private String password;
    private String role;
    private String formation;
    private String theme;

    @JsonCreator
    public UserModel(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") String role, @JsonProperty("formation") String formation, @JsonProperty("theme") String theme) {
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
        if(username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
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
