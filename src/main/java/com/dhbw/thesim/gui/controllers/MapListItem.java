package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MapListItem extends HBox {
    @FXML
    public HBox container;
        @FXML
        public RadioButton radioButton;
        @FXML
        public ImageView imageView;

    public MapListItem() {

    }

    public MapListItem newInstance() {
        return (MapListItem) Display.makeFXMLController("map-list-item.fxml", MapListItem.class);
    }

    public void initialize(String label, Image image){
        setText(label);
        setImage(image);
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return radioButton.textProperty();
    }

    public Image getImage( ) {
        return imageProperty().getValue();
    }

    public void setImage(Image image) {
        imageProperty().set(image);
    }

    public ObjectProperty<Image> imageProperty() {
        return imageView.imageProperty();
    }
}
