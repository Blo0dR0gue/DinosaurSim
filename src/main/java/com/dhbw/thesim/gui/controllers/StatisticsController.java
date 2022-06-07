package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Controller Class for the End Screen FXML file
 * @author Robin Khatri Chetri
 */
public class StatisticsController implements Initializable {
    @FXML
    public Button restartSimulationButton;
    @FXML
    protected void onRestartSimulationButtonClick() {
        Stage window = (Stage) restartSimulationButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/StartSimulation.fxml"));

        try {
            Scene configScene = new Scene(fxmlLoader.load());
            window.setScene(configScene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        window.setFullScreen(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
