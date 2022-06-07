package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Controller Class for the End Screen FXML file
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class StatisticsEndcard extends AnchorPane {
    @FXML
    public Button restartSimulationButton;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public StatisticsEndcard() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ConfigScreen}
     * @return The newly created and initialized {@link ConfigScreen}
     */
    public static StatisticsEndcard newInstance() {
        return (StatisticsEndcard) Display.makeFXMLController("statistics-endcard.fxml", StatisticsEndcard.class);
    }

    /**
     * Method to initialize the Statistics Endcard screen, its listeners and adding custom controls dynamically
     */
    public void initialize() {
        initializeListeners();
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    private void initializeListeners() {
        restartSimulationButton.setOnAction(event -> {
            Stage window = (Stage) restartSimulationButton.getScene().getWindow();
            ConfigScreen configScreen = ConfigScreen.newInstance();
            window.setScene(new Scene(configScreen));
            window.setFullScreen(true);
        });
    }
}
