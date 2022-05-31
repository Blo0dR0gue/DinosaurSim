package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.dinosaur.Wander;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Represents a dinosaur object in our simulation.
 * Takes care to update each State and switch between them.
 *
 * @author Daniel Czeschner
 * @see com.dhbw.thesim.core.statemachine.StateMachine
 * @see State
 */
public class Dinosaur extends SimulationObject {


    public final static int PROXIMITY_RANGE = 5;

    /**
     * Constructor for a dinosaur object
     */
    public Dinosaur() {
        super();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/dinosaur/test.png")).toString());
        setSprite(image);
        //TODO
        setState(new Wander(this));
    }

    /**
     * Handles the updates for each {@link State} of a {@link Dinosaur}-object.
     *
     * @param deltaTime             The time since the last update call in seconds.
     * @param currentSimulationData The current {@link Simulation}-Object with all information to the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation currentSimulationData) {
        //Check all transitions for the current state and switch to next state is one met.
        stateMachineTick();
        //TODO move to stateMachineTick?
        currentState.update(deltaTime, currentSimulationData);
    }

    /**
     * Updates the position of the representation for a {@link Dinosaur}-object. <br>
     * It also changes the image if necessary.
     */
    @Override
    public void updateGraphics() {
        //Center the image.
        imageObj.setTranslateX(position.getX() - renderOffset.getX());
        imageObj.setTranslateY(position.getY() - renderOffset.getY());
    }

}
