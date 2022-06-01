package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Holds all information for one Simulation and provides functions each {@link SimulationObject} needs to know which are using simulation data.
 *
 * @author Daniel Czeschner
 * @see SimulationMap
 * @see SimulationObject
 * @see SimulationLoop
 */
public class Simulation {

    //region variables
    /**
     * The used {@link SimulationMap}
     *
     * @see SimulationMap
     */
    private final SimulationMap simulationMap;
    /**
     * The {@link GraphicsContext} for the background canvas
     *
     * @see SimulationOverlay
     * @see javafx.scene.canvas.Canvas
     * @see GraphicsContext
     */
    private final GraphicsContext backgroundGraphics;
    /**
     * The List, with all handed {@link SimulationObject}s used in a Simulation.
     *
     * @see SimulationObject
     */
    private final List<SimulationObject> simulationObjects;

    //endregion
    /**
     * Constructor
     *
     * @param landscapeName The name of the used landscape.
     * @param backgroundGraphicsContext The {@link GraphicsContext} for the background canvas.
     * @param simulationOverlay The {@link SimulationOverlay} object on which {@link SimulationObject}s are spawned.
     * @param dinosaurs Map with all dinosaurs, which should be added to this simulation. Key = Dinosaur-Name Value = Amount.
     * @param plants Map with all plants, which should be added to this simulation. Key = Dinosaur-Name Value = Amount.
     * @param plantGrowthRate The growth rate for each plant.
     */
    public Simulation(String landscapeName, GraphicsContext backgroundGraphicsContext, SimulationOverlay simulationOverlay, HashMap<String, Integer> dinosaurs, HashMap<String, Integer> plants, int plantGrowthRate) {
        //TODO load via json2objects
        this.simulationMap = new SimulationMap(landscapeName);
        this.simulationObjects = new ArrayList<>();

        //TODO handle map calls to json2object

        //TODO remove temp code
        this.simulationObjects.add(new Dinosaur());

        this.backgroundGraphics = backgroundGraphicsContext;

        //Draw the map
        drawMap();
        //TODO
        spawnObjects(simulationOverlay);
    }

    /**
     * Method, that spawns the {@link SimulationObject}s of the list {@link Simulation#simulationObjects}.
     *
     * @param simulationOverlay The {@link SimulationOverlay} object on which the {@link SimulationObject} are spawned.
     */
    public void spawnObjects(SimulationOverlay simulationOverlay) {
        for (SimulationObject obj : simulationObjects) {
            simulationOverlay.getChildren().add(obj.getJavaFXObj());
        }
    }

    /**
     * Draws the simulation background
     */
    private void drawMap() {

        backgroundGraphics.setFill(Color.BLACK);
        backgroundGraphics.fillRect(0, 0, SimulationOverlay.BACKGROUND_WIDTH, SimulationOverlay.BACKGROUND_HEIGHT);

        Tile[][] tiles = simulationMap.getTiles();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                backgroundGraphics.drawImage(
                        tiles[x][y].getBackground(),
                        x * Tile.TILE_SIZE,
                        y * Tile.TILE_SIZE
                );
            }
        }
    }

    /**
     * Gets the used {@link SimulationMap}.
     *
     * @return The currently used {@link SimulationMap}
     */
    public SimulationMap getSimulationMap() {
        return simulationMap;
    }

    /**
     * Gets all handled {@link SimulationObject}s.
     *
     * @return The list {@link Simulation#simulationObjects}.
     */
    public List<SimulationObject> getSimulationObjects() {
        return simulationObjects;
    }
}
