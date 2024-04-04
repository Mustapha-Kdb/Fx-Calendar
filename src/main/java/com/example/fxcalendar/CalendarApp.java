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



            
            if (scene == null) {
                scene = new Scene(root);
                primaryStage.setScene(scene);
            } else {
                
                scene.setRoot(root);
            }

            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);

            primaryStage.setWidth(400); 
            primaryStage.setHeight(400); 

            setTheme(scene, LIGHT_THEME); 

            primaryStage.setTitle("Login");
            primaryStage.centerOnScreen(); 
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
            loader.<CalendarController>getController().setApp(this);
            String theme=user.getTheme();
            String formation=user.getFormation();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            scene.setRoot(root);
            //scene.setMaximized(true);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(true);

            setTheme(scene, "DARK".equals(theme) ? DARK_THEME : LIGHT_THEME); 

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
}
