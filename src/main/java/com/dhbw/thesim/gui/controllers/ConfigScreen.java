package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
    public SliderWithLabel plantGrowthSliderWithLabel;
    @FXML
    public ListView<DinoListItem> dinoListView;
    @FXML
    public GridPane gridPane;
    @FXML
    public ListView<MapListItem> mapListView;

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

        //TODO min. 2 dino-arten, min. 1 dino pro art
        //TODO min. 1 pflanzenart, min. 1 pflanze pro art

        //Instantiating and initializing dinos to add all of them to the list view of dinos
        DinoListItem dinoListItem1 = DinoListItem.newInstance();
        dinoListItem1.initialize("Tyrannosaurus Rex", new Image("/dinosaur/t-rex.png"), dinoListView);

        DinoListItem dinoListItem2 = DinoListItem.newInstance();
        dinoListItem2.initialize("Oviraptor", new Image("/dinosaur/oviraptor.png"), dinoListView);

        DinoListItem dinoListItem3 = DinoListItem.newInstance();
        dinoListItem3.initialize("Triceratops", new Image("/dinosaur/triceratops.png"), dinoListView);

        DinoListItem dinoListItem4 = DinoListItem.newInstance();
        dinoListItem4.initialize("Brontosaurus", new Image("/dinosaur/brontosaurus.png"), dinoListView);

        GridPane.setFillWidth(dinoListView, true);

        for (DinoListItem dinoListItem : Arrays.asList(dinoListItem1, dinoListItem2, dinoListItem3, dinoListItem4)) {
            dinoListView.getItems().add(dinoListItem);
        }

        //Toggle group for the map radio buttons, so only one radio button can be active at a time
        ToggleGroup mapGroup = new ToggleGroup();

        //Instantiating and initializing maps to add all of them to the list view of dinos
        MapListItem mapListItem1 = MapListItem.newInstance();
        mapListItem1.initialize("Map A", new Image("/map/map-a.png"));
        mapListItem1.radioButton.setToggleGroup(mapGroup);

        MapListItem mapListItem2 = MapListItem.newInstance();
        mapListItem2.initialize("Map B", new Image("/map/map-b.png"));
        mapListItem2.radioButton.setToggleGroup(mapGroup);

        GridPane.setFillWidth(dinoListView, true);

        for (MapListItem mapListItem : Arrays.asList(mapListItem1, mapListItem2)) {
            mapListView.getItems().add(mapListItem);
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
