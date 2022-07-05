package com.dhbw.thesim.core.map;

import com.dhbw.thesim.gui.Display;
import javafx.scene.image.Image;

/**
 * Represents a background-object in the {@link SimulationMap}-grid.
 *
 * @author Daniel Czeschner
 * @see SimulationMap
 */
public class Tile {

    //region variables
    /**
     * The image, which is rendered for this {@link Tile}.
     */
    private final Image background;

    /**
     * Defines if this tile can only be crossed, if a {@link com.dhbw.thesim.core.entity.Dinosaur} can swim.
     */
    private final boolean swimmable;

    /**
     * Defines if this tile can ony be crossed, if a {@link com.dhbw.thesim.core.entity.Dinosaur} can climb.
     */
    private final boolean climbable;

    /**
     * Does this tile can contain plants?
     */
    private final boolean canContainPlants;

    /**
     * The gird x coordinate inside the 2-dimensional-array.
     */
    private final int gridX;

    /**
     * The gird y coordinate inside the 2-dimensional-array.
     */
    private final int gridY;

    //region constants
    /**
     * The square size for one tile (as specified in the requirement's specification)
     */
    public static final double TILE_SIZE = Display.adjustScale(45, Display.SCALE_X);
    //endregion

    //endregion

    /**
     * Constructor for a {@link Tile}
     *
     * @param image            The background for this image.
     * @param gridX            The grid x coordinate.
     * @param gridY            The grid x coordinate.
     * @param swimmable        Is this tile a water tile?
     * @param climbable        Is this tile a mountain tile?
     * @param canContainPlants Can this tile contain plants?
     */
    public Tile(Image image, int gridX, int gridY, boolean swimmable, boolean climbable, boolean canContainPlants) {
        this.background = image;
        this.gridX = gridX;
        this.gridY = gridY;
        this.swimmable = swimmable;
        this.climbable = climbable;
        this.canContainPlants = canContainPlants;
    }

    //region getter & setter

    /**
     * Gets the {@link #background} image for this {@link Tile}.
     *
     * @return The {@link #background} image.
     */
    public Image getBackground() {
        return background;
    }

    /**
     * Checks if this {@link Tile} is swimmable. (A water tile)
     *
     * @return true if it is swimmable.
     */
    public boolean isSwimmable() {
        return swimmable;
    }

    /**
     * Checks if this {@link Tile} is climbable. (A mountain tile)
     *
     * @return true if it is climbable.
     */
    public boolean isClimbable() {
        return climbable;
    }

    /**
     * Checks if this {@link Tile} allow plants growth.
     *
     * @return true if plants can grow on this tile.
     */
    public boolean arePlantsAllowed() {
        return canContainPlants;
    }

    /**
     * The gird x coordinate.
     *
     * @return The {@link #gridX} value.
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * The gird y coordinate.
     *
     * @return The {@link #gridY} value.
     */
    public int getGridY() {
        return gridY;
    }

    //endregion
}
