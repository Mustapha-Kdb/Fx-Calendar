package com.example.fxcalendar.Controleur;

import biweekly.component.VEvent;
import com.example.fxcalendar.ICalendarReader;
import com.example.fxcalendar.Modele.RoomModel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {
    
    private Map<String, RoomModel> rooms = new HashMap<>();
    private ICalendarReader calendarReader = new ICalendarReader();


    public RoomManager() {
        this.rooms = new HashMap<>();
        
        List<VEvent> eventsSTAT1 = calendarReader.fetchAndParseCalendarData("STAT 1");
        List<VEvent> eventsS2 = calendarReader.fetchAndParseCalendarData("S2");
        List<VEvent> eventsS3 = calendarReader.fetchAndParseCalendarData("S3");


        rooms.put("STAT 1", new RoomModel("STAT 1",eventsSTAT1));
        rooms.put("S2", new RoomModel("S2",eventsS2));
        rooms.put("S3", new RoomModel("S3",eventsS3));
    }


    
    public boolean isRoomAvailable(String roomName, LocalDateTime start, LocalDateTime end) {
        RoomModel room = rooms.get(roomName);
        return room != null && room.isAvailable(start, end);
    }

    
    public boolean bookRoom(String roomName, LocalDateTime start, LocalDateTime end, String reservedBy) {
        RoomModel room = rooms.get(roomName);
        if (room != null) {
            return room.addReservation(start, end, reservedBy);
        }
        return false;
    }

}
