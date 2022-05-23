package com.dhbw.thesim.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Represents the Simulation Overlay containing the control panel and drawn simulation-objects and grid-background
 *
 * @author Daniel Czeschner
 */
public class SimulationOverlay extends AnchorPane {

    private Scene simulationScene;
    private Canvas backgroundCanvas;
    private GraphicsContext canvasGraphics;
    private AnchorPane sidebar;
    //TODO add simloop

    public SimulationOverlay() {

        createCanvas();
        createSideBar();

        setPrefHeight(1920);
        setPrefWidth(1080);

        //Add the Canvas and the Sidebar to the StackPane
        getChildren().add(backgroundCanvas);
        getChildren().add(sidebar);

        //Create the Scene
        simulationScene = new Scene(this);
    }

    private void createCanvas() {
        backgroundCanvas = new Canvas(1620, 1080);
        canvasGraphics = backgroundCanvas.getGraphicsContext2D();
    }

    private void createSideBar(){
        sidebar = new AnchorPane();

        sidebar.setPrefWidth(300);
        sidebar.setPrefHeight(1080);
        AnchorPane.setRightAnchor(sidebar, 0d);

        sidebar.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    //region getter & setter

    public Scene getSimulationScene(){
        return simulationScene;
    }

    public Canvas getBackgroundCanvas() {
        return backgroundCanvas;
    }

    public GraphicsContext getCanvasGraphics(){
        return canvasGraphics;
    }

    //endregion


}
