package com.dhbw.thesim.core.map;

import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

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
    private Image background;
    /**
     * Defines, if this tile can only be crossed, if a {@link com.dhbw.thesim.core.entity.Dinosaur} can swim.
     */
    private boolean swimmable;
    /**
     * Defines, iff this tile can ony be crossed, if a {@link com.dhbw.thesim.core.entity.Dinosaur} can climb.
     */
    private boolean climbable;

    private final int gridX;
    private final int gridY;

    //region constants
    /**
     * The square size for one tile (as specified in the requirement's specification)
     */
    public static final double TILE_SIZE = SimulationOverlay.adjustScale(45, SimulationOverlay.SCALE_X);
    //endregion

    //endregion

    /**
     * Constructor
     *
     * @param image The background for this {@link Tile} object.
     * @see #background
     */
    public Tile(Image image, int gridX, int gridY, boolean swimmable, boolean climbable) {
        this.background = image;
        this.gridX = gridX;
        this.gridY = gridY;
        this.swimmable = swimmable;
        this.climbable = climbable;
    }

    //region getter & setter
    public Image getBackground() {
        return background;
    }

    public boolean isSwimmable() {
        return swimmable;
    }

    public boolean isClimbable() {
        return climbable;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    //endregion

    /**
     * TODO REMOVE
     *
     * @return
     */
    public static Image tmpSprite() {
        Canvas c = new Canvas(TILE_SIZE, TILE_SIZE);
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.clearRect(0, 0, TILE_SIZE, TILE_SIZE);
        gc.setStroke(Color.RED);
        gc.strokeRect(0, 0, TILE_SIZE, TILE_SIZE);

        WritableImage writableImage = new WritableImage((int) Math.rint(1 * c.getWidth()), (int) Math.rint(1 * c.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(1, 1));
        spa.setFill(Color.TRANSPARENT);
        return c.snapshot(spa, writableImage);
    }
}
