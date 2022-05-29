package com.dhbw.thesim.core.statemachine;

import com.dhbw.thesim.core.statemachine.state.State;

/**
 * This class holds the current State for a dinosaur.
 * It handles the transitions between behaviors.
 * For a deeper look, see the definition of the State Pattern.
 *
 * @author Daniel Czeschner
 * @see State
 */
public class StateMachine {

    /**
     * The current State an {@link com.dhbw.thesim.core.entity.SimulationObject} is in.
     */
    State currentState;

}
