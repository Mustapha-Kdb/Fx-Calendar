package com.example.fxcalendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;


public class PlanningController {

    @FXML
    private TableView<?> tableView;

    public void initialize() {
        loadEventsfromAPI();

    }

    private void loadEventsfromAPI() {
        //load from string
        String s ;



    }

    public void handleExportAction(ActionEvent actionEvent) {
    }
}


