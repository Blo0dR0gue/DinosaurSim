package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * The Custom Control Class provides a map list item for the {@link javafx.scene.control.ListView} of maps
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class MapListItem extends HBox {
    @FXML
    public HBox container;
        @FXML
        public RadioButton radioButton;
        @FXML
        public ImageView imageView;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public MapListItem() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link MapListItem}
     * @return The newly created and initialized {@link MapListItem}
     */
    public MapListItem newInstance() {
        return (MapListItem) Display.makeFXMLController("map-list-item.fxml", MapListItem.class);
    }

    /**
     * Method to initialize the Map list item, its listeners and setting the label text and image url
     */
    public void initialize(String labelText, Image image){
        setText(labelText);
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
