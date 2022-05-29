package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import javafx.scene.image.Image;
import java.util.Objects;

/**
 * Represents a dinosaur object in our simulation.
 * Takes care to update each State and switch between them.
 *
 * @author Daniel Czeschner
 * @see com.dhbw.thesim.core.statemachine.StateMachine
 * @see com.dhbw.thesim.core.statemachine.state.State
 */
public class Dinosaur extends SimulationObject {

    /**
     * Constructor for a dinosaur object
     */
    public Dinosaur() {
        super();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/dinosaur/test.png")).toString());
        setSprite(image);
    }

    /**
     * Handles the updates for each {@link com.dhbw.thesim.core.statemachine.state.State} of a {@link Dinosaur}-object.
     * @param deltaTime The time since the last update call in seconds.
     * @param currentSimulationData The current {@link Simulation}-Object with all information to the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation currentSimulationData) {
        //TODO remove tests
        double lastX = this.getPosition().getX();
        this.getPosition().setX(lastX+25*deltaTime);
    }

    /**
     * Updates the position of a {@link Dinosaur}-object.
     */
    @Override
    public void updateGraphics() {
        imageObj.setTranslateX(position.getX());
        imageObj.setTranslateY(position.getY());
    }

}
