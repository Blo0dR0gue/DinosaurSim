package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * The Custom Control Class provides a scenario list item for the {@link javafx.scene.control.ListView} of scenarios
 *
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class ScenarioListItem extends HBox {
    @FXML
    HBox container;
    @FXML
    RadioButton radioButton;

    private String scenarioFileName;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public ScenarioListItem() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ScenarioListItem}
     *
     * @return The newly created and initialized {@link ScenarioListItem}
     */
    public static ScenarioListItem newInstance() {
        return (ScenarioListItem) Display.makeFXMLController("scenario-list-item.fxml", ScenarioListItem.class);
    }

    /**
     * Method to initialize the List item and setting the text (removing substring beforehand), id and toggle group.
     */
    public void initialize(String name, ToggleGroup toggleGroup) {
        scenarioFileName = name.replace(".json", "").replace(".JSON", "");

        //Remove generated filename substring from name
        int scenarioConfigIndex = name.toLowerCase().lastIndexOf("scenarioconfig");
        name = name.substring(0, scenarioConfigIndex);

        radioButton.setText(name);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setId(name);
    }

    //region getter & setter
    public String getScenarioFileName() {
        return scenarioFileName;
    }
    //endregion
}
