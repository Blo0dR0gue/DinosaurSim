package com.dhbw.thesim.core.map;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * Represents a background-object in the {@link SimulationMap}-grid
 *
 * @author Daniel Czeschner
 * @see SimulationMap
 */
public class Tile {

    //region variables
    private Image background;
    private boolean swimmable;
    private boolean climbable;
    //region constants
    /**
     * The square size for one tile (as specified in the requirement's specification)
     */
    public static final int TILE_SIZE = 45;
    //endregion

    //endregion

    public Tile(Image image) {
        this.background = image;
    }

    //region getter & setter
    public Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public boolean isSwimmable() {
        return swimmable;
    }

    public void setSwimmable(boolean swimmable) {
        this.swimmable = swimmable;
    }

    public boolean isClimbable() {
        return climbable;
    }

    public void setClimbable(boolean climbable) {
        this.climbable = climbable;
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
