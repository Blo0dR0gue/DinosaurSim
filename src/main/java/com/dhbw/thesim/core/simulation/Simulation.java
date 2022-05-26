package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Holds all information for one Simulation and provides functions each {@link SimulationObject} needs to know
 *
 * @author Daniel Czeschner
 * @see SimulationMap
 * @see SimulationObject
 * @see SimulationLoop
 */
public class Simulation {

    private final SimulationMap simulationMap;
    private final GraphicsContext backgroundGraphics;
    private final ArrayList<SimulationObject> simulationObjects;

    public Simulation(String landscapeName, GraphicsContext backgroundGraphicsContext, SimulationOverlay simulationOverlay) {
        //TODO load via json2objects
        this.simulationMap = new SimulationMap(landscapeName);
        this.simulationObjects = new ArrayList<>();

        this.simulationObjects.add(new Dinosaur());

        this.backgroundGraphics = backgroundGraphicsContext;

        drawMap();
        //TODO
        spawnObjects(simulationOverlay);
    }

    public void spawnObjects(SimulationOverlay simulationOverlay){
        for (SimulationObject obj: simulationObjects) {
            simulationOverlay.getChildren().add(obj.getJavaFXObj());
        }
    }

    /**
     * Draws the simulation background
     */
    private void drawMap(){

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

    public SimulationMap getSimulationMap() {
        return simulationMap;
    }

    public ArrayList<SimulationObject> getSimulationObjects() {
        return simulationObjects;
    }
}
