package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * Represents a {@link State} an {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is waiting {@link #waitTimeInSeconds} seconds.
 */
public class Stand extends State {

    /**
     * Time since we entered this state.
     */
    private double timeSinceStart;

    /**
     * Max time, we stay in this state.
     */
    private static final double waitTimeInSeconds = 5;

    public Stand(Dinosaur simulationObject) {
        super(simulationObject);
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        if (timeSinceStart <= waitTimeInSeconds)
            timeSinceStart += deltaTime;
    }

    @Override
    public void initTransitions() {
        addTransition(new StateTransition(StateFactory.States.stand, () -> timeSinceStart >= waitTimeInSeconds));
    }
}
