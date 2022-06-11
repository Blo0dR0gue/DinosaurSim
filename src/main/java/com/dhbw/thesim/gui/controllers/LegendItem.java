package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


/**
 * The Custom Control Class provides a legend item for the legend of all dinosaur species
 * @author Robin Khatri Chetri
 */
public class LegendItem extends HBox {
    @FXML
    public HBox root;
        @FXML
        public ImageView imageView;
        @FXML
        public Label legendLabel;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public LegendItem() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link LegendItem}
     * @return The newly created and initialized {@link LegendItem}
     */
    public static LegendItem newInstance() {
        return (LegendItem) Display.makeFXMLController("legend-item.fxml", LegendItem.class);
    }

    /**
     * Method to initialize the Map list item, its listeners and setting the label text and image url
     */
    public void initialize(Image image, String legendLabelText){
        setImage(image);
        setText(legendLabelText);
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

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set("= " + value);
    }

    public StringProperty textProperty() {
        return legendLabel.textProperty();
    }
}
