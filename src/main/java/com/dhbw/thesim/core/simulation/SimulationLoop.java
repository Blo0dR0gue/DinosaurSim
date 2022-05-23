package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class SimulationLoop {

    private final SimulationMap simulationMap;
    private int simulationSpeed;
    private int stepRange;

    private final GraphicsContext backgroundGraphics;

    public SimulationLoop(String landscapeName, GraphicsContext graphicsContext){
        //TODO load via json2objects
        simulationMap = new SimulationMap(landscapeName);

        backgroundGraphics = graphicsContext;

        drawMap();
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
