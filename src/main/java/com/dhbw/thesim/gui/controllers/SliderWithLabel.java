package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class SliderWithLabel extends GridPane {
    @FXML
    public GridPane gridPane;
        @FXML
        public Label label;
        @FXML
        public Slider slider;
        @FXML
        public Label sliderValueLabel;

    public SliderWithLabel() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/slider-with-label.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        initializeListeners();
    }

    public static SliderWithLabel newInstance() {
        return (SliderWithLabel) Display.makeFXMLController("slider-with-label.fxml", SliderWithLabel.class);
    }

    public void initialize() {
        initializeListeners();
    }

    private void initializeListeners() {
        //Add change listener on the slider control to reflect its current value in the label
        addListener(slider, sliderValueLabel);
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

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }
}
