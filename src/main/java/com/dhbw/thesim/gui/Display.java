package com.dhbw.thesim.gui;

import javafx.application.Application;
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

    public static final double SCALE_X = Screen.getPrimary().getOutputScaleX();
    public static final double SCALE_Y = Screen.getPrimary().getOutputScaleY();

    /**
     * @param toScale Dimension Parameter to be scaled to {@code scale}
     * @param scale   Scale to adjust {@code toScale} to
     * @return The Dimension {@code toScale} is adjusted with the given Scale {@code scale}
     */
    public static double adjustScale(double toScale, double scale) {
        return (((double) toScale) / scale);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("TheSim - A Dinosaur-Simulation");

        //This is a fullscreen application for 1920x1080 screens
        //but as a workaround for not appropriate screen sizes the window size is set to round about 1920x1080 pixels
        //remember that this is not claimed in the functional specification document, which is why we are not liable for this
        //TODO noch auf Windows testen, ob es mit der screenHeight bei zu großem Bildschirm funktioniert (auf Linux mit Gnome funktionierts)
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        double scaledScreenHeight = screenHeight * SCALE_Y;
        double scaledScreenWidth = screenWidth * SCALE_X;

        if (screenHeight > 1080.0 && screenWidth > 1920.0) {
            //TODO THIS PART IS ONLY FOR DEBUGGING REASONS and should be removed on release
            if (screenHeight >= (1080.0 + 37.0)) {
                //primaryStage.setMaxHeight(1080.0+37.0);
                //primaryStage.setMaxWidth(1920.0);
            } else {
                System.out.println("Display not possible, because your screen is too small in height.");
            }
        } else if (scaledScreenHeight < 1080.0 && scaledScreenWidth < 1920.0) { //If display is at minimum 1080x1920, but is scaled to some degree, check against scaled resolution

            System.out.println("Display not possible, because your screen is too small.\n"
                    + "Resolution: " + screenHeight + "x" + screenWidth + "\n"
                    + "Output Scale Vertical: " + SCALE_Y + "\n"
                    + "Output Scale Horizontal: " + SCALE_X + "\n"
                    + "Scaled Resolution: " + scaledScreenHeight + "x" + scaledScreenWidth
            );

            //TODO: Exit program on too small resolution
        } else {
            //TODO currently works with white color around AND currently isn't centred -> this two things should be updated
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
