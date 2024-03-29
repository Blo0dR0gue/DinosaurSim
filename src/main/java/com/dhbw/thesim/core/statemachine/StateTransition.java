package com.dhbw.thesim.core.statemachine;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * This record represents a transition between {@link State}s.
 *
 * @author Daniel Czeschner
 */
public class StateTransition {

    //region variables

    /**
     * The next {@link State} if a {@link State} exits using this transition.
     */
    StateFactory.States nextState;

    /**
     * The {@link ITransition} which is used for the checks.
     */
    ITransition stateTransition;

    //endregion

    /**
     * Constructor for a normal {@link StateTransition}-object.
     *
     * @param nextState       The next {@link State}.
     * @param stateTransition The handled {@link ITransition}.
     */
    public StateTransition(StateFactory.States nextState, ITransition stateTransition) {
        this.nextState = nextState;
        this.stateTransition = stateTransition;
    }

    /**
     * Should we transition to the {@link #nextState}.
     *
     * @return true, if the condition of {@link #stateTransition} is met.
     */
    public boolean shouldTransition(Simulation simulation) {
        return stateTransition.isMet(simulation);
    }

    /**
     * Gets the next {@link State} using the {@link StateFactory}.
     *
     * @param simulationObject The {@link SimulationObject} which is transition to the next state.
     * @return The next {@link State}.
     */
    public State getNextState(SimulationObject simulationObject) {
        return StateFactory.createState(nextState, simulationObject);
    }
}