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
            String urlemploi = getUrlBasedOnFilter(filtre);

            List<VEvent> events = new ArrayList<>();
            try {
                // Vérifiez si l'URL est valide avant de continuer
                if (urlemploi == null || urlemploi.isEmpty()) {
                    System.err.println("L'URL est invalide ou manquante pour le filtre: " + filtre);
                    return events;
                }

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

        private String getUrlBasedOnFilter(String filtre) {
            // Cette méthode retourne l'URL basée sur le filtre donné
            String urlemploi = "";

            switch (filtre.toUpperCase()){
                case "M1 IA CLA":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200ff18615ca376c914d64718ba1c1b53f0e353a91e020e3db5ad510e3a1c6b2cddce1c75be4152b1f5a3e02d9afa7ac932694ecac8253a834133608548850e6039c8939b0c4a23636bb6d2a703da29063dd761acc6";
                    break;
                case "M1 IA":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200ff18615ca376c914d64718ba1c1b53f0e353a91e020e3db5ad510e3a1c6b2cddce1c75be4152b1f5a3e02d9afa7ac932694ecac8253a834133608548850e6039c8939b0c4a23636bb6d2a703da29063dd761acc6";
                    break;
                case "M1 IA ALT":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200a6feb5384bf725a47174fccbee30c1ba4efacc8b80a24085d50e033a93dc239b8f518cc0302da065e2d2272d41cc8c0cc3b00784856916234e5f3bf0cd4ec797bc54a7d116a5b759ca88e504796207c01fbccbae";
                    break;
                case "M1 SICOM CLA":
                    urlemploi= "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200acb5334a16e3f4d32e098e3f55414fa4cb38f03efcebd6987082ba800915ae9121ea780330458f0f0cee4c0d1c89473d9c79089f7816a928cd76a84e98dbcd42ec13126b7d9ac69b0466bc24d5e9bac30e3e08a8";
                    break;
                case "M1 SICOM":
                    urlemploi= "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200acb5334a16e3f4d32e098e3f55414fa4cb38f03efcebd6987082ba800915ae9121ea780330458f0f0cee4c0d1c89473d9c79089f7816a928cd76a84e98dbcd42ec13126b7d9ac69b0466bc24d5e9bac30e3e08a8";
                    break;
                case "M1 SICOM ALT":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200a1bc27602f662bb59b19f83737bba4db65d5ce5ec46e1b3e0ee7f2ffdafa16564b15cafad214d7ebe8468d1a915429c9a37bdc803578092262648e8990b7724588e535426e41546161c61acc22750faa359b0c71";
                    break;

                case "M1 ILSEN CLA":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200e5fecbb8d6522ce009fe47e6482eacdbcd4017263070dc27fda3903710694af29074cf3241e300ce97cc7f17d9b8c46f59e0451bfd15e710977d94b101229e36e4ed6273977b6376355fa14ae3851dd0a13cecb5";
                    break;
                case "M1 ILSEN":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200e5fecbb8d6522ce009fe47e6482eacdbcd4017263070dc27fda3903710694af29074cf3241e300ce97cc7f17d9b8c46f59e0451bfd15e710977d94b101229e36e4ed6273977b6376355fa14ae3851dd0a13cecb5";
                    break;
                case "M1 ILSEN ALT":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/tdoption/def50200bfbf7405961f26baa8fa1f6ac8d0251b3731fab26da699ae517206a1ea7980a8f08c1663bf68a7a10477e06f1c3eb9b19c713223a18c5c6fe497f5ee06a13bb78101d6c14f751328cb8e47078cabae562778b41c";
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
                case "NOE":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/enseignant/def5020014cf744f63f7181931e243c5139c5d8427de488f3da5b30b52905edfe9de85e8da750e291f852c095f6fd05f93658cbbf3260bf1308a84c444accdb9ab8f67de5f5758e0b59200e3c78068a677fc5055644c4635";
                    break;
                case "NOE CLA":
                    urlemploi = "https://edt-api.univ-avignon.fr/api/exportAgenda/enseignant/def5020014cf744f63f7181931e243c5139c5d8427de488f3da5b30b52905edfe9de85e8da750e291f852c095f6fd05f93658cbbf3260bf1308a84c444accdb9ab8f67de5f5758e0b59200e3c78068a677fc5055644c4635";
                    break;

                default:
                    break;
            }
            return urlemploi;
        }

}


