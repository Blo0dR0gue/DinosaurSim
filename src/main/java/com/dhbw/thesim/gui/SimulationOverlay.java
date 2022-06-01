package com.dhbw.thesim.gui;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Represents the Simulation Overlay containing the control panel and drawn simulation-objects and grid-background
 *
 * @author Daniel Czeschner
 */
public class SimulationOverlay extends AnchorPane {

    private final Scene simulationScene;
    private Canvas backgroundCanvas;
    private GraphicsContext canvasGraphics;
    private Pane sidebar;
    private SimulationLoop simulationLoop;

    public static final int BACKGROUND_WIDTH = 1620;
    public static final int BACKGROUND_HEIGHT = 1080;

    public SimulationOverlay(Stage primaryStage) {

        createCanvas();
        createSideBar();

        setPrefHeight(1920);
        setPrefWidth(1080);

        //Add the Canvas and the Sidebar to the StackPane
        getChildren().add(backgroundCanvas);
        getChildren().add(sidebar);

        Circle c = new Circle(500,500,50);
        c.setFill(Color.GREEN);
        Line l = new Line(500,500,300,450);
        l.setStroke(Color.BLUE);
        getChildren().add(c);
        getChildren().add(l);

        //TODO get data from config screen
        Simulation sim = new Simulation("test", canvasGraphics, this, null, null, 0);

        simulationLoop = new SimulationLoop(1, 1, sim);

        //Create the Scene
        simulationScene = new Scene(this);

        simulationScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                simulationLoop.togglePause();
            }
            //TODO handle step procedure
            if (e.getCode() == KeyCode.D) {
                simulationLoop.triggerUpdates();
            }
        });

        primaryStage.setOnCloseRequest(e -> {
            simulationLoop.stopSimulationRunner();
        });

        //TODO handle loop
        simulationLoop.startSimulationRunner();
    }

    private void createCanvas() {
        backgroundCanvas = new Canvas(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        canvasGraphics = backgroundCanvas.getGraphicsContext2D();
    }

    private void createSideBar() {
        sidebar = new Pane();

        sidebar.setPrefWidth(300);
        sidebar.setPrefHeight(1080);
        sidebar.relocate(1920d-300d,0d);

        sidebar.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    //region getter & setter

    public Scene getSimulationScene() {
        return simulationScene;
    }

    public Canvas getBackgroundCanvas() {
        return backgroundCanvas;
    }

    public GraphicsContext getCanvasGraphics() {
        return canvasGraphics;
    }

    //endregion


}
