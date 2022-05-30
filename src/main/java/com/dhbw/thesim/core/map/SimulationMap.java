package com.dhbw.thesim.core.map;

import javafx.scene.image.Image;

import java.util.Arrays;

/**
 * Represents a landscape
 *
 * @author Daniel Czeschner
 */
public class SimulationMap {

    //region variables
    /**
     * The name if the current landscape
     */
    private String landscapeName;
    /**
     * The background grid-field
     *
     * @see Tile
     */
    private final Tile[][] tiles;

    //region constants

    /**
     * The width of the grid-field (as specified in the requirement's specification)
     */
    public static final int width = 36;
    /**
     * The height of the grid-field (as specified in the requirement's specification)
     */
    public static final int height = 24;

    //endregion

    //endregion

    /**
     * The constructor for a new simulation map
     *
     * @param landscapeName The name for this landscape
     */
    public SimulationMap(String landscapeName) {
        this.landscapeName = landscapeName;
        this.tiles = new Tile[width][height];
        initMap();
    }

    //TODO make dynamic
    private void initMap() {
        Image tmp = Tile.tmpSprite();
        for (Tile[] row : tiles) {
            Arrays.fill(row, new Tile(tmp));
        }
    }

    //region getter & setter

    /**
     * Gets the name of the landscape.
     *
     * @return The name of this map.
     */
    public String getLandscapeName() {
        return landscapeName;
    }

    /**
     * Gets all Tiles as an 2-dimensional array.
     *
     * @return The 2-Dimensional array with all Tiles. Which represents the current map.
     */
    public Tile[][] getTiles() {
        return tiles;
    }
    //endregion
}
