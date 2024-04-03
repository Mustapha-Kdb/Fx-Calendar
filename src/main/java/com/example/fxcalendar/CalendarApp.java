package com.example.fxcalendar;

import com.example.fxcalendar.Controleur.CalendarController;
import com.example.fxcalendar.Controleur.LoginController;
import com.example.fxcalendar.Modele.UserModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class CalendarApp extends Application {
    private static final String LIGHT_THEME = "styles/light-theme.css";
    private static final String DARK_THEME = "styles/dark-theme.css";

    private Stage primaryStage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadLoginView();
    }

    public void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setApp(this);

            scene = new Scene(root);
            setTheme(scene, LIGHT_THEME);

            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCalendarView(UserModel user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarView.fxml"));
            Parent root = loader.load();
            loader.<CalendarController>getController().initialize(user);
            String theme=user.getTheme();
            String formation=user.getFormation();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            scene.setRoot(root);
            scene.getWindow().setWidth(screenBounds.getWidth());
            scene.getWindow().setHeight(screenBounds.getHeight());

            setTheme(scene, "DARK".equals(theme) ? DARK_THEME : LIGHT_THEME); // Exemple de basculement basé sur le thème

            primaryStage.setTitle("University Calendar - " + formation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(theme).toExternalForm());
    }


    public static void main(String[] args) {
        launch(args);
    }
    //on close enregistre le theme dans le fichier json
}
