package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Controller Class for the End Screen FXML file
 * @author Robin Khatri Chetri
 */
public class StatisticsEndcard extends AnchorPane {
    @FXML
    public Button restartSimulationButton;

    public StatisticsEndcard() {

    }

    public static StatisticsEndcard newInstance() {
        return (StatisticsEndcard) Display.makeFXMLController("statistics-endcard.fxml", StatisticsEndcard.class);
    }

    public void initialize() {
        initializeListeners();
    }

    private void initializeListeners() {
        restartSimulationButton.setOnAction(event -> {
            Stage window = (Stage) restartSimulationButton.getScene().getWindow();
            ConfigScreen configScreen = ConfigScreen.newInstance();
            window.setScene(new Scene(configScreen));
            window.setFullScreen(true);
        });
    }
}
