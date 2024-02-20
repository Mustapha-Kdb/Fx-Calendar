package com.example.fxcalendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {
    private final SimpleStringProperty date;
    private final SimpleStringProperty description;

    public Event(String date, String description) {
        this.date = new SimpleStringProperty(date);
        this.description = new SimpleStringProperty(description);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
