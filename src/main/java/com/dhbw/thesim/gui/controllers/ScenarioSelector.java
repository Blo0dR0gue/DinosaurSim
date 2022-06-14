package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The Custom Control Class provides a scenario selector area including the scenario list and save and load button
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class ScenarioSelector extends VBox {
    @FXML
    VBox container;
    @FXML
    Label chooseLabel;
    @FXML
    ListView<ScenarioListItem> scenarioListView;
    @FXML
    Label saveLabel;
    @FXML
    Button saveButton;
    @FXML
    TextField filename;
    @FXML
    public Text promptText;
    @FXML
    public Button loadButton;

    private ToggleGroup scenarioToggleGroup;

    private ConfigScreen configScreen;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public ScenarioSelector(){

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ScenarioSelector}
     * @return The newly created and initialized {@link ScenarioSelector}
     */
    public static ScenarioSelector newInstance(){
        return (ScenarioSelector) Display.makeFXMLController("scenario-selector.fxml", ScenarioSelector.class);
    }

    /**
     * Method to initialize the scenario selector, its listeners and initializing the list of scenario configs
     * Also a tooltip is set for additional information to dinosaur and/ or plant species.
     */
    public void initialize(ConfigScreen configScreen){
        this.scenarioToggleGroup = new ToggleGroup();
        try {
            for (String scenarioName:
                    JsonHandler.getExistingScenarioFileNames()) {
                ScenarioListItem scenarioListItem = ScenarioListItem.newInstance();
                scenarioListItem.initialize(scenarioName, scenarioToggleGroup);
                scenarioListView.getItems().add(scenarioListItem);
                RadioButton scenarioRadioButton = scenarioListItem.radioButton;
                if (Objects.equals(scenarioRadioButton.getText(), "default")) {
                    scenarioRadioButton.setSelected(true);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.configScreen = configScreen;

        initializeListeners();
    }

    /**
     * Updates the scenario list with all exisiting scenario configs
     * @param fileName The file name which is display as the text for the radio button
     */
    private void updateScenarioList(String fileName){
        scenarioListView.getItems().clear();
        try {
            for (String scenarioName:
                    JsonHandler.getExistingScenarioFileNames()) {
                ScenarioListItem scenarioListItem = ScenarioListItem.newInstance();
                scenarioListItem.initialize(scenarioName, scenarioToggleGroup);
                RadioButton scenarioRadioButton = scenarioListItem.radioButton;
                if (Objects.equals(scenarioRadioButton.getText(), fileName)) {
                    scenarioRadioButton.setSelected(true);
                }
                scenarioListView.getItems().add(scenarioListItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
    private void initializeListeners(){
        saveButton.setOnAction(event -> {
            String file = filename.getText().replace(" ","");
            if (!file.equals("")) {
                try {
                    JsonHandler.exportScenarioConfig(configScreen.getDinoParams(), configScreen.getPlantParams(),
                            configScreen.getLandscapeName(), configScreen.getPlantGrowthRate(), file);

                    Stage window = (Stage) loadButton.getScene().getWindow();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Szenarioerstellung");
                    alert.setHeaderText(String.format("Die Szenario-Konfigurationsdatei %s.json wurde erfolgreich gespeichert!", file));
                    alert.setContentText(String.format("Die Parameter der aktuellen Konfiguration wurden gespeichert unter: %s/%s.json", JsonHandler.getWorkingDirectory(), file));
                    alert.initOwner(window);

                    alert.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateScenarioList(file);
                filename.clear();
            } else {
                Stage window = (Stage) saveButton.getScene().getWindow();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Szenarioerstellungsfehler");
                alert.setHeaderText("Fehler während des Erstellens der Szenario-Konfiguration!");
                alert.setContentText("Bitte geben Sie einen Namen für die Szenario-Konfigurationsdatei ein");
                alert.initOwner(window);

                alert.showAndWait();
            }
        });

        loadButton.setOnAction(event -> {
            String selectedScenario = getSelectedScenario();
            try {
                HashMap<JsonHandler.ScenarioConfigParams, ArrayList<Object[]>> scenarioParams = Json2Objects.getParamsForGUI(Json2Objects.Type.WITH_SCENARIO_FILE, selectedScenario);
                configScreen.addScenarioParams(scenarioParams.get(JsonHandler.ScenarioConfigParams.DINO),
                        scenarioParams.get(JsonHandler.ScenarioConfigParams.PLANT),
                        (double) scenarioParams.get(JsonHandler.ScenarioConfigParams.PLANT_GROWTH).get(0)[0],
                        (String) scenarioParams.get(JsonHandler.ScenarioConfigParams.LANDSCAPE).get(0)[0]);

                Stage window = (Stage) loadButton.getScene().getWindow();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Konfigurationswechsel");
                alert.setHeaderText("Die ausgewählte Szenario-Konfigurationsdatei wurde erfolgreich geladen!");
                alert.setContentText("Die Parameter der ausgewählten Szenario-Konfiguration wurden geladen " +
                        "und werden nun im Konfigurationsmenü angezeigt.");
                alert.initOwner(window);

                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        EventHandler<KeyEvent> handler = new EventHandler<>() {
            private boolean willConsume = false;

            @Override
            public void handle(KeyEvent event) {

                if (willConsume) {
                    event.consume();
                }

                if (event.getCode().isWhitespaceKey()) {
                    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                        willConsume = true;
                    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                        willConsume = false;
                    }
                }
            }
        };

        filename.addEventFilter(KeyEvent.ANY, handler);
    }

    //region getter & setter
    /**
     * Retrieves the file name of the currently selected scenario config
     * @return The {@link String} containing the file name of the selected scenario
     */
    public String getSelectedScenario() {
        return ((ScenarioListItem) ((RadioButton) scenarioToggleGroup.getSelectedToggle()).getParent()).getScenarioFileName();
    }
    //endregion
}
