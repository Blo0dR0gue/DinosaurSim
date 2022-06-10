package com.dhbw.thesim.gui.controllers;

import com.dhbw.thesim.gui.Display;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * The Control Class for the side bar FXML file
 * @author Robin Khatri Chetri
 */
public class SideBar extends BorderPane {
    @FXML
    public BorderPane root;
    @FXML
    public HBox bottomContainer;
    @FXML
    public VBox centerContainer;

    /**
     * The {@code Constructor} of this class which {@link Display#makeFXMLController(String, Class)}
     * is getting to create the specified controller
     */
    public SideBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/side-bar.fxml"));
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
     * This method creates and initializes a new instance of from the FXML {@link SideBar}
     * @return The newly created and initialized {@link SideBar}
     */
    public static SideBar newInstance() {
        return (SideBar) Display.makeFXMLController("side-bar.fxml", SideBar.class);
    }

    /**
     * Method to initialize the side bar, its listeners and adding custom controls dynamically
     */
    public void initialize() {
        initializeListeners();
    }

    /**
     * Adds all specified event handlers to the specified {@link Node}
     */
    public void initializeListeners() {

    }

    public ObservableList<Node> getBody() {
        return centerContainer.getChildren();
    }

    public ObservableList<Node> getFooter() {
        return bottomContainer.getChildren();
    }
}
