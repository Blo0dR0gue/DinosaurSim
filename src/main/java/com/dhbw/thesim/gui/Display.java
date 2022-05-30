package com.dhbw.thesim.gui;

import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Main JavaFx Application
 * TODO gui stuff :D
 *
 * @author Daniel Czeschner
 */
public class Display extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("TheSim - A Dinosaur-Simulation");

        //This is a fullscreen application
        primaryStage.setFullScreen(true);

        //We don't want to exit the fullscreen when keys are pressed
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        //TODO scene-controller?
        SimulationOverlay simulationOverlay = new SimulationOverlay(primaryStage);
        //TODO remove tmp loading simulation overlay (entry is config)
        primaryStage.setScene(simulationOverlay.getSimulationScene());

        //Show the app
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        //TODO
        super.stop();
    }
}
