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
     * Defines, if this tile can only be crossed, if a {@link com.dhbw.thesim.core.entity.Dinosaur} can swim.
     */
    private final boolean swimmable;

    /**
     * Defines, iff this tile can ony be crossed, if a {@link com.dhbw.thesim.core.entity.Dinosaur} can climb.
     */
    private final boolean climbable;

    /**
     * Does this tile can contain plants?
     */
    private final boolean canContainPlants;

    private final int gridX;
    private final int gridY;

    //region constants
    /**
     * The square size for one tile (as specified in the requirement's specification)
     */
    public static final double TILE_SIZE = Display.adjustScale(45, Display.SCALE_X);
    //endregion
    //endregion

    /**
     * Constructor
     * @param image The background for this {@link Tile} object.
     * @see #background
     */
    public Tile(Image image, int gridX, int gridY, boolean swimmable, boolean climbable, boolean canContainPlants) {
        this.background = image;
        this.gridX = gridX;
        this.gridY = gridY;
        this.swimmable = swimmable;
        this.climbable = climbable;
        this.canContainPlants = canContainPlants;
    }

    /**
     * Getter and setter methods for {@link Tile}-objects.
     */
    public Image getBackground() {
        return background;
    }

    public boolean isSwimmable() {
        return swimmable;
    }

    public boolean isClimbable() {
        return climbable;
    }

    public boolean arePlantsAllowed() {
        return canContainPlants;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
