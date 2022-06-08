package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * The Custom Control Class provides a dino list item for the {@link javafx.scene.control.ListView} of dinos
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class DinoListItem extends HBox {
    @FXML
    public HBox container;
        @FXML
        public ImageView imageView;
        @FXML
        public BorderPane borderPane;
            @FXML
            public Label label_name;
            @FXML
            public Slider slider;
            @FXML
            public HBox hbox_inner;
                @FXML
                public Button button_info;
                @FXML
                public Button button_remove;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public DinoListItem() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link DinoListItem}
     * @return The newly created and initialized {@link DinoListItem}
     */
    public static DinoListItem newInstance(){
        return (DinoListItem) Display.makeFXMLController("dino-list-item.fxml", DinoListItem.class);
    }

    /**
     * Method to initialize the Dino list item, its listeners and setting the label text and image url
     */
    public void initialize(String labelText, Image image, ListView<DinoListItem> dinoListView){
        setText(labelText);
        setImage(image);
        DinoListItem dinoListItem = this;
        button_remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dinoListView.getItems().remove(dinoListItem);
                //TODO remove dino from hashmap that is passed to simulation
            }
        });
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return label_name.textProperty();
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
