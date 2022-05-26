package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Handles one simulation
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

    public Simulation(String landscapeName, GraphicsContext backgroundGraphicsContext) {
        //TODO load via json2objects
        this.simulationMap = new SimulationMap(landscapeName);
        this.simulationObjects = new ArrayList<>();

        this.backgroundGraphics = backgroundGraphicsContext;

        drawMap();
    }

    public void spawnObjects(){
        //TODO
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
}