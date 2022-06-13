package com.dhbw.thesim.gui;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.core.util.SpriteLibrary;
import com.dhbw.thesim.gui.controllers.ConfigScreen;
import com.dhbw.thesim.gui.controllers.LegendItem;
import com.dhbw.thesim.gui.controllers.SideBar;
import com.dhbw.thesim.gui.controllers.StatisticsEndcard;
import com.dhbw.thesim.impexp.JsonHandler;
import com.dhbw.thesim.stats.Statistics;
import com.dhbw.thesim.stats.StatisticsStruct;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

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
    private final boolean isSimulationModeAuto;

    public static final double BACKGROUND_WIDTH = Display.adjustScale(1620, Display.SCALE_X);
    public static final double BACKGROUND_HEIGHT = Display.adjustScale(1080, Display.SCALE_Y);

    private Statistics statistics;

    public SimulationOverlay(Stage primaryStage, ConfigScreen configScreen) {
        //Create another pane which acts as a container for the simulation overlay which allows for centering in fullscreen mode
        centerPane = new AnchorPane();
        centerPane.setMaxWidth(Display.adjustScale(1920, Display.SCALE_X));
        centerPane.setMaxHeight(Display.adjustScale(1080, Display.SCALE_Y));
        centerPane.setMinWidth(Display.adjustScale(1920, Display.SCALE_X));
        centerPane.setMinHeight(Display.adjustScale(1080, Display.SCALE_Y));

        isSimulationModeAuto = Objects.equals(configScreen.getMode().getId(), "auto");

        createCanvas();
        createSideBar();

        //Add the Canvas and the Sidebar to the AnchorPane
        centerPane.getChildren().add(backgroundCanvas);
        centerPane.getChildren().add(sideBar);

        //Lay out the AnchorPane in the center position of the BorderPane
        setCenter(centerPane);

        //create Statistics
        statistics = new Statistics();

        Simulation sim = new Simulation(configScreen.getMap().getId(), canvasGraphics, this, configScreen.getDinoParams(), configScreen.getPlantParams(), configScreen.getPlantGrowthRate());

        simulationLoop = new SimulationLoop((int) configScreen.getSimulationSteps(), (int) configScreen.getSimulationSteps(), sim, (int) configScreen.getMaxSteps(), (int) configScreen.getMaxRuntime(), this);

        statistics.addSimulationObjectList(sim.getSimulationObjects(), simulationLoop.getCurrentSimulation().getCurrentSimulationTime());

        //Create the Scene
        simulationScene = new Scene(this);

        //Set a background image for fullscreen mode if screen resolution is higher than 1920x1080
        Image img = new Image("/background/background.jpg");
        simulationScene.setFill(new ImagePattern(img, 0, 0, 1, 1, true));
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background bGround = new Background(bImg);
        setBackground(bGround);

        createSideLegend();
        createSideDinosaurStats();

        simulationScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                simulationLoop.togglePause();
            }
            if (e.getCode() == KeyCode.SPACE) {
                if (!isSimulationModeAuto) {
                    nextSimulationStep();
                }
            }
        });

        primaryStage.setOnCloseRequest(e -> {
            simulationLoop.stopSimulationRunner();
            if (timer != null)
                timer.cancel();
        });

        if (isSimulationModeAuto)
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

        //Check if automatic simulation mode was selected from the config screen, then add needed controls to sidebar
        if (isSimulationModeAuto) {
            //Add the control buttons for automatic simulation mode to the sidebar and add a click listener to each
            createToggleButton("/controls/play.png", false);

            createToggleButton("/controls/pause.png", true);
        } else {
            //Add the control buttons for manual simulation mode to the sidebar and add a click listener to each
            Button nextStepButton = addControlButtonToSideBar("/controls/next.png");
            nextStepButton.setOnAction(e -> nextSimulationStep());
        }
        createStopButton();
    }

    private void nextSimulationStep() {
        simulationLoop.triggerUpdates();
        statistics.addSimulationObjectList(simulationLoop.getCurrentSimulation().getSimulationObjects(), simulationLoop.getCurrentSimulation().getCurrentSimulationTime());
        triggerDinosaurSingleStatsUpdate();
    }

    private void createToggleButton(String controlImgUrl, Boolean shouldPauseSimulation) {
        Button toggleButton = addControlButtonToSideBar(controlImgUrl);
        toggleButton.setOnAction(e -> {
            if (simulationLoop.getSimulationPaused() != shouldPauseSimulation) {
                simulationLoop.togglePause();
            }
        });
    }

    private void createStopButton() {
        Button stopButton = addControlButtonToSideBar("/controls/stop.png");
        stopButton.setOnAction(e -> {
            simulationLoop.stopSimulationRunner();
            showStatisticsEndcard();
        });
    }

    public void showStatisticsEndcard() {
        statistics.addSimulationObjectList(simulationLoop.getCurrentSimulation().getSimulationObjects(), simulationLoop.getCurrentSimulation().getCurrentSimulationTime());

        Stage window = (Stage) simulationScene.getWindow();
        StatisticsEndcard statisticsEndcard = StatisticsEndcard.newInstance();
        statisticsEndcard.initialize(statistics, isSimulationModeAuto);

        window.setScene(new Scene(statisticsEndcard));

        window.setFullScreen(true);
    }

    private Button addControlButtonToSideBar(String controlImgUrl) {
        Image controlImg = new Image(controlImgUrl);
        ImageView controlImageView = new ImageView(controlImg);
        controlImageView.setFitHeight(40.0);
        controlImageView.setPreserveRatio(true);
        Button controlButton = new Button();
        controlButton.setGraphic(controlImageView);
        controlButton.styleProperty().set("-fx-background-color: transparent;");
        sideBar.getFooter().add(controlButton);

        return controlButton;
    }

    //region dinosaur stats

    /**
     * Is called, when a dinosaur gets clicked with the mouse.
     *
     * @param dinosaur The {@link Dinosaur} object.
     */
    public void dinosaurClicked(Dinosaur dinosaur) {
        if (simulationLoop.getSimulationPaused() && isSimulationModeAuto) {
            startStatsTimer(dinosaur);
        } else if (!isSimulationModeAuto) {
            if (lastSelectedDinosaur != null)
                lastSelectedDinosaur.setSelectionRingVisibility(false);
            lastSelectedDinosaur = dinosaur;
            dinosaur.setSelectionRingVisibility(true);
            triggerDinosaurSingleStatsUpdate();
        }
    }

    private Dinosaur lastSelectedDinosaur;

    private void startStatsTimer(Dinosaur dinosaur) {
        if (timer != null)
            timer.cancel();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!dinosaur.diedOfThirst() && !dinosaur.diedOfHunger()) {
                        if (lastSelectedDinosaur != null)
                            lastSelectedDinosaur.setSelectionRingVisibility(false);
                        lastSelectedDinosaur = dinosaur;
                        dinosaur.setSelectionRingVisibility(true);
                        triggerDinosaurSingleStatsUpdate();
                    } else {
                        //The dinosaur died
                        lastSelectedDinosaur = null;
                        dinosaur.setSelectionRingVisibility(false);
                        resetStatsScreen();
                        cancel();
                    }
                });
            }
        }, 0, 2000);
    }

    private void triggerDinosaurSingleStatsUpdate() {
        if (lastSelectedDinosaur != null && !lastSelectedDinosaur.diedOfHunger() && !lastSelectedDinosaur.diedOfThirst())
            setSideBarStats(statistics.getSingleStats(lastSelectedDinosaur, List.copyOf(simulationLoop.getCurrentSimulation().getSimulationObjects()), simulationLoop.getCurrentSimulation().getCurrentSimulationTime()));
        else
            resetStatsScreen();
    }

    private void resetStatsScreen() {
        nutrition.setText(NO_DINO_SELECTED);
        hydration.setText(NO_DINO_SELECTED);
        fertility.setText(NO_DINO_SELECTED);
        weight.setText(NO_DINO_SELECTED);
        height.setText(NO_DINO_SELECTED);
        length.setText(NO_DINO_SELECTED);
        survivalTime.setText(NO_DINO_SELECTED);
        speciesProportion.setText(NO_DINO_SELECTED);
    }


    private static final String NO_DINO_SELECTED = "Kein Dino";

    private final Label dinoType = new Label(NO_DINO_SELECTED, new Text("Dinosaurierart: "));
    private final Label dietType = new Label(NO_DINO_SELECTED, new Text("Nahrungart: "));
    private final Label gender = new Label(NO_DINO_SELECTED, new Text("Geschlecht: "));
    private final Label nutrition = new Label(NO_DINO_SELECTED, new Text("Nahrung: "));
    private final Label hydration = new Label(NO_DINO_SELECTED, new Text("Hydration: "));
    private final Label fertility = new Label(NO_DINO_SELECTED, new Text("Fortpflanzungswille: "));
    private final Label weight = new Label(NO_DINO_SELECTED, new Text("Gewicht: "));
    private final Label height = new Label(NO_DINO_SELECTED, new Text("Höhe: "));
    private final Label length = new Label(NO_DINO_SELECTED, new Text("Länge: "));
    private final Label canSwim = new Label(NO_DINO_SELECTED, new Text("Kann schwimmen: "));
    private final Label canClimb = new Label(NO_DINO_SELECTED, new Text("Kann klettern: "));
    private final Label survivalTime = new Label(NO_DINO_SELECTED, new Text("Überlebenszeit: "));
    private final Label speciesProportion = new Label(NO_DINO_SELECTED, new Text("Artenanteil: "));


    private Timer timer = null;
    private static final DecimalFormat dfZero = new DecimalFormat("0.0");

    private void createSideLegend() {
        VBox vBox = new VBox();
        Label legendTitle = new Label("Legende:");
        legendTitle.setTextFill(Color.WHITE);
        legendTitle.setFont(new Font(17.0));
        vBox.getChildren().add(legendTitle);

        StatisticsStruct stats = statistics.getSimulationStats();
        for (String speciesName : stats.getAllSpecies()) {
            try {
                //Retrieve sim object config and instantiating and initializing legend item to add to sidebar legend
                HashMap<String, Object> dino = Objects.requireNonNull(
                        JsonHandler.importSimulationObjectsConfig(JsonHandler.SimulationObjectType.DINO)
                ).get(speciesName);
                LegendItem legendItem = LegendItem.newInstance();
                legendItem.initialize(SpriteLibrary.getInstance().getImage((String) dino.get("Bild")), speciesName);
                vBox.getChildren().add(legendItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Separator separator = new Separator();
        vBox.getChildren().add(separator);
        VBox.setMargin(separator, new Insets(10.0, 0.0, 10.0, 0.0));

        sideBar.getBody().add(vBox);
    }

    private void setSideBarStats(Map<String, Double> dinosaurStats) {

        dinoType.setText(lastSelectedDinosaur.getType());

        dietType.setText(lastSelectedDinosaur.getDiet().translatedText);

        gender.setText(lastSelectedDinosaur.getGender() == 'm' ? "Männlich" : "Weiblich");

        nutrition.setText(dfZero.format(dinosaurStats.get("Hunger")) + " / " + dfZero.format(dinosaurStats.get("MaxHunger")));

        hydration.setText(dfZero.format(dinosaurStats.get("Durst")) + " / " + dfZero.format(dinosaurStats.get("MaxDurst")));

        fertility.setText(dfZero.format(dinosaurStats.get("Fortpflanzungswilligkeit")) + " %");

        weight.setText(dfZero.format(dinosaurStats.get("Gewicht")) + " Kilogramm");

        height.setText(dfZero.format(dinosaurStats.get("Hoehe")) + " Meter");

        length.setText(dfZero.format(dinosaurStats.get("Laenge")) + " Meter");

        canSwim.setText(dinosaurStats.get("KannSchwimmen") > 0 ? "Ja" : "Nein");

        canClimb.setText(dinosaurStats.get("KannKlettern") > 0 ? "Ja" : "Nein");

        survivalTime.setText(dfZero.format(dinosaurStats.get("Ueberlebenszeit")) + " Tage");

        speciesProportion.setText(dfZero.format(dinosaurStats.get("Artenanteil") * 100) + " %");
    }

    private void createSideDinosaurStats() {
        VBox vBox = new VBox();

        Label statsTitle = new Label("Statistik / Eigenschaften:");
        statsTitle.setTextFill(Color.WHITE);
        statsTitle.setFont(new Font(17.0));
        vBox.getChildren().add(statsTitle);

        dinoType.setTextFill(Color.WHITE);
        dietType.setTextFill(Color.WHITE);
        gender.setTextFill(Color.WHITE);
        nutrition.setTextFill(Color.WHITE);
        hydration.setTextFill(Color.WHITE);
        fertility.setTextFill(Color.WHITE);
        weight.setTextFill(Color.WHITE);
        height.setTextFill(Color.WHITE);
        length.setTextFill(Color.WHITE);
        canSwim.setTextFill(Color.WHITE);
        canClimb.setTextFill(Color.WHITE);
        survivalTime.setTextFill(Color.WHITE);
        speciesProportion.setTextFill(Color.WHITE);


        vBox.getChildren().add(dinoType);
        vBox.getChildren().add(dietType);
        vBox.getChildren().add(gender);
        vBox.getChildren().add(nutrition);
        vBox.getChildren().add(hydration);
        vBox.getChildren().add(fertility);
        vBox.getChildren().add(weight);
        vBox.getChildren().add(height);
        vBox.getChildren().add(length);
        vBox.getChildren().add(canSwim);
        vBox.getChildren().add(canClimb);
        vBox.getChildren().add(survivalTime);
        vBox.getChildren().add(speciesProportion);

        sideBar.getBody().add(vBox);
    }


    //endregion


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

    public Statistics getStatistics() {
        return statistics;
    }

}
