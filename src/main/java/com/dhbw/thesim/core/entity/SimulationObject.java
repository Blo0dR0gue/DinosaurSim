package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an object, which is handled in our simulation
 *
 * @author Daniel Czeschner
 */
public abstract class SimulationObject {

    /**
     * The representation object for this {@link SimulationObject}.
     * Contains an {@link Image}
     */
    protected ImageView imageObj;
    /**
     * The position object for this {@link SimulationObject}.
     * @see Vector2D
     */
    protected Vector2D position;

    /**
     * Constructor
     */
    public SimulationObject() {
        super();
        imageObj = new ImageView();
        //TODO
        position = new Vector2D(0,0);
    }

    /**
     * Is called each update in the {@link SimulationLoop}.
     * @param deltaTime The time since the last update call in seconds.
     * @param currentSimulation The current {@link Simulation}-Object with all information to the currently running simulation.
     */
    public abstract void update(double deltaTime, Simulation currentSimulation);

    /**
     * Is called each render call. (FPS)
     */
    public abstract void updateGraphics();

    //region getter & setter

    /**
     * Sets/Updates and image for the representation of this {@link SimulationObject}
     * @param image
     */
    public void setSprite(Image image) {
        imageObj.setImage(image);
    }

    /**
     * Gets the current position on the screen. (Doesn't have to be the position of the graphics)
     * Use this for behaviour updates.
     * @return The current {@link Vector2D} position.
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Sets/Overrides the position of this {@link SimulationObject}
     * @param position The new {@link Vector2D} for the position.
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * Gets the current JavaFX object.
     * @return An {@link ImageView}. It's the representation for this {@link SimulationObject}
     */
    public ImageView getJavaFXObj(){
        return imageObj;
    }

    //endregion
}
