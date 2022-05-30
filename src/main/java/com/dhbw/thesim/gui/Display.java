package com.dhbw.thesim.gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Main JavaFx Application
 * TODO gui stuff :D
 *
 * @author Daniel Czeschner, Eric Stefan
 */
public class Display extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("TheSim - A Dinosaur-Simulation");

        //This is a fullscreen application for 1920x1080 screens
        //but as a workaround for not appropriate screen sizes the window size is set to round about 1920x1080 pixels
        //remember that this is not claimed in the functional specification document, which is why we are not liable for this
        //TODO noch auf Windows testen, ob es mit der screenHeight bei zu großem Bildschirm funktioniert (auf Linux mit Gnome funktionierts)
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        if (screenHeight>1080.0 && screenWidth>1920.0){
            if (screenHeight>=(1080.0+37.0)){
                primaryStage.setMaxHeight(1080.0+37.0);
                primaryStage.setMaxWidth(1920.0);
            }else {
                System.out.println("Display not possible, because your screen is too small in height.");
            }
        }else if (screenHeight<1080.0 && screenWidth<1920.0) {
            System.out.println("Display not possible, because your screen is too small.");
        }else{
            primaryStage.setFullScreen(true);
        }

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
