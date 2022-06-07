package com.dhbw.thesim.gui;

import com.dhbw.thesim.gui.controllers.ConfigScreen;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;

import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
        return (toScale / scale);
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

        ConfigScreen configScreen = ConfigScreen.newInstance();
        configScreen.initializeListeners();
        primaryStage.setScene(new Scene(configScreen));

        //Show the app
        primaryStage.show();
    }

    /**
     *
     * @param filename Name of the FXML File
     * @param controllerClass Class of the Controller to the corresponding FXML File
     * @return Loads the FXML File into a controller of the given class and returns that controller instance
     */
    public static Object makeFXMLController(String filename, Class<?> controllerClass){
        FXMLLoader loader = new FXMLLoader();

        try {
            Object controller = controllerClass.getConstructor().newInstance();
            loader.setRoot(controller);
            loader.setController(controller);

            loader.setLocation(controllerClass.getResource("/gui/" + filename));

            loader.load();

            return controller;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void stop() throws Exception {
        //TODO
        super.stop();
    }
}
