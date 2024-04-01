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

    public List<VEvent> fetchAndParseCalendarData(String filtre) {
        String urlemploi = "";

        switch (filtre.toUpperCase()){
            case "M1 IA":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200ff18615ca376c914d64718ba1c1b53f0e353a91e020e3db5ad510e3a1c6b2cddce1c75be4152b1f5a3e02d9afa7ac932694ecac8253a834133608548850e6039c8939b0c4a23636bb6d2a703da29063dd761acc6";
                break;
            case "M1 SICOM":
                urlemploi= "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200acb5334a16e3f4d32e098e3f55414fa4cb38f03efcebd6987082ba800915ae9121ea780330458f0f0cee4c0d1c89473d9c79089f7816a928cd76a84e98dbcd42ec13126b7d9ac69b0466bc24d5e9bac30e3e08a8";
                break;
            case "M1 ILSEN":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200e5fecbb8d6522ce009fe47e6482eacdbcd4017263070dc27fda3903710694af29074cf3241e300ce97cc7f17d9b8c46f59e0451bfd15e710977d94b101229e36e4ed6273977b6376355fa14ae3851dd0a13cecb5";
                        break;
            case "APPROCHES NEURONALES":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/enseignement/def502002803b785793cfe66032b2c172579dfa6e5c465e7a43ab6f96c1958993ad42c3ade493af48e1a279d60a006e46e670fecb1667ed2676462a74b797e6c3aaa149a7796ed42980f96be248f6ffcbfd6029df19bbc1b3f0adba91bdc";
                break;
            case "PROTOTYPAGE":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/enseignement/def50200f59cd4b3e1ffaa9b081bd8043c473362d784af57360e292489e2519851316b035ca7f27763e1a436f063b35a3407369a7d78437593a26b63c1ded699213389c6c53e66b149743a8797cd3463e6e63cb50a35729e74a5262b6ed0";
                break;
            case "TEST":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/enseignement/def50200d5a585fe86fce5f15db0d114c927f73f29eed56390b3d2bccf98c60049bad713324f60c6dff49b79d6047898e589081b1da0b7bead84ab48c774d3771945f20657b1e593e4278353f4018e18337cdd0477946c9564b93d6afc00";
                break;
            case "STAT 1" :
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/salle/def50200fb1bc63d0b82c20db552072ce772f54ef1c63b0c5d6cf9bd8038c3d572db784ed2db4749ed568ff5f693941d1834a7f48a80f9cc27214b025a3bb5b641524a45ae569500cd92b8593cc3f0afd40e49de20de34eec5d309d9d3c9";
                break;
            case "S2":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/salle/def50200c01b2e4565e22b0f1dc39e0cda61664d8963f56663f78e0f5240bfb8d0747208ce695561f01d594418cf05ee1d3591bfd072751c02c080b27e523b03938f1c10b0f072e2fbe7c31060f8f36a33d43be2e3f90f7698d949" ;
                break;
            case "S3":
                urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/salle/def50200f43d90345455e2128e171ae049aae3332ff10b1488d40baa304b9c8c36ec905ff05e779ebb3e1099c65127a37f62802d4cebc805d6be6b1d4a762f74ba76e6e0bfb8fabda9802419f5da3731ab78fe206da8bafec88153";
                break;

            default:
                break;

        }

        List<VEvent> events = new ArrayList<>();
        try {
            URL url = new URL(urlemploi);
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


