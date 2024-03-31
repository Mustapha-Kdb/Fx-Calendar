package com.example.fxcalendar.Vue;

import biweekly.component.VEvent;

import java.util.List;

public interface EventDataProvider {
    List<VEvent> getEvents();
}
