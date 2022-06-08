package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import com.dhbw.thesim.impexp.Json2Objects;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    public ListView<ListItem> dinoListView;
    @FXML
    public GridPane gridPane;
    @FXML
    public ListView<MapListItem> mapListView;
    @FXML
    public ListView<ListItem> plantListView;

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
     * @param dinos The {@link ArrayList} of dino GUI parameters returned by
     * {@link com.dhbw.thesim.impexp.Json2Objects#getParamsForGUI(Json2Objects.Type, String)}
     */
    public void initialize(ArrayList<Object[]> dinos) {
        initializeListeners();

        //TODO min. 2 dino-arten, min. 1 dino pro art
        //TODO min. 1 pflanzenart, min. 1 pflanze pro art

        for (Object[] dino :
                dinos) {
            String name = (String) dino[0];
            //Image image = SpriteLibrary.getInstance().getImage((String) dino[1]); //TODO fix SpriteLibrary crash when image not found
            Image image = new Image("/dinosaur/test.png");
            int amount = (int) dino[2];

            ListItem dinoListItem = ListItem.newInstance();
            dinoListItem.initialize(name, image, amount, dinoListView);

            dinoListView.getItems().add(dinoListItem);
        }

        GridPane.setFillWidth(dinoListView, true);

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

        //Instantiating and initializing maps to add all of them to the list view of dinos
        ListItem plantListItem1 = ListItem.newInstance();
        plantListItem1.initialize("Farn", new Image("/plant/farn.png"), 2, plantListView);

        ListItem plantListItem2 = ListItem.newInstance();
        plantListItem2.initialize("Ginkgoales", new Image("/plant/plantTest.png"), 2, plantListView);

        GridPane.setFillWidth(dinoListView, true);

        for (ListItem plantListItem : Arrays.asList(plantListItem1, plantListItem2)) {
            plantListView.getItems().add(plantListItem);
        }
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    public void initializeListeners() {
        ConfigScreen configScreen = this;
        // When the start button is clicked the current scene gets replaced by the SimulationOverlay scene
        startSimulationButton.setOnAction(event -> {
            Stage window = (Stage) startSimulationButton.getScene().getWindow();
            SimulationOverlay simulationOverlay = new SimulationOverlay(window, configScreen);
            window.setScene(simulationOverlay.getSimulationScene());
            window.setFullScreen(true);
        });
    }

    public HashMap<String, Integer> getDinoParams(){
        HashMap<String, Integer> dinos = new HashMap<>();

        for (ListItem listItem :
                dinoListView.getItems()) {
            dinos.put(listItem.getText(), listItem.getCount());
        }

        return dinos;
    }


    public int getPlantGrowthRate(){
        return (int) plantGrowthSliderWithLabel.getValue();
    }
}
