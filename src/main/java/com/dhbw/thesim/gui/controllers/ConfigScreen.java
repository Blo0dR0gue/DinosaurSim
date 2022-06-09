package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import com.dhbw.thesim.impexp.Json2Objects;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
    @FXML
    public RadioButton auto;
    @FXML
    public RadioButton manual;
    @FXML
    public SliderWithLabel maxSteps;
    @FXML
    public SliderWithLabel maxRuntime;
    @FXML
    public SideBar sideBar;
    @FXML
    public SliderWithLabel stepSliderWithLabel;

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
    public void initialize(ArrayList<Object[]> dinos, ArrayList<Object[]> plants, double plantGrowthRate) {
        //TODO min. 2 dino-arten, min. 1 dino pro art
        //TODO min. 1 pflanzenart, min. 1 pflanze pro art

        populateMapListView();

        populateListView(dinos, dinoListView);

        populateListView(plants, plantListView);

        plantGrowthSliderWithLabel.setValue(plantGrowthRate);

        GridPane.setMargin(maxRuntime.slider, new Insets(40.0,40.0,0.0,0.0));
        GridPane.setMargin(maxSteps.slider, new Insets(40.0,40.0,0.0,0.0));

        initializeListeners();
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    public void initializeListeners() {
        // When the start button is clicked the current scene gets replaced by the SimulationOverlay scene
        startSimulationButton.setOnAction(event -> {
            Stage window = (Stage) startSimulationButton.getScene().getWindow();
            SimulationOverlay simulationOverlay = new SimulationOverlay(window, this);
            window.setScene(simulationOverlay.getSimulationScene());
            window.setFullScreen(true);
        });

        // add a change listener
        modeGroup.selectedToggleProperty().addListener((observableValue, previousSelection, currentSelection) -> {
            maxRuntime.setVisible(currentSelection.equals(auto));
            maxSteps.setVisible(currentSelection.equals(manual));
        });

        maxSteps.slider.valueProperty()
                .addListener((observableValue, oldValue, newValue) -> maxSteps.sliderValueLabel.textProperty().setValue(
                        String.valueOf(Math.round(newValue.doubleValue()/10)*10)
                ));
    }

    /**
     * Method to populate a given list view with all objects from the {@link ArrayList}
     * @param objectsList {@link ArrayList} containing all objects to populate the list {@link ListView} with
     * @param listView The {@link ListView} which should be populated
     */
    private void populateListView(ArrayList<Object[]> objectsList, ListView<ListItem> listView) {
        for (Object[] objects : objectsList) {
            String name = (String) objects[0];
            Image image = SpriteLibrary.getInstance().getImage((String) objects[1]); //TODO fix SpriteLibrary crash when image not found
            int amount = (int) objects[2];

            ListItem listItem = ListItem.newInstance();
            listItem.initialize(name, image, amount, listView);

            listView.getItems().add(listItem);
        }
    }

    /**
     * Method to populate {@link #mapListView} with {@link MapListItem}
     */
    private void populateMapListView() {
        //Toggle group for the map radio buttons, so only one radio button can be active at a time
        mapGroup = new ToggleGroup();
        //Instantiating and initializing maps to add all of them to the list view of dinos
        MapListItem mapListItem1 = MapListItem.newInstance();
        mapListItem1.initialize("Map A", new Image("/map/map-a.png"));
        mapListItem1.radioButton.setToggleGroup(mapGroup);

        MapListItem mapListItem2 = MapListItem.newInstance();
        mapListItem2.initialize("Map B", new Image("/map/map-b.png"));
        mapListItem2.radioButton.setToggleGroup(mapGroup);

        for (MapListItem mapListItem : Arrays.asList(mapListItem1, mapListItem2)) {
            mapListView.getItems().add(mapListItem);
        }
    }
    //region getter & setter
    public HashMap<String, Integer> getDinoParams(){
        HashMap<String, Integer> dinos = new HashMap<>();

        for (ListItem listItem :
                dinoListView.getItems()) {
            dinos.put(listItem.getText(), listItem.getCount());
        }

        return dinos;
    }

    public HashMap<String, Integer> getPlantParams(){
        HashMap<String, Integer> plants = new HashMap<>();

        for (ListItem listItem :
                plantListView.getItems()) {
            plants.put(listItem.getText(), listItem.getCount());
        }

        return plants;
    }

    public RadioButton getMap(){
        return (RadioButton) mapGroup.getSelectedToggle();
    }

    public RadioButton getMode(){
        return (RadioButton) modeGroup.getSelectedToggle();
    }

    public double getPlantGrowthRate(){
        return plantGrowthSliderWithLabel.getValue();
    }

    public double getSimulationSteps(){
        return stepSliderWithLabel.getValue();
    }

    public double getMaxRuntime(){
        return maxRuntime.getValue();
    }

    public double getMaxSteps(){
        return maxSteps.getValue();
    }

    //endregion
}
