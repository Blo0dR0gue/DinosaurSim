package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class ScenarioListItem extends HBox {
    @FXML
    HBox container;
    @FXML
    RadioButton radioButton;

    private String scenarioFileName;

    public ScenarioListItem(){

    }

    public static ScenarioListItem newInstance(){
        return (ScenarioListItem) Display.makeFXMLController("scenario-list-item.fxml", ScenarioListItem.class);
    }

    public void initialize(String name, ToggleGroup toggleGroup){
        scenarioFileName = name.replace(".json", "").replace(".JSON", "");

        //Remove generated filename substring from name
        int scenarioConfigIndex = name.toLowerCase().lastIndexOf("scenarioconfig");
        name = name.toLowerCase().substring(0, scenarioConfigIndex);

        radioButton.setText(name);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setId(name);
    }

    public String getScenarioFileName() {
        return scenarioFileName;
    }
}
