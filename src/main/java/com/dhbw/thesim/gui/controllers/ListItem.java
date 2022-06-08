package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
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
 * The Custom Control Class provides a list item for the {@link javafx.scene.control.ListView} of dinos or plants
 * @author Tamina Mühlenberg, Robin Khatri Chetri
 */
public class ListItem extends HBox {
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
    @FXML
    public Label sliderValueLabel;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public ListItem() {

    }

    /**
     * This method creates and initializes a new instance of from the FXML {@link ListItem}
     * @return The newly created and initialized {@link ListItem}
     */
    public static ListItem newInstance(){
        return (ListItem) Display.makeFXMLController("list-item.fxml", ListItem.class);
    }

    /**
     * Method to initialize the List item, its listeners and setting the label text and image url
     */
    public void initialize(String labelText, Image image, int amount, ListView<ListItem> listView){
        setText(labelText);
        setImage(image);
        setCount(amount);

        ListItem listItem = this;
        button_remove.setOnAction(event -> {
            listView.getItems().remove(listItem);
            //TODO remove dino/plant from hashmap that is passed to simulation
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

    public Integer getCount() {
        return countProperty().intValue();
    }

    public void setCount(Integer value) {
        countProperty().set(value);
        sliderValueLabel.setText(String.valueOf(value));
    }

    public DoubleProperty countProperty() {
        return slider.valueProperty();
    }
}
