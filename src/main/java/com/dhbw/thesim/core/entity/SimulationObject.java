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
 * Represents an object, which is handled in our simulation
 *
 * @author Daniel Czeschner
 */
@SuppressWarnings("unused")
public abstract class SimulationObject extends StateMachine {

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

    /**
     * Constructor
     *
     * @param type             The type for this object.
     * @param interactionRange The range, in which collisions are handled.
     * @param image            The image, which is used for the representation for this object.
     */
    protected SimulationObject(String type, double interactionRange, Image image) {
        this.type = type;
        this.interactionRange = interactionRange;
        this.imageObj = new ImageView();

        this.position = new Vector2D(0, 0);
        this.renderOffset = new Vector2D(0, 0);

        imageObj.setPreserveRatio(true);
        imageObj.setFitHeight(Display.adjustScale(interactionRange * 2, Display.SCALE_X));
        imageObj.setFitWidth(Display.adjustScale(interactionRange * 2, Display.SCALE_Y));

        selectionRing = new Circle(0, 0, interactionRange);
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
     * Eats this object
     */
    public abstract void eat();

    /**
     * Sets the visibility for the selection ring of this object.
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
     * Checks, if a other object can eat this object.
     *
     * @param checkValue A value of another object, which is used to check, if the other object can eat this object.
     * @return true if this object can be eaten by the other object.
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

    public Vector2D getRenderOffset() {
        return renderOffset;
    }

    /**
     * Gets the interaction range, in which collisions are counted.
     *
     * @return The collision range.
     */
    public double getInteractionRange() {
        return interactionRange - 10;
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
     * Sets/Overrides the position of this {@link SimulationObject}
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
     * Gets the current JavaFX object.
     *
     * @return An {@link ImageView}. It's the representation for this {@link SimulationObject}
     */
    public ImageView getJavaFXObj() {
        return imageObj;
    }

    /**
     * Check, if the dinosaur is rendered outside the view range.
     *
     * @return true, if the {@link SimulationObject} gets rendered outside the view panel.
     */
    public boolean isRenderedOutside() {
        return (position.getX() - renderOffset.getX()) < 0 || (position.getY() - renderOffset.getY()) < 0 ||
                position.getX() + renderOffset.getX() > SimulationOverlay.BACKGROUND_WIDTH ||
                position.getY() + renderOffset.getY() > SimulationOverlay.BACKGROUND_HEIGHT;
    }

    public static boolean willBeRenderedOutside(Vector2D targetPosition, Vector2D renderOffset) {
        return (targetPosition.getX() - renderOffset.getX()) < 0 || (targetPosition.getY() - renderOffset.getY()) < 0 ||
                targetPosition.getX() + renderOffset.getX() > SimulationOverlay.BACKGROUND_WIDTH ||
                targetPosition.getY() + renderOffset.getY() > SimulationOverlay.BACKGROUND_HEIGHT;
    }
}