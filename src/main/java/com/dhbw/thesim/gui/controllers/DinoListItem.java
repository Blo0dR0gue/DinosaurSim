package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

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

    public DinoListItem() {

    }

    public static DinoListItem newInstance(){
        return (DinoListItem) Display.makeFXMLController("dino-list-item.fxml", DinoListItem.class);
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
