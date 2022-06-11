package com.dhbw.thesim.gui;

import com.dhbw.thesim.gui.controllers.ConfigScreen;
import com.dhbw.thesim.impexp.Json2Objects;
import com.dhbw.thesim.impexp.JsonHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

/**
 * Main JavaFx Application
 *
 * @author Daniel Czeschner, Eric Stefan, Tamina Mühlenberg, Robin Khatri Chetri
 */
public class Display extends Application {

    public static final double SCALE_X = Screen.getPrimary().getOutputScaleX();
    public static final double SCALE_Y = Screen.getPrimary().getOutputScaleY();
    public static Scene configScene;

    /**
     * @param toScale Dimension Parameter to be scaled to {@code scale}
     * @param scale   Scale to adjust {@code toScale} to
     * @return The Dimension {@code toScale} is adjusted with the given Scale {@code scale}
     */
    public static double adjustScale(double toScale, double scale) {
        return (toScale / scale);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Init JsonData
        JsonHandler.setDirectory();
        JsonHandler.exportDefaultScenarioConfig();
        JsonHandler.exportDefaultSimulationObjectsConfig();

        //Setup GUI
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
            if (screenHeight < (1080.0 + 37.0)) {
                System.out.println("Display not possible, because your screen is too small in height.");
            }
        } else if (scaledScreenHeight < 1080.0 && scaledScreenWidth < 1920.0) { //If display is at minimum 1080x1920, but is scaled to some degree, check against scaled resolution

            System.out.println("Display not possible, because your screen is too small.\n"
                    + "Resolution: " + screenHeight + "x" + screenWidth + "\n"
                    + "Output Scale Vertical: " + SCALE_Y + "\n"
                    + "Output Scale Horizontal: " + SCALE_X + "\n"
                    + "Scaled Resolution: " + scaledScreenHeight + "x" + scaledScreenWidth
            );

            System.exit(1);
        } else {
            primaryStage.setFullScreen(true);
        }

        //We don't want to exit the fullscreen when keys are pressed
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        HashMap<JsonHandler.ScenarioConfigParams, ArrayList<Object[]>> defaultScenarioParams = Json2Objects.getParamsForGUI(Json2Objects.Type.WITH_SCENARIO_FILE, "defaultScenarioConfiguration");

        //If plantGrowthRate from the defaultScenarioParams is null, there should be 1.0 returned
        double plantGrowthRate = ((double) getDefaultIfNull(defaultScenarioParams.get(JsonHandler.ScenarioConfigParams.PLANT_GROWTH), 1.0).get(0)[0]);
        //If landscapeName from the defaultScenarioParams is null, there should be "Landschaft1" returned
        String landscapeName = ((String) getDefaultIfNull(defaultScenarioParams.get(JsonHandler.ScenarioConfigParams.LANDSCAPE), "Landschaft1").get(0)[0]);

        //Creates the Configuration Screen and sets its scene as the current one on the primary stage
        ConfigScreen configScreen = ConfigScreen.newInstance();
        configScreen.initialize(defaultScenarioParams.get(JsonHandler.ScenarioConfigParams.DINO), defaultScenarioParams.get(JsonHandler.ScenarioConfigParams.PLANT), plantGrowthRate, landscapeName);

        configScene = new Scene(configScreen);
        primaryStage.setScene(configScene);

        //Show the app
        primaryStage.show();
    }

    /**
     * @param filename Name of the FXML File
     * @param controllerClass Class of the Controller to the corresponding FXML File
     * @return Loads the FXML File into a controller of the given class and returns that controller instance
     */
    public static Object makeFXMLController(String filename, Class<?> controllerClass){
        FXMLLoader loader = new FXMLLoader();

        // Try to load the fxml into a controller
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

    private ArrayList<Object[]> getDefaultIfNull(ArrayList<Object[]> list, Object defaultValue){
        return Optional.ofNullable(list).orElse(new ArrayList<>(Collections.singleton(new Object[]{defaultValue})));
    }

    @Override
    public void stop() throws Exception {
        //TODO
        super.stop();
    }
}
