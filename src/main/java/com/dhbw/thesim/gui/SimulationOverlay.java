package com.dhbw.thesim.gui;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.gui.controllers.StatisticsEndcard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
    private Pane sidebar;
    private SimulationLoop simulationLoop;
    public AnchorPane centerPane;
    private Boolean simulationIsRunning ;

    public static final double BACKGROUND_WIDTH = Display.adjustScale(1620, Display.SCALE_X);
    public static final double BACKGROUND_HEIGHT = Display.adjustScale(1080, Display.SCALE_Y);

    public SimulationOverlay(Stage primaryStage) {
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
        centerPane.getChildren().add(sidebar);

        //Lay out the AnchorPane in the center position of the BorderPane
        setCenter(centerPane);

        //TODO get data from config screen
        Simulation sim = new Simulation("test", canvasGraphics, this, null, null, 10);

        simulationLoop = new SimulationLoop(1, 1, sim);
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
        sidebar = new StackPane();

        sidebar.setPrefWidth(Display.adjustScale(300, Display.SCALE_X));
        sidebar.setPrefHeight(Display.adjustScale(1080, Display.SCALE_Y));

        AnchorPane.setRightAnchor(sidebar, 0.0);

        sidebar.setBackground(new Background(new BackgroundFill(Color.web("#808080"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label title = new Label("Dinosaurier\nSimulation");
        title.setTextFill(Color.WHITE);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(new Font(20.0));
        sidebar.getChildren().add(title);
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        StackPane.setMargin(title, new Insets(10.0,0.0,0.0,0.0));

        //Add the control buttons to the sidebar and add a click listener to each
        Button playButton = addControlButtonToSidebar("/controls/play.png");
        playButton.setOnAction(e -> {
            if (!simulationIsRunning) {
                simulationLoop.togglePause();
                simulationIsRunning = !simulationIsRunning;
            }
        });
        StackPane.setAlignment(playButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(playButton, new Insets(0.0,0.0,10.0,0.0));
        Button pauseButton = addControlButtonToSidebar("/controls/pause.png");
        pauseButton.setOnAction(e -> {
            if (simulationIsRunning) {
                simulationLoop.togglePause();
                simulationIsRunning = !simulationIsRunning;
            }
        });
        StackPane.setAlignment(pauseButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(pauseButton, new Insets(0.0,0.0,10.0,0.0));
        Button stopButton = addControlButtonToSidebar("/controls/stop.png");
        stopButton.setOnAction(e -> {
            simulationLoop.stopSimulationRunner();
            Stage window = (Stage) stopButton.getScene().getWindow();
            StatisticsEndcard statisticsEndcard = StatisticsEndcard.newInstance();
            window.setScene(new Scene(statisticsEndcard));

            window.setFullScreen(true);
        });
        StackPane.setAlignment(stopButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(stopButton, new Insets(0.0,0.0,10.0,0.0));
    }

    private Button addControlButtonToSidebar(String controlImgUrl) {
        Image controlImg = new Image(controlImgUrl);
        ImageView controlImageView = new ImageView(controlImg);
        controlImageView.setFitHeight(40.0);
        controlImageView.setPreserveRatio(true);
        Button controlButton = new Button();
        controlButton.setGraphic(controlImageView);
        controlButton.styleProperty().set("-fx-background-color: transparent;");
        sidebar.getChildren().add(controlButton);

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
