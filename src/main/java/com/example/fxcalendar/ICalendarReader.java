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

    public List<VEvent> fetchAndParseCalendarData(String Formation) {
        String urlFormation = "";
        switch (Formation.toUpperCase()){
            case "M1 IA":
                urlFormation = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200ff18615ca376c914d64718ba1c1b53f0e353a91e020e3db5ad510e3a1c6b2cddce1c75be4152b1f5a3e02d9afa7ac932694ecac8253a834133608548850e6039c8939b0c4a23636bb6d2a703da29063dd761acc6";
                break;
            case "M1 SICOM":
                urlFormation= "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200acb5334a16e3f4d32e098e3f55414fa4cb38f03efcebd6987082ba800915ae9121ea780330458f0f0cee4c0d1c89473d9c79089f7816a928cd76a84e98dbcd42ec13126b7d9ac69b0466bc24d5e9bac30e3e08a8";
                break;
            case "M1 ILSEN":
                urlFormation = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200e5fecbb8d6522ce009fe47e6482eacdbcd4017263070dc27fda3903710694af29074cf3241e300ce97cc7f17d9b8c46f59e0451bfd15e710977d94b101229e36e4ed6273977b6376355fa14ae3851dd0a13cecb5";
                        break;
            default:
                break;

        }
        List<VEvent> events = new ArrayList<>();
        try {
            URL url = new URL(urlFormation);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String icsData = new Scanner(connection.getInputStream()).useDelimiter("\\A").next();
                List<ICalendar> calendars = Biweekly.parse(icsData).all();
                for (ICalendar calendar : calendars) {
                    events.addAll(calendar.getEvents());
                }
            } else {
                System.err.println("HTTP error fetching data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }

}


