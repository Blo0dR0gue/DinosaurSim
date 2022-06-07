package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.map.SimulationMap;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.simulation.SimulationLoop;
import com.dhbw.thesim.core.statemachine.StateMachine;
import com.dhbw.thesim.core.util.Vector2D;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.Objects;

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

    //TODO remove test objects
    protected Rectangle test = new Rectangle(0, 0, 10, 10);
    protected Circle circle;

    /**
     * Constructor
     *
     * @param type             The type for this object.
     * @param interactionRange The range, in which collisions are handled.
     * @param imgPath          The full path to an image. E.g. /dinosaur/test.png   //TODO maybe pass a image via spritelibF?
     */
    public SimulationObject(String type, double interactionRange, String imgPath) {
        this.type = type;
        this.interactionRange = interactionRange;
        this.imageObj = new ImageView();

        this.position = new Vector2D(0, 0);
        this.renderOffset = new Vector2D(0, 0);

        test.setFill(Color.BLUE);

        //TODO use sprite lib and don't create the image here.
        Image image = new Image(Objects.requireNonNull(getClass().getResource(imgPath)).toString());
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
        renderOffset.setX(image.getWidth() / 2);
        renderOffset.setY(image.getHeight() / 2);
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
        return interactionRange;
    }

    /**
     * Flips the image vertically.
     */
    public void flipImage() {
        Translate flipTranslation = new Translate(0, imageObj.getImage().getHeight());
        Rotate flipRotation = new Rotate(180, Rotate.X_AXIS);
        imageObj.getTransforms().addAll(flipTranslation, flipRotation);
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
        if (Math.abs(position.getX()) > SimulationMap.width * Tile.TILE_SIZE && Math.abs(position.getY()) > SimulationMap.height * Tile.TILE_SIZE) {
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

    public Rectangle getTest() {
        return test;
    }

    public Circle getCircle() {
        return circle;
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

    //endregion
}
