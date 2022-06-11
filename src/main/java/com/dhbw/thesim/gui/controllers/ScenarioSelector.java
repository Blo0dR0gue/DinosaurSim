package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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

    public ScenarioSelector(){

    }

    public static ScenarioSelector newInstance(){
        return (ScenarioSelector) Display.makeFXMLController("scenario-selector.fxml", ScenarioSelector.class);
    }

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

    private void initializeListeners(){
        saveButton.setOnAction(event -> {
            String file = filename.getText().strip();
            if (!file.equals("")) {
                try {
                    JsonHandler.exportScenarioConfig(configScreen.getDinoParams(), configScreen.getPlantParams(),
                            configScreen.getLandscapeName(), configScreen.getPlantGrowthRate(), file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateScenarioList(file);
                filename.clear();
            } else {
                //TODO display error of empty filename
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Szenarioerstellungsfehler");
                alert.setHeaderText("Fehler während des Erstellens der Szenario-Konfiguration!");
                alert.setContentText("Bitte geben Sie einen Namen für die Szenario-Konfigurationsdatei ein");

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String getSelectedScenario() {
        return ((ScenarioListItem) ((RadioButton) scenarioToggleGroup.getSelectedToggle()).getParent()).getScenarioFileName();
    }
}
