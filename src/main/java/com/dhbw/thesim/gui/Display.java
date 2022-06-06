package com.dhbw.thesim.gui;

import com.dhbw.thesim.gui.controllers.DinoListItem;
import com.dhbw.thesim.gui.controllers.SliderNoImage;
import com.dhbw.thesim.gui.controllers.StartSimulation;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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

        //loading start scene
        //TODO properly format startScene
        FXMLLoader startSceneLoader = new FXMLLoader();
        StartSimulation startSceneController = new StartSimulation();
        startSceneLoader.setController(startSceneController);
        startSceneLoader.setLocation(getLocationURL("StartSimulation.fxml"));

        try {
            startSceneLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SliderNoImage pflanzenwachstumsrate_slider = makeSliderNoImage("Pflanzenwachstumsrate");

        startSceneController.borderPane_bottom.setLeft(pflanzenwachstumsrate_slider.vbox_outer);

        //adding dinos to list
        DinoListItem dinoListItem1 = makeDinoListItem();
        dinoListItem1.label_name.setText("dino1");

        DinoListItem dinoListItem2 = makeDinoListItem();
        dinoListItem2.label_name.setText("dino2");

        for (DinoListItem dinoListItem : Arrays.asList(dinoListItem1, dinoListItem2)) {
            startSceneController.listView1.getItems().add(dinoListItem.hbox_outer);
        }

        //adding startScene to primary stage
        Scene scene = new Scene(startSceneController.outer_borderPane);
        primaryStage.setScene(scene);

        //TODO scene-controller?
        SimulationOverlay simulationOverlay = new SimulationOverlay(primaryStage);
        //TODO remove tmp loading simulation overlay (entry is config)
        //primaryStage.setScene(simulationOverlay.getSimulationScene());

        startSceneController.start_borderPane_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(simulationOverlay.getSimulationScene());
                primaryStage.setFullScreen(true);
            }
        });

        //Show the app
        primaryStage.show();



    }

    private DinoListItem makeDinoListItem() {
        return (DinoListItem) makeFXMLController("DinoListItem.fxml", DinoListItem.class);
    }

    private SliderNoImage makeSliderNoImage(String name){
        SliderNoImage sliderNoImage = (SliderNoImage) makeFXMLController("SliderNoImage.fxml", SliderNoImage.class);

        sliderNoImage.label.setText(name);

        return sliderNoImage;
    }

    /**
     *
     * @param filename Name of the FXML File
     * @param controllerClass Class of the Controller to the corresponding FXML File
     * @return Loads the FXML File into a controller of the given class and returns that controller instance
     */
    private Object makeFXMLController(String filename, Class controllerClass){
        FXMLLoader loader = new FXMLLoader();

        try {
            Object controller = controllerClass.getConstructor().newInstance();
            loader.setController(controller);

            loader.setLocation(getLocationURL(filename));

            loader.load();

            return controller;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param filename Name of the File, whose URL should be returned
     * @return call to {@code getLocationURL(filename, folder)} with {@code folder} being the resources folder containing
     * the gui fxml files
     */
    private URL getLocationURL(String filename){
        String currentWorkingDirectory = System.getProperty("user.dir");
        currentWorkingDirectory = currentWorkingDirectory.replace('\\', '/');
        String resourcesFolder = currentWorkingDirectory + "/src/main/resources/gui/";

        return getLocationURL(filename, resourcesFolder);
    }

    /**
     *
     * @param filename Name of the File, whose URL should be returned
     * @param folder Path to Folder, containing the file, ending with {@code "/"} or {@code "\"}
     * @return File URL to the file in the given folder
     */
    private URL getLocationURL(String filename, String folder){
        try {
            return new URL("file:///" + folder.replace('\\', '/') + filename);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public void stop() throws Exception {
        //TODO
        super.stop();
    }
}
