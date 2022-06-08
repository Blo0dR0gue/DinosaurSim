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

    StateFactory.States nextState;
    ITransition stateTransition;
    IOnStateEnter onStateEnter;

    public StateTransition(StateFactory.States nextState,
                           ITransition stateTransition, IOnStateEnter onStateEnter){
        this.nextState = nextState;
        this.stateTransition = stateTransition;
        this.onStateEnter = onStateEnter;
    }

    public StateTransition(StateFactory.States nextState, ITransition stateTransition){
        this.nextState = nextState;
        this.stateTransition = stateTransition;
        this.onStateEnter = null;
    }

    /**
     * Should we transition to the {@link #nextState}.
     *
     * @return true, if the condition of {@link #stateTransition} is met.
     */
    public boolean shouldTransition(Simulation simulation) {
        return stateTransition.isMet(simulation);
    }

    public void onStateEntered(Simulation simulation){
        if(onStateEnter != null)
            onStateEnter.stateEntered(simulation);
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
