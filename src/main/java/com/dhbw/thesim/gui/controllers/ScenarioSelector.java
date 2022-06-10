package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ScenarioSelector extends VBox {
    @FXML
    VBox container;
    @FXML
    Label chooseLabel;
    @FXML
    ListView scenarioListView;
    @FXML
    Label saveLabel;
    @FXML
    Button saveButton;
    @FXML
    TextField filename;

    private ToggleGroup scenarioToggleGroup;

    private ConfigScreen configScreen;

    public ScenarioSelector(){

    }

    public static ScenarioSelector newInstance(){
        return (ScenarioSelector) Display.makeFXMLController("ScenarioSelector.fxml", ScenarioSelector.class);
    }

    public void initialize(ConfigScreen configScreen){
        this.scenarioToggleGroup = new ToggleGroup();
        try {
            for (String scenarioName:
                    JsonHandler.getExistingScenarioFileNames()) {
                ScenarioListItem scenarioListItem = ScenarioListItem.newInstance();
                scenarioListItem.initialize(scenarioName, scenarioToggleGroup);
                scenarioListView.getItems().add(scenarioListItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.configScreen = configScreen;

        initializeListeners();
    }

    private void updateScenarioList(){
        scenarioListView.getItems().clear();
        try {
            for (String scenarioName:
                    JsonHandler.getExistingScenarioFileNames()) {
                ScenarioListItem scenarioListItem = ScenarioListItem.newInstance();
                scenarioListItem.initialize(scenarioName, scenarioToggleGroup);
                scenarioListView.getItems().add(scenarioListItem);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeListeners(){
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String file = filename.getText().strip();
                if (!file.equals("")) {
                    try {
                        JsonHandler.exportScenarioConfig(configScreen.getDinoParams(), configScreen.getPlantParams(),
                                configScreen.getLandscapeName(), configScreen.getPlantGrowthRate(), file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateScenarioList();
                } else {
                    //TODO display error of empty filename
                }
            }
        });
    }

    public String getSelectedScenario(){
        return ((ScenarioListItem) scenarioToggleGroup.getSelectedToggle()).getScenarioFileName();
    }
}
