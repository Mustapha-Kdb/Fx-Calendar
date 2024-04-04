package com.example.fxcalendar.Modele;

import biweekly.component.VEvent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RoomModel {
    private String roomName;
    private List<Reservation> reservations;

    public RoomModel(String roomName, List<VEvent> events) {
        this.roomName = roomName;
        this.reservations = eventsToReservations(events);
    }

    private List<Reservation> eventsToReservations(List<VEvent> events) {
        List<Reservation> reservations = new ArrayList<>();
        for (VEvent event : events) {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getDateStart().getValue().getTime()), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getDateEnd().getValue().getTime()), ZoneId.systemDefault());
            String reservedBy = event.getSummary().getValue();
            reservations.add(new Reservation(start, end, reservedBy));
        }
        return reservations;
    }

    // Ajoute une réservation si le créneau est libre
    public boolean addReservation(LocalDateTime start, LocalDateTime end, String reservedBy) {
        if (isAvailable(start, end)) {
            reservations.add(new Reservation(start, end, reservedBy));
            return true;
        }
        return false;
    }

    // Vérifie si la salle est disponible pour un créneau donné
    public boolean isAvailable(LocalDateTime start, LocalDateTime end) {
        for (Reservation reservation : reservations) {
            if (start.isBefore(reservation.getEnd()) && end.isAfter(reservation.getStart())) {
                return false;
            }
        }
        return true;
    }

    public String getRoomName() {
        return roomName;
    }

    // Une classe interne pour représenter une réservation
    private static class Reservation {
        private LocalDateTime start;
        private LocalDateTime end;
        private String reservedBy;

        public Reservation(LocalDateTime start, LocalDateTime end, String reservedBy) {
            this.start = start;
            this.end = end;
            this.reservedBy = reservedBy;
        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalDateTime getEnd() {
            return end;
        }

        public String getReservedBy() {
            return reservedBy;
        }
    }
}
