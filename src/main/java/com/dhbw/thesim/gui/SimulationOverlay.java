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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

/**
 * Represents the Simulation Overlay containing the control panel and drawn simulation-objects and grid-background
 *
 * @author Daniel Czeschner, Tamina Mühlenberg
 */
public class SimulationOverlay extends AnchorPane {

    private final Scene simulationScene;
    private Canvas backgroundCanvas;
    private GraphicsContext canvasGraphics;
    private Pane sidebar;
    private SimulationLoop simulationLoop;

    public static final double SCALE_X = Screen.getPrimary().getOutputScaleX();
    public static final double SCALE_Y = Screen.getPrimary().getOutputScaleY();

    public static final double BACKGROUND_WIDTH = adjustScale(1620, SCALE_X);
    public static final double BACKGROUND_HEIGHT = adjustScale(1080, SCALE_Y);

    public SimulationOverlay(Stage primaryStage) {


        createCanvas();
        createSideBar();

        setPrefHeight(adjustScale(1080, SCALE_X));
        setPrefWidth(adjustScale(1920, SCALE_Y));

        //Add the Canvas and the Sidebar to the StackPane
        getChildren().add(backgroundCanvas);
        getChildren().add(sidebar);

        //TODO get data from config screen
        Simulation sim = new Simulation("test", canvasGraphics, this, null, null, 10);

        simulationLoop = new SimulationLoop(1, 1, sim, 50, 2);

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

        double width = adjustScale(300, SCALE_X);

        sidebar.setPrefWidth(width);
        sidebar.setPrefHeight(adjustScale(1080, SCALE_Y));
        sidebar.relocate(adjustScale(1920d, SCALE_X) - width, 0d);

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

    /**
     * @param toScale Dimension Parameter to be scaled to {@code scale}
     * @param scale   Scale to adjust {@code toScale} to
     * @return The Dimension {@code toScale} is adjusted with the given Scale {@code scale}
     */
    public static double adjustScale(double toScale, double scale) {
        return (((double) toScale) / scale);
    }

}
