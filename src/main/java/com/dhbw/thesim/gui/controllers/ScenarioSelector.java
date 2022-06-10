package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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

    private ToggleGroup scenarioToggleGroup;

    public ScenarioSelector(){

    }

    public static ScenarioSelector newInstance(){
        return (ScenarioSelector) Display.makeFXMLController("ScenarioSelector.fxml", ScenarioSelector.class);
    }

    public void initialize(ArrayList<String> existingScenarioFileNames){
        this.scenarioToggleGroup = new ToggleGroup();
        for (String scenarioName:
                existingScenarioFileNames) {
            ScenarioListItem scenarioListItem = ScenarioListItem.newInstance();
            scenarioListItem.initialize(scenarioName, scenarioToggleGroup);
            scenarioListView.getItems().add(scenarioListItem);
        }
    }

    public String getSelectedScenario(){
        return ((ScenarioListItem) scenarioToggleGroup.getSelectedToggle()).getScenarioFileName();
    }
}
