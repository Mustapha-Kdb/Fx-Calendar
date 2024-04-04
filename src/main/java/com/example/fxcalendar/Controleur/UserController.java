package com.example.fxcalendar.Controleur;

import biweekly.component.VEvent;
import com.example.fxcalendar.Modele.EventModel;
import com.example.fxcalendar.Modele.UserModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class UserController {
    private List<UserModel> users;

    public UserController() {
        this.loadUsers();
    }

    private void loadUsers() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Activer la mise en forme
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

    public void updateUser(UserModel user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Activer la mise en forme
            List<UserModel> updatedUsers = objectMapper.readValue(new File("src/main/resources/users.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, UserModel.class));
            for (int i = 0; i < updatedUsers.size(); i++) {
                if (updatedUsers.get(i).getUsername().equals(user.getUsername())) {
                    updatedUsers.set(i, user);
                    break;
                }
            }
            objectMapper.writeValue(new File("src/main/resources/users.json"), updatedUsers);
            this.loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUsers(List<UserModel> users) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Activer la mise en forme
            File file = new File("src/main/resources/users.json");
            objectMapper.writeValue(file, users);
            System.out.println("Successfully updated users.json with events.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update users.json.");
        }
    }

    public void addEventToUser(String username, VEvent vEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Activer la mise en forme
        File file = new File("src/main/resources/users.json");

        try {
            // Charger la liste actuelle des utilisateurs
            List<UserModel> users = objectMapper.readValue(file, new TypeReference<List<UserModel>>() {
            });
            UserModel user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user != null) {
                // Convertir VEvent en un format approprié pour le JSON (par exemple, EventModel ou un Map)
                EventModel eventModel = convertVEventToEventModel(user, vEvent);
                user.getEvents().add(eventModel);

                // Ajouter l'événement à l'utilisateur
                user.addEvent(eventModel);
                List<UserModel> allUsers = getAllUsers();

                saveUsers(allUsers);

                // Sauvegarder la liste mise à jour des utilisateurs dans le fichier JSON
                objectMapper.writeValue(file, users);
                System.out.println(this + "Successfully added event to user.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addEventToUser(String username, EventModel eventModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Activer la mise en forme
        File file = new File("src/main/resources/users.json");

        try {
            // Charger la liste actuelle des utilisateurs
            List<UserModel> users = objectMapper.readValue(file, new TypeReference<List<UserModel>>() {
            });
            UserModel user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user != null) {
                // Convertir VEvent en un format approprié pour le JSON (par exemple, EventModel ou un Map)
                user.getEvents().add(eventModel);

                // Ajouter l'événement à l'utilisateur
                user.addEvent(eventModel);
                List<UserModel> allUsers = getAllUsers();

                saveUsers(allUsers);

                // Sauvegarder la liste mise à jour des utilisateurs dans le fichier JSON
                objectMapper.writeValue(file, users);
                System.out.println(this + "Successfully added event to user.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<UserModel> getAllUsers() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Activer la mise en forme
            return objectMapper.readValue(new File("src/main/resources/users.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, UserModel.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private EventModel convertVEventToEventModel(UserModel user, VEvent vEvent) {
        // Default values
        String title = "No Title";
        String description = "No Description";
        String startHour = "08:00";
        String endHour = "09:00";
        String location = "No Location";
        String date = LocalDate.now().toString();
        String duration = "1"; // Default duration
        String color = "#0000FF"; // Default color (blue)
        String formation = user.getFormation();

        if (vEvent.getSummary() != null && vEvent.getSummary().getValue() != null) {
            title = vEvent.getDescription().getValue();
        }

        if (vEvent.getDescription() != null && vEvent.getDescription().getValue() != null) {
            description = vEvent.getDescription().getValue();
        }

        if (vEvent.getDateStart() != null && vEvent.getDateStart().getValue() != null) {
            startHour = vEvent.getDateStart().getValue().toString(); // You may need to format this properly
            date = LocalDate.from(vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault())).toString();
        }

        if (vEvent.getDateEnd() != null && vEvent.getDateEnd().getValue() != null) {
            endHour = vEvent.getDateEnd().getValue().toString(); // You may need to format this properly
            // Calculate duration based on start and end time if necessary
        }

        if (vEvent.getLocation() != null && vEvent.getLocation().getValue() != null) {
            location = vEvent.getLocation().getValue();
        }


        // Assuming you have a way to set or get the color from the event, this is a placeholder.
        // Color might be stored in another property or require a custom solution based on your application's design.

        return new EventModel(user.getUsername(), title, description, date, startHour, endHour, location, color, duration, formation);
    }


    public void deleteEventFromUser(String username, EventModel eventToRemove) {
        // Votre logique actuelle pour trouver l'utilisateur
        UserModel user = findUserByUsername(username);

        if (user != null) {
            user.getEvents().removeIf(event ->
                    event.getTitle().equals(eventToRemove.getTitle()) &&
                            event.getDate().equals(eventToRemove.getDate()) &&
                            event.getStartHour().equals(eventToRemove.getStartHour())
            );

            System.out.println("L'événement "+ eventToRemove.getTitle() +" a été supprimé avec succès.");
            // Ensuite, sauvegarder les utilisateurs mis à jour dans le fichier JSON comme avant
            saveUsers(users);
        }

    }


    private UserModel findUserByUsername(String username) {
        for (UserModel user : users) {
            if (user.getUsername().equals(username)) {
                return user; // Retourner l'utilisateur si trouvé
            }
        }
        return null; // Retourner null si aucun utilisateur correspondant n'est trouvé
    }




}

