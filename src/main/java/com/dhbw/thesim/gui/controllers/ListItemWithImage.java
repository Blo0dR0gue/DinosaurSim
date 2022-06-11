package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

/**
 * The Custom Control Class provides a list item for the {@link ListView} of dinos or plants
 * @author Tamina Mühlenberg, Robin Khatri Chetri, Eric Stefan
 */
public class ListItemWithImage extends HBox {
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
    public Button button_remove;
    @FXML
    public Label sliderValueLabel;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public ListItemWithImage() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ListItemWithImage}
     * @return The newly created and initialized {@link ListItemWithImage}
     */
    public static ListItemWithImage newInstance(){
        return (ListItemWithImage) Display.makeFXMLController("list-item-with-image.fxml", ListItemWithImage.class);
    }

    /**
     * Method to initialize the List item, its listeners and setting the label text and image url.
     * Also a tooltip is set for additional information to dinosaur and/ or plant species.
     */
    public void initialize(String labelText, Image image, int amount, ListView<ListItemWithImage> listView, JsonHandler.SimulationObjectType type) throws IOException {
        setText(labelText);
        setImage(image);
        setCount(amount);

        //this variable contains the content which will be inside the tooltip
        String temp="";

        if (type == JsonHandler.SimulationObjectType.DINO){ //create the tooltip for dinosaurs
            HashMap<String,Object> dino = Objects.requireNonNull(JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO)).get(labelText);

            temp+="Eigenschaften:\n";
            switch (dino.get("Nahrungsart").toString()) {
                case "p" -> temp += "Nahrungsart: Pflanzenfresser\n";
                case "f" -> temp += "Nahrungsart: Fleischfresser\n";
                case "a" -> temp += "Nahrungsart: Allesfresser\n";
            }
            temp+="Gewicht: "+dino.get("Gewicht")+" kg\n";
            temp+="Laenge: "+dino.get("Laenge")+" m\n";
            temp+="Hoehe: "+dino.get("Hoehe")+" m\n";
            temp+="Kann Schwimmen: "+(dino.get("KannSchwimmen")=="true"?"ja":"nein")+"\n";
            temp+="Kann Klettern: "+(dino.get("KannKlettern")=="true"?"ja":"nein")+"\n";
            temp+="\n";

            temp+="Technische Parameter:\n";
            temp+="Nahrung: "+((double[])(dino.get("Nahrung")))[0]+", "+((double[])(dino.get("Nahrung")))[1]+"\n";
            temp+="Hydration: "+((double[])(dino.get("Hydration")))[0]+", "+((double[])(dino.get("Hydration")))[1]+"\n";
            temp+="Staerke: "+((double[])(dino.get("Staerke")))[0]+", "+((double[])(dino.get("Staerke")))[1]+"\n";
            temp+="Geschwindigkeit: "+((double[])(dino.get("Geschwindigkeit")))[0]+", "+((double[])(dino.get("Geschwindigkeit")))[1]+"\n";
            temp+="Fortpflanzungswilligkeit: "+((double[])(dino.get("Fortpflanzungswilligkeit")))[0]+", "+((double[])(dino.get("Fortpflanzungswilligkeit")))[1]+"\n";
            temp+="Sichtweite: "+((double[])(dino.get("Sichtweite")))[0]+", "+((double[])(dino.get("Sichtweite")))[1]+"\n";
            temp+="Interaktionsweite: "+((double[])(dino.get("Interaktionsweite")))[0]+", "+((double[])(dino.get("Interaktionsweite")))[1];

        } else if (type == JsonHandler.SimulationObjectType.PLANT) { //create the tooltip for plants
            HashMap<String,Object> plant = Objects.requireNonNull(JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.PLANT)).get(labelText);

            temp+="Technische Parameter:\n";
            temp+="Interaktionsweite: "+((double[])(plant.get("Interaktionsweite")))[0]+", "+((double[])(plant.get("Interaktionsweite")))[1];
        }

        ListItemWithImage listItemWithImage = this;

        Tooltip tooltip = new Tooltip(temp);
        Tooltip.install(container,tooltip);

        button_remove.setOnAction(event -> {
//            listView.getItems().remove(listItemWithImage);
            //TODO remove dino/plant from hashmap that is passed to simulation
            listItemWithImage.setCount(0);
        });

        slider.valueProperty()
                .addListener((observableValue, oldValue, newValue) -> sliderValueLabel.textProperty().setValue(
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

    public int getCount() {
        return countProperty().intValue();
    }

    public void setCount(int value) {
        countProperty().set(value);
        sliderValueLabel.setText(String.valueOf(value));
    }

    public DoubleProperty countProperty() {
        return slider.valueProperty();
    }
}
