package com.example.fxcalendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ICalendarReader {

    public List<VEvent> fetchAndParseCalendarData(String urlString) {
        List<VEvent> events = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String icsData = new Scanner(connection.getInputStream()).useDelimiter("\\A").next();

            List<ICalendar> calendars = Biweekly.parse(icsData).all();
            for (ICalendar calendar : calendars) {
                events.addAll(calendar.getEvents());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }
}

