package com.dhbw.thesim.gui;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.gui.controllers.ConfigScreen;
import com.dhbw.thesim.gui.controllers.SideBar;
import com.dhbw.thesim.gui.controllers.StatisticsEndcard;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

/**
 * Represents the Simulation Overlay containing the control panel and drawn simulation-objects and grid-background
 *
 * @author Daniel Czeschner, Tamina Mühlenberg, Robin Khatri Chetri
 */
public class SimulationOverlay extends BorderPane {

    private final Scene simulationScene;
    private Canvas backgroundCanvas;
    private GraphicsContext canvasGraphics;
    private SimulationLoop simulationLoop;
    public AnchorPane centerPane;
    private Boolean simulationIsRunning;
    private SideBar sideBar;

    public static final double BACKGROUND_WIDTH = Display.adjustScale(1620, Display.SCALE_X);
    public static final double BACKGROUND_HEIGHT = Display.adjustScale(1080, Display.SCALE_Y);

    public SimulationOverlay(Stage primaryStage, ConfigScreen configScreen) {
        //Create another pane which acts as a container for the simulation overlay which allows for centering in fullscreen mode
        centerPane = new AnchorPane();
        centerPane.setMaxWidth(Display.adjustScale(1920, Display.SCALE_X));
        centerPane.setMaxHeight(Display.adjustScale(1080, Display.SCALE_Y));
        centerPane.setMinWidth(Display.adjustScale(1920, Display.SCALE_X));
        centerPane.setMinHeight(Display.adjustScale(1080, Display.SCALE_Y));

        createCanvas();
        createSideBar();

        //Add the Canvas and the Sidebar to the AnchorPane
        centerPane.getChildren().add(backgroundCanvas);
        centerPane.getChildren().add(sideBar);

        //Lay out the AnchorPane in the center position of the BorderPane
        setCenter(centerPane);

        //TODO get data from config screen
        Simulation sim = new Simulation(configScreen.getMap().getId(), canvasGraphics, this, configScreen.getDinoParams(), configScreen.getPlantParams(), configScreen.getPlantGrowthRate());

        simulationLoop = new SimulationLoop(1, (int) configScreen.getSimulationSteps(), sim, (int) configScreen.getMaxSteps(), (int) configScreen.getMaxRuntime());

        simulationIsRunning = true;

        //Create the Scene
        simulationScene = new Scene(this);

        //Set a background image for fullscreen mode if screen resolution is higher than 1920x1080
        //TODO Background image or colored background?
        Image img = new Image("/background/background.jpg");
        simulationScene.setFill(new ImagePattern(img, 0, 0, 1, 1, true));
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0,1.0,true,true,false,false));
        Background bGround = new Background(bImg);
        setBackground(bGround);

        simulationScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                simulationLoop.togglePause();
            }
            //TODO handle step procedure
            if (e.getCode() == KeyCode.D) {
                simulationLoop.triggerUpdates();
            }
        });

        primaryStage.setOnCloseRequest(e -> simulationLoop.stopSimulationRunner());

        //TODO handle loop
        simulationLoop.startSimulationRunner();
    }

    private void createCanvas() {
        backgroundCanvas = new Canvas(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        canvasGraphics = backgroundCanvas.getGraphicsContext2D();
    }

    private void createSideBar() {
        sideBar = SideBar.newInstance();

        sideBar.setPrefWidth(Display.adjustScale(300, Display.SCALE_X));
        sideBar.setPrefHeight(Display.adjustScale(1080, Display.SCALE_Y));

        AnchorPane.setRightAnchor(sideBar, 0.0);

        //Add the control buttons to the sidebar and add a click listener to each
        Button playButton = addControlButtonToSideBar("/controls/play.png");
        playButton.setOnAction(e -> {
            if (!simulationIsRunning) {
                simulationLoop.togglePause();
                simulationIsRunning = true ;
            }
        });

        Button pauseButton = addControlButtonToSideBar("/controls/pause.png");
        pauseButton.setOnAction(e -> {
            if (simulationIsRunning) {
                simulationLoop.togglePause();
                simulationIsRunning = false;
            }
        });

        Button stopButton = addControlButtonToSideBar("/controls/stop.png");
        stopButton.setOnAction(e -> {
            simulationLoop.stopSimulationRunner();
            Stage window = (Stage) stopButton.getScene().getWindow();
            StatisticsEndcard statisticsEndcard = StatisticsEndcard.newInstance();
            window.setScene(new Scene(statisticsEndcard));

            window.setFullScreen(true);
        });
    }

    private Button addControlButtonToSideBar(String controlImgUrl) {
        Image controlImg = new Image(controlImgUrl);
        ImageView controlImageView = new ImageView(controlImg);
        controlImageView.setFitHeight(40.0);
        controlImageView.setPreserveRatio(true);
        Button controlButton = new Button();
        controlButton.setGraphic(controlImageView);
        controlButton.styleProperty().set("-fx-background-color: transparent;");
        sideBar.bottomContainer.getChildren().add(controlButton);

        return controlButton;
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
