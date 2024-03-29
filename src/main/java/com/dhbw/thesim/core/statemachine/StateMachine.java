package com.dhbw.thesim.core.statemachine;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;

/**
 * This class holds the current State for a dinosaur. <br>
 * It handles the transitions between behaviors.
 * For a deeper look, see the definition of the "State Pattern".
 * Based on <a href="https://www.youtube.com/watch?v=G1bd75R10m4">Youtube - Infallible Code</a>
 *
 * @author Daniel Czeschner
 * @see State
 */
public abstract class StateMachine {

    //region variables

    /**
     * The current {@link State} a {@link com.dhbw.thesim.core.entity.SimulationObject} is in.
     */
    protected State currentState;

    //endregion

    /**
     * Sets the {@link #currentState}.
     *
     * @param state The {@link State}, which now should be used.
     */
    public void setState(State state) {
        this.currentState = state;
    }

    /**
     * Needs to be called each update call. <br>
     * This method handles state transitions.
     *
     * @param simulation The current {@link Simulation} data.
     */
    public void stateMachineTick(Simulation simulation) {
        State nextState = currentState.checkTransitions(simulation);
        if (nextState != null) {
            currentState.onExit();
            setState(nextState);
        }
    }
}