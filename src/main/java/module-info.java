module com.example.fxcalendar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.mnode.ical4j.core;
    requires jfxtras.icalendarfx;

    opens com.example.fxcalendar to javafx.fxml;
    exports com.example.fxcalendar;
}