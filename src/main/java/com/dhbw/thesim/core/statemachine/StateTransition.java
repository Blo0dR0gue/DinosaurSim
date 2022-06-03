package com.dhbw.thesim.core.statemachine;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * This record represents a transition between {@link State}s.
 *
 * @author Daniel Czeschner
 */
public record StateTransition(StateFactory.States nextState,
                              ITransition stateTransition) {

    /**
     * Should we transition to the {@link #nextState}.
     * @return true, if the condition of {@link #stateTransition} is met.
     */
    public boolean shouldTransition() {
        return stateTransition.isMet();
    }

    /**
     * Gets the next {@link State} using the {@link StateFactory}.
     * @param simulationObject The {@link SimulationObject} which is transition to the next state.
     * @return The next {@link State}.
     */
    public State getNextState(SimulationObject simulationObject) {
        return StateFactory.createState(nextState, simulationObject);
    }
}
