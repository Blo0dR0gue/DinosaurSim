package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.core.statemachine.StateMachine;
import com.dhbw.thesim.core.util.Vector2D;
import com.dhbw.thesim.gui.Display;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents an object, which is handled in our simulation. <br>
 * Extends the {@link StateMachine}.
 *
 * @author Daniel Czeschner
 * @see StateMachine
 */
public abstract class SimulationObject extends StateMachine {

    //region variables

    /**
     * The representation object for this {@link SimulationObject}.
     * Contains an {@link Image}
     */
    protected ImageView imageObj;

    /**
     * The position for this {@link SimulationObject}.
     *
     * @see Vector2D
     */
    protected Vector2D position;

    /**
     * Offset for the image to center it on the actual position of this {@link SimulationObject}. <br>
     * The {@link ImageView} has his origin on the top left.
     */
    protected Vector2D renderOffset;

    /**
     * The type of this {@link SimulationObject}. E.g. "Brachiosaurus"
     */
    protected final String type;

    /**
     * The collision range of this {@link SimulationObject}, in which collisions are counted.
     */
    protected final double interactionRange;

    /**
     * Used, when a dinosaur gets selected.
     */
    protected final Circle selectionRing;

    //endregion

    /**
     * Constructor
     *
     * @param type             The type of this {@link SimulationObject}.
     * @param interactionRange The range, in which collisions with other {@link SimulationObject} are handled. (in pixels)
     * @param image            The image, which is used for the graphical representation for this {@link SimulationObject}.
     */
    protected SimulationObject(String type, double interactionRange, Image image) {
        this.type = type;
        this.interactionRange = interactionRange;
        this.imageObj = new ImageView();

        this.position = new Vector2D(0, 0);
        this.renderOffset = new Vector2D(0, 0);

        imageObj.setPreserveRatio(true);
        imageObj.setFitHeight(Display.adjustScale(interactionRange * 2, Display.SCALE_Y==0?1:Display.SCALE_Y));
        imageObj.setFitWidth(Display.adjustScale(interactionRange * 2, Display.SCALE_X==0?1:Display.SCALE_X));

        selectionRing = new Circle(0, 0, getInteractionRange());
        selectionRing.setVisible(false);
        selectionRing.setFill(Color.TRANSPARENT);
        selectionRing.setStroke(Color.YELLOW);

        setSprite(image);
    }

    /**
     * Is called each update in the {@link SimulationLoop}.
     *
     * @param deltaTime         The time since the last update call in seconds.
     * @param currentSimulation The current {@link Simulation}-Object with all information to the currently running simulation.
     */
    public abstract void update(double deltaTime, Simulation currentSimulation);

    /**
     * Is called each render call. (FPS)
     */
    public abstract void updateGraphics();

    /**
     * Eats this {@link SimulationObject}.
     */
    public abstract void eat();

    //region getter & setter

    /**
     * Sets the visibility for the selection ring of this {@link SimulationObject}.
     *
     * @param visible true, if the rind should be visible.
     */
    public void setSelectionRingVisibility(boolean visible) {
        this.selectionRing.setVisible(visible);
    }

    /**
     * Gets the {@link #selectionRing} object
     *
     * @return The {@link #selectionRing} object.
     */
    public Circle getSelectionRing() {
        return this.selectionRing;
    }

    /**
     * Checks, if this {@link SimulationObject} can eat by another {@link SimulationObject}.
     *
     * @param checkValue A value of another {@link SimulationObject}, which is used to check, if the other {@link SimulationObject} can eat this {@link SimulationObject}.
     * @return true if this {@link SimulationObject} can be eaten by the other {@link SimulationObject}.
     */
    public abstract boolean canBeEaten(double checkValue);

    //region getter & setter

    /**
     * Sets/Updates and image for the representation of this {@link SimulationObject} <br>
     * This method also updates the {@link #renderOffset} to center the image/sprite. <br>
     * The position of the {@link SimulationObject} is in the center of the image.
     *
     * @param image The new image, which should be shown.
     */
    public void setSprite(Image image) {
        imageObj.setImage(image);

        double aspectRatio = image.getWidth() / image.getHeight();
        double realWidth = Math.min(imageObj.getFitWidth(), imageObj.getFitHeight() * aspectRatio);
        double realHeight = Math.min(imageObj.getFitHeight(), imageObj.getFitWidth() / aspectRatio);

        renderOffset.setX(realWidth / 2);
        renderOffset.setY(realHeight / 2 + Dinosaur.PROXIMITY_RANGE);
    }

    /**
     * Gets the current position on the screen. (Doesn't have to be the position of the graphics)  <br>
     * Use this for behaviour updates.
     *
     * @return The current {@link Vector2D} position.
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Gets the current render offset.
     *
     * @return The {@link #renderOffset} {@link Vector2D}.
     */
    public Vector2D getRenderOffset() {
        return renderOffset;
    }

    /**
     * Gets the interaction range, in which collisions are counted.
     *
     * @return The collision range.
     */
    public double getInteractionRange() {
        return Display.adjustScale(interactionRange - 10, Display.SCALE_X==0?1:Display.SCALE_X);
    }

    public double getRealInteractionRange() {
        return interactionRange;
    }

    /**
     * Flips the image facing a direction. <br>
     * The prerequisite is that the picture is facing to the right.
     *
     * @param direction The {@link Vector2D} direction.
     */
    public void flipImage(Vector2D direction) {
        if (direction.getX() < 0)
            imageObj.setScaleX(imageObj.getScaleX() < 0 ? imageObj.getScaleX() * -1 : imageObj.getScaleX());
        else
            imageObj.setScaleX(imageObj.getScaleX() < 0 ? imageObj.getScaleX() : imageObj.getScaleX() * -1);
    }

    /**
     * Gets the type.
     *
     * @return The type {@link #type}
     */
    public String getType() {
        return type;
    }

    /**
     * Sets/Overrides the position of this {@link SimulationObject}. <br>
     * Resets it to 0,0 if is outside the screen-borders.
     *
     * @param position The new {@link Vector2D} for the position.
     */
    public void setPosition(Vector2D position) {
        if (Math.abs(position.getX()) > SimulationMap.WIDTH * Tile.TILE_SIZE && Math.abs(position.getY()) > SimulationMap.HEIGHT * Tile.TILE_SIZE) {
            position = new Vector2D(0, 0);
        }
        this.position = position;
    }

    /**
     * Gets the current JavaFX object for the graphical representation for this {@link SimulationObject}.
     *
     * @return An {@link ImageView}.
     */
    public ImageView getJavaFXObj() {
        return imageObj;
    }

    /**
     * Checks if a point would be rendered outside the screen.
     *
     * @param targetPosition The target {@link Vector2D} position.
     * @param renderOffset   The {@link Vector2D} offset to the center of the target position.
     * @return true if it would be rendered outside.
     */
    public static boolean willBeRenderedOutside(Vector2D targetPosition, Vector2D renderOffset) {
        return (targetPosition.getX() - renderOffset.getX()) < 0 || (targetPosition.getY() - renderOffset.getY()) < 0 ||
                targetPosition.getX() + renderOffset.getX() > SimulationOverlay.BACKGROUND_WIDTH ||
                targetPosition.getY() + renderOffset.getY() > SimulationOverlay.BACKGROUND_HEIGHT;
    }

    //endregion

}