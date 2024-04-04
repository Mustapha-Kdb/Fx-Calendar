package com.example.fxcalendar.Controleur;

import biweekly.component.VEvent;
import com.example.fxcalendar.Modele.EventModel;
import com.example.fxcalendar.Modele.UserModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
            InputStream is = getClass().getClassLoader().getResourceAsStream("users.json");
            if (is == null) {
                throw new FileNotFoundException("users.json file not found");
            }
            users = objectMapper.readValue(is, new TypeReference<List<UserModel>>() {});
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
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
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
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
            File file = new File("src/main/resources/users.json");
            objectMapper.writeValue(file, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEventToUser(String username, VEvent vEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
        File file = new File("src/main/resources/users.json");

        try {
            
            List<UserModel> users = objectMapper.readValue(file, new TypeReference<List<UserModel>>() {
            });
            UserModel user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user != null) {
                
                EventModel eventModel = convertVEventToEventModel(user, vEvent);
                user.getEvents().add(eventModel);

                
                user.addEvent(eventModel);
                List<UserModel> allUsers = getAllUsers();

                saveUsers(allUsers);

                
                objectMapper.writeValue(file, users);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addEventToUser(String username, EventModel eventModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
        File file = new File("src/main/resources/users.json");

        try {
            
            List<UserModel> users = objectMapper.readValue(file, new TypeReference<List<UserModel>>() {
            });
            UserModel user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

            if (user != null) {
                
                user.getEvents().add(eventModel);

                
                user.addEvent(eventModel);
                List<UserModel> allUsers = getAllUsers();

                saveUsers(allUsers);

                
                objectMapper.writeValue(file, users);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<UserModel> getAllUsers() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); 
            return objectMapper.readValue(new File("src/main/resources/users.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, UserModel.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private EventModel convertVEventToEventModel(UserModel user, VEvent vEvent) {
        
        String title = "No Title";
        String description = "No Description";
        String startHour = "08:00";
        String endHour = "09:00";
        String location = "No Location";
        String date = LocalDate.now().toString();
        String duration = "1"; 
        String color = "#0000FF"; 
        String formation = user.getFormation();

        if (vEvent.getDescription() != null && vEvent.getDescription().getValue() != null) {
            title = vEvent.getDescription().getValue();
        }

        if (vEvent.getDescription() != null && vEvent.getDescription().getValue() != null) {
            description = vEvent.getDescription().getValue();
        }

        if (vEvent.getDateStart() != null && vEvent.getDateStart().getValue() != null) {
            startHour = vEvent.getDateStart().getValue().toString(); 
            date = LocalDate.from(vEvent.getDateStart().getValue().toInstant().atZone(ZoneId.systemDefault())).toString();
        }

        if (vEvent.getDateEnd() != null && vEvent.getDateEnd().getValue() != null) {
            endHour = vEvent.getDateEnd().getValue().toString(); 
            
        }

        if (vEvent.getLocation() != null && vEvent.getLocation().getValue() != null) {
            location = vEvent.getLocation().getValue();
        }


        
        

        return new EventModel(user.getUsername(), title, description, date, startHour, endHour, location, color, duration, formation);
    }


    public void deleteEventFromUser(String username, EventModel eventToRemove) {
        
        UserModel user = findUserByUsername(username);

        if (user != null) {
            user.getEvents().removeIf(event ->
                    event.getTitle().equals(eventToRemove.getTitle()) &&
                            event.getDate().equals(eventToRemove.getDate()) &&
                            event.getStartHour().equals(eventToRemove.getStartHour())
            );


            saveUsers(users);
            user.setEvents(user.getEvents());
        }

    }


    private UserModel findUserByUsername(String username) {
        for (UserModel user : users) {
            if (user.getUsername().equals(username)) {
                return user; 
            }
        }
        return null; 
    }




}

