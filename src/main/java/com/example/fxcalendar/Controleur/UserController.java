package com.example.fxcalendar.Controleur;

import com.example.fxcalendar.Modele.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

public class UserController {
    private List<UserModel> users;

    public UserController() {
        this.loadUsers();
    }

    private void loadUsers() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(new File("src/main/resources/users.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, UserModel.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserModel authenticate(String username, String password) {
        for (UserModel user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}

