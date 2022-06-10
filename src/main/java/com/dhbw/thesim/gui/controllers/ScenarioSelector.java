package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

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

    public ScenarioSelector(){

    }

    public static ScenarioSelector newInstance(){
        return (ScenarioSelector) Display.makeFXMLController("ScenarioSelector.fxml", ScenarioSelector.class);
    }

    public void initialize(){

    }
}
