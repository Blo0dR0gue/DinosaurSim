package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

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
    public ListView<ListItemWithImage> dinoListView;
    @FXML
    public GridPane gridPane;
    @FXML
    public ListView<MapListItem> mapListView;
    @FXML
    public ListView<ListItemWithImage> plantListView;
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
     *              {@link Json2Objects#getParamsForGUI(Json2Objects.Type, String)}
     * @param plants The {@link ArrayList} of plant GUI parameters returned by
     *              {@link Json2Objects#getParamsForGUI(Json2Objects.Type, String)}
     * @param plantGrowthRate The plant growth rate returned by
     *              {@link Json2Objects#getParamsForGUI(Json2Objects.Type, String)}
     * @param landscapeName The name of the desired landscape returned by
     *              {@link Json2Objects#getParamsForGUI(Json2Objects.Type, String)}
     */
    public void initialize(ArrayList<Object[]> dinos, ArrayList<Object[]> plants, double plantGrowthRate, String landscapeName) throws IOException {
        addScenarioParams(dinos, plants, plantGrowthRate, landscapeName);

        GridPane.setMargin(maxRuntime.slider, new Insets(40.0,40.0,0.0,0.0));
        GridPane.setMargin(maxSteps.slider, new Insets(40.0,40.0,0.0,0.0));

        ScenarioSelector scenarioSelector = ScenarioSelector.newInstance();
        scenarioSelector.initialize(this);

        sideBar.getBody().add(scenarioSelector);

        initializeListeners();

        Tooltip.install(stepSliderWithLabel,new Tooltip("Dieser Wert spezifiziert, um welchen Faktor die Simulationsgeschwindigkeit erhöht wird."));
        Tooltip.install(plantGrowthSliderWithLabel,new Tooltip("Dieser Wert wird auf alle existierenden Pflanzen angewandt\nund spezifiziert, wie schnell diese wachsen."));
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    public void initializeListeners() {
        // When the start button is clicked the current scene gets replaced by the SimulationOverlay scene
        startSimulationButton.setOnAction(event -> {
            Node node = (Node) event.getSource();
            checkRequiredSimObjects(node);
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
     * Checks if enough dinos as well as enough plants have been configured, otherwide erro alert is shown
     */
    private void checkRequiredSimObjects(Node node) {
        boolean minDinosSelected = true;
        boolean minPlantsSelected = true;
        Stage window = (Stage) node.getScene().getWindow();

        //Iterate over both list views and over each item in them to check if required amount is satisfied
        for (ListView<ListItemWithImage> listView : Arrays.asList(dinoListView, plantListView)) {
            ArrayList<ListItemWithImage> items = new ArrayList<>();
            for (ListItemWithImage listItem : listView.getItems()) {
                if (listItem.getCount() > 0) {
                    items.add(listItem);
                }
            }
            if (items.size() < 2 && listView.equals(dinoListView)) {
                minDinosSelected = false;
            }
            else if (items.size() < 1 && listView.equals(plantListView)) {
                minPlantsSelected = false;
            }
        }
        if (minDinosSelected && minPlantsSelected) {
            try {
                SimulationOverlay simulationOverlay = new SimulationOverlay(window, this);
                window.setScene(simulationOverlay.getSimulationScene());
                window.setFullScreen(true);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehlermeldung");
                alert.setHeaderText("Fehler beim Starten der Simulation!");
                alert.setContentText("Während des Startens der Simulation trat ein unbekannter Fehler auf.");

                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("Stacktrace der Ausnahme:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);
                alert.initOwner(window);

                alert.showAndWait();
            }
        }
        if (!minDinosSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Konfigurationsfehler");
            alert.setHeaderText("Fehler während des Konfigurierens der Simulationsparameter!");
            alert.setContentText("""
                    Sie haben nicht die erforderliche Mindestanzahl von Dinos festgelegt.

                    Zum Starten der Simulation werden mindestens zwei verschiedene Arten von Dinosauriern benötigt.""");
            alert.initOwner(window);

            alert.showAndWait();
        }
        if (!minPlantsSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Konfigurationsfehler");
            alert.setHeaderText("Fehler während des Konfigurierens der Simulationsparameter!");
            alert.setContentText("""
                    Sie haben nicht die erforderliche Mindestanzahl von Pflanzen festgelegt.

                    Zum Starten der Simulation wird mindestens eine Art von Pflanzen benötigt.""");
            alert.initOwner(window);

            alert.showAndWait();
        }
    }

    /**
     * Method to load all of the params from the specified scenario config to load
     * @param dinos All of the listed dinos listed retrieved from scenario config
     * @param plants All of the listed plants listed retrieved from scenario config
     * @param plantGrowthRate The given plant growth rate in scenario config
     * @param landscapeName The given landscape name in scenario config
     */
    public void addScenarioParams(ArrayList<Object[]> dinos, ArrayList<Object[]> plants, double plantGrowthRate, String landscapeName) throws IOException {
        mapListView.getItems().clear();
        populateMapListView(landscapeName);

        dinoListView.getItems().clear();
        populateListView(dinos, dinoListView, JsonHandler.SimulationObjectType.DINO);

        plantListView.getItems().clear();
        populateListView(plants, plantListView, JsonHandler.SimulationObjectType.PLANT);

        plantGrowthSliderWithLabel.setValue(plantGrowthRate);
    }

    /**
     * Method to populate a given list view with all objects from the {@link ArrayList}
     * @param objectsList {@link ArrayList} containing all objects to populate the list {@link ListView} with
     * @param listView The {@link ListView} which should be populated
     */
    private void populateListView(ArrayList<Object[]> objectsList, ListView<ListItemWithImage> listView, JsonHandler.SimulationObjectType type) throws IOException {
        for (Object[] objects : objectsList) {
            String name = (String) objects[0];
            Image image = SpriteLibrary.getInstance().getImage((String) objects[1]);
            int amount = (int) objects[2];

            ListItemWithImage listItem = ListItemWithImage.newInstance();
            listItem.initialize(name, image, amount, listView, type);

            listView.getItems().add(listItem);
        }
    }

    /**
     * Method to populate {@link #mapListView} with {@link MapListItem}
     */
    private void populateMapListView(String landscapeName) {
        //Toggle group for the map radio buttons, so only one radio button can be active at a time
        mapGroup = new ToggleGroup();
        //Instantiating and initializing maps to add all of them to the list view of dinos
        MapListItem mapListItem1 = MapListItem.newInstance();
        String mapName1 = "Landschaft 1";
        mapListItem1.initialize(mapName1, new Image("/map/landscape-one.png"));
        mapListItem1.radioButton.setId(mapName1.replace(" ", ""));
        mapListItem1.radioButton.setToggleGroup(mapGroup);

        MapListItem mapListItem2 = MapListItem.newInstance();
        String mapName2 = "Landschaft 2";
        mapListItem2.initialize(mapName2, new Image("/map/landscape-two.png"));
        mapListItem2.radioButton.setId(mapName2.replace(" ", ""));
        mapListItem2.radioButton.setToggleGroup(mapGroup);

        //Add the map list items to the map list view
        for (MapListItem mapListItem : Arrays.asList(mapListItem1, mapListItem2)) {
            mapListView.getItems().add(mapListItem);
        }

        //Set toggle for landscape from config file as selected (search by text and id of radio button)
        for (Toggle toggle: mapGroup.getToggles()) {
            if (Objects.equals(((RadioButton) toggle).getText(), landscapeName)
                    || Objects.equals(((RadioButton) toggle).getId(), landscapeName)) {
                toggle.setSelected(true);
                break;
            }
        }
    }

    //region getter & setter
    public HashMap<String, Integer> getDinoParams(){
        return getListItemParams(dinoListView);
    }

    public HashMap<String, Integer> getPlantParams(){
        return getListItemParams(plantListView);
    }

    private HashMap<String, Integer> getListItemParams(ListView<ListItemWithImage> listView){
        HashMap<String, Integer> items = new HashMap<>();

        for (ListItemWithImage listItem :
                listView.getItems()) {
            items.put(listItem.getText(), listItem.getCount());
        }

        return items;
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

    public String getLandscapeName() {
        return getMap().getText();
    }
    //endregion
}
