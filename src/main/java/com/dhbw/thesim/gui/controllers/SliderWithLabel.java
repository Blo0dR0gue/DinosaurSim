package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * The Custom Control Class provides a {@link Slider} and {@link Label} controls
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class SliderWithLabel extends GridPane {
    @FXML
    public GridPane gridPane;
        @FXML
        public Label label;
        @FXML
        public Slider slider;
        @FXML
        public Label sliderValueLabel;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
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

    /**
     * This method creates and initializes a new instance of from the FXML {@link SliderWithLabel}
     * @return The newly created and initialized {@link SliderWithLabel}
     */
    public static SliderWithLabel newInstance() {
        return (SliderWithLabel) Display.makeFXMLController("slider-with-label.fxml", SliderWithLabel.class);
    }

    /**
     * Method to initialize the Slider with corresponding labels, its listeners and setting the label text
     */
    public void initialize(String labelText) {
        setText(labelText);
        initializeListeners();
    }

    /**
     * Adds all specified event handlers to the specified {@link javafx.scene.Node}
     */
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

    public double getValue() {
        return valueProperty().doubleValue();
    }

    public void setValue(double value){
        valueProperty().set(value);
    }

    public DoubleProperty valueProperty() {
        return slider.valueProperty();
    }

    public double getMax() {
        return maxValProperty().doubleValue();
    }

    public void setMax(double value) {
        maxValProperty().set(value);
    }

    public DoubleProperty maxValProperty() {
        return slider.maxProperty();
    }

    public double getMin() {
        return minValProperty().doubleValue();
    }

    public void setMin(double value) {
        minValProperty().set(value);
    }

    public DoubleProperty minValProperty() {
        return slider.minProperty();
    }

    public int getMinor() {
        return minorTickCountProperty().intValue();
    }

    public void setMinor(int value) {
        minorTickCountProperty().set(value);
    }

    public IntegerProperty minorTickCountProperty() {
        return slider.minorTickCountProperty();
    }

    public double getMajor() {
        return majorTickUnitProperty().doubleValue();
    }

    public void setMajor(double value) {
        majorTickUnitProperty().set(value);
    }

    public DoubleProperty majorTickUnitProperty() {
        return slider.majorTickUnitProperty();
    }
}
