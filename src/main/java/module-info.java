module com.example.fxcalendar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires jfxtras.agenda;
    requires java.net.http;
    requires org.mnode.ical4j.core;
    requires biweekly;

    opens com.example.fxcalendar to javafx.fxml;
    exports com.example.fxcalendar;
    exports com.example.fxcalendar.Vue;
    opens com.example.fxcalendar.Vue to javafx.fxml;
    exports com.example.fxcalendar.Controleur;
    opens com.example.fxcalendar.Controleur to javafx.fxml;
}