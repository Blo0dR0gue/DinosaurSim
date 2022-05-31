package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.core.statemachine.StateMachine;
import com.dhbw.thesim.core.util.Vector2D;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an object, which is handled in our simulation
 *
 * @author Daniel Czeschner
 */
public abstract class SimulationObject extends StateMachine {

    /**
     * The representation object for this {@link SimulationObject}.
     * Contains an {@link Image}
     */
    protected ImageView imageObj;
    /**
     * The position object for this {@link SimulationObject}.
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
     * The collision range of this {@link SimulationObject}, in which collisions are counted.
     */
    protected final double interactionRange;

    /**
     * Constructor
     */
    public SimulationObject(double interactionRange) {
        this.interactionRange = interactionRange;
        imageObj = new ImageView();
        //TODO
        position = new Vector2D(0, 0);
        renderOffset = new Vector2D(0,0);
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

    //region getter & setter

    /**
     * Sets/Updates and image for the representation of this {@link SimulationObject} <br>
     * This method also updates the {@link #renderOffset} to center the image/sprite.
     *
     * @param image The new image, which should be shown.
     */
    public void setSprite(Image image) {
        imageObj.setImage(image);
        renderOffset.setX(image.getWidth()/2);
        renderOffset.setY(image.getHeight()/2);
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

    public Vector2D getRenderOffset(){
        return renderOffset;
    }

    /**
     * Gets the interaction range, in which collisions are counted.
     * @return The collision range.
     */
    public double getInteractionRange() {
        return interactionRange;
    }

    /**
     * Sets/Overrides the position of this {@link SimulationObject}
     *
     * @param position The new {@link Vector2D} for the position.
     */
    public void setPosition(Vector2D position) {
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
     * @return true, if the {@link SimulationObject} gets rendered outside the view panel.
     */
    public boolean isRenderedOutside(){
        return (position.getX() - renderOffset.getX()) < 0 || (position.getY() - renderOffset.getY()) < 0 ||
                position.getX() + renderOffset.getX() > SimulationOverlay.BACKGROUND_WIDTH ||
                position.getY() + renderOffset.getY() > SimulationOverlay.BACKGROUND_HEIGHT;
    }

    public static boolean willBeRenderedOutside(Vector2D targetPosition, Vector2D renderOffset){
        return (targetPosition.getX() - renderOffset.getX()) < 0 || (targetPosition.getY() - renderOffset.getY()) < 0 ||
                targetPosition.getX() + renderOffset.getX() > SimulationOverlay.BACKGROUND_WIDTH ||
                targetPosition.getY() + renderOffset.getY() > SimulationOverlay.BACKGROUND_HEIGHT;
    }

    //endregion
}
