package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

/**
 * The Control CLass for the Configuration Screen FXML file
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class ConfigScreen extends AnchorPane {
    @FXML
    public Button startSimulationButton;
    @FXML
    public AnchorPane container;
    @FXML
    public Label stepLabel;
    @FXML
    public Slider stepSlider;
    @FXML
    public ToggleGroup mapGroup;
    @FXML
    public ToggleGroup modeGroup;
    @FXML
    public Slider rexSlider;
    @FXML
    public Label rexLabel;
    @FXML
    public Slider raptorSlider;
    @FXML
    public Label raptorLabel;
    @FXML
    public Slider triSlider;
    @FXML
    public Label triLabel;
    @FXML
    public Slider brontoSlider;
    @FXML
    public Label brontoLabel;
    @FXML
    public SliderWithLabel plantGrowthSliderWithLabel;
    @FXML
    public ListView<DinoListItem> dinoListView;
    @FXML
    public GridPane gridPane;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public ConfigScreen() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ConfigScreen}
     * @return The newly created and initialized {@link ConfigScreen}
     */
    public static ConfigScreen newInstance() {
        return (ConfigScreen) Display.makeFXMLController("config-screen.fxml", ConfigScreen.class);
    }

    /**
     * Method to initialize the Configuration Screen, its listeners and adding custom controls dynamically
     */
    public void initialize() {
        initializeListeners();
        //adding dinos to list
        DinoListItem dinoListItem1 = DinoListItem.newInstance();
        dinoListItem1.initialize("Tyrannosaurus Rex", new Image("/dinosaur/t-rex.png"));

        DinoListItem dinoListItem2 = DinoListItem.newInstance();
        dinoListItem2.initialize("Oviraptor", new Image("/dinosaur/oviraptor.png"));

        DinoListItem dinoListItem3 = DinoListItem.newInstance();
        dinoListItem3.initialize("Triceratops", new Image("/dinosaur/triceratops.png"));

        DinoListItem dinoListItem4 = DinoListItem.newInstance();
        dinoListItem4.initialize("Brontosaurus", new Image("/dinosaur/brontosaurus.png"));

        GridPane.setFillWidth(dinoListView, true);

        for (DinoListItem dinoListItem : Arrays.asList(dinoListItem1, dinoListItem2, dinoListItem3, dinoListItem4)) {
            dinoListView.getItems().add(dinoListItem);
        }
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    public void initializeListeners() {
        // When the start button is clicked the current scene gets replaced by the SimulationOverlay scene
        startSimulationButton.setOnAction(event -> {
            Stage window = (Stage) startSimulationButton.getScene().getWindow();
            SimulationOverlay simulationOverlay = new SimulationOverlay(window);
            window.setScene(simulationOverlay.getSimulationScene());
            window.setFullScreen(true);
        });
    }
}
