package com.dhbw.thesim.core.statemachine.state;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single State for a {@link com.dhbw.thesim.core.entity.SimulationObject}. <br>
 * Each state handles one behaviour for a {@link com.dhbw.thesim.core.entity.SimulationObject}.
 *
 * @author Daniel Czeschner
 */
public abstract class State {

    /**
     * List with all {@link StateTransition}s for this {@link State}. They are checked in the order they were added.
     */
    private final List<StateTransition> stateTransitionList;

    /**
     * The {@link SimulationObject} to which this state belongs. (Handled SimulationObject)
     */
    protected SimulationObject simulationObject;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public State(SimulationObject simulationObject){
        this.stateTransitionList = new ArrayList<>();
        this.simulationObject = simulationObject;
        //Init all transitions
        initTransitions();
    }

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     * @param deltaTime The delta time since the last update call.
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    public abstract void update(double deltaTime, Simulation simulation);

    /**
     * Method to force states, which implements this abstract class to create transitions.
     */
    public abstract void initTransitions();

    /**
     * Adds a {@link StateTransition} to the {@link #stateTransitionList} of this {@link State}. <br>
     * Transitions are checked by the {@link #checkTransitions()} function in the order they were added.
     * @param stateTransition The {@link StateTransition} which should be added.
     */
    public void addTransition(StateTransition stateTransition){
        this.stateTransitionList.add(stateTransition);
    }

    /**
     * Checks, if any transition is met. If yes, return the next state for this transition otherwise return null.
     * @return The next {@link State} or null.
     */
    public State checkTransitions(){
        for (StateTransition stateTransition: stateTransitionList) {
            if(stateTransition.shouldTransition()){
                return stateTransition.getNextState(simulationObject);
            }
        }
        return null;
    }

}
