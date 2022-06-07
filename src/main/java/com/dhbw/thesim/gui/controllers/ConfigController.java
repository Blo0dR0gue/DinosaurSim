package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Controller Class for the Configuration Screen FXML file
 * @author Robin Khatri Chetri
 */
public class ConfigController implements Initializable {
    @FXML
    public Button startSimulationButton;
    @FXML
    public AnchorPane container;
    @FXML
    public Label stepLabel;
    @FXML
    public Slider stepSlider;
    @FXML
    public Slider growthRateSlider;
    @FXML
    public Label growthRateLabel;
    @FXML
    public ToggleGroup mapGroup;
    @FXML
    public ToggleGroup modeGroup;
    @FXML
    public Slider rexSlider;
    @FXML
    public Label rexLabel;
    @FXML
    public Slider raptorSlider;
    @FXML
    public Label raptorLabel;
    @FXML
    public Slider triSlider;
    @FXML
    public Label triLabel;
    @FXML
    public Slider brontoSlider;
    @FXML
    public Label brontoLabel;

    /**
     * When the start button is clicked the current scene gets replaced by the {@link SimulationOverlay} scene
     */
    @FXML
    protected void onStartSimulationButtonClick() {
        Stage window = (Stage) startSimulationButton.getScene().getWindow();
        SimulationOverlay simulationOverlay = new SimulationOverlay(window);
        window.setScene(simulationOverlay.getSimulationScene());
        window.setFullScreen(true);
    }

    /**
     * Used to create a change listener on the given {@code slider} to update the corresponding {@code label}
     * @param slider The {@link Slider} node which the listener gets added to
     * @param label The {@link Label} node which will be updated with the current value of the Slider as it is being dragged
     */
    private void addListener(Slider slider, Label label) {
        slider.valueProperty()
                .addListener((observableValue, oldValue, newValue) -> label.textProperty().setValue(
                        String.valueOf(newValue.intValue())
                ));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Add change listeners on the slider controls to reflect their current values in the label
        addListener(rexSlider, rexLabel);
        addListener(raptorSlider, raptorLabel);
        addListener(triSlider, triLabel);
        addListener(brontoSlider, brontoLabel);
        addListener(growthRateSlider, growthRateLabel);
        addListener(stepSlider, stepLabel);
    }
}
