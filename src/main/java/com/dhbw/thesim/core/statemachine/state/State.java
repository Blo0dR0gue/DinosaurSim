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

    //region variables

    /**
     * List with all {@link StateTransition}s for this {@link State}. They are checked in the order they were added.
     */
    private final List<StateTransition> stateTransitionList;

    /**
     * The {@link SimulationObject} to which this state belongs. (Handled SimulationObject)
     */
    protected SimulationObject simulationObject;

    //endregion

    /**
     * Constructor for a normal {@link State}-object.
     * @param simulationObject The handled {@link SimulationObject}
     */
    public State(SimulationObject simulationObject) {
        this.stateTransitionList = new ArrayList<>();
        this.simulationObject = simulationObject;
        //Init all transitions
        initTransitions();
    }

    /**
     * Is called on state exit
     */
    public abstract void onExit();

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    public abstract void update(double deltaTime, Simulation simulation);

    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
    public abstract void initTransitions();

    /**
     * Adds a {@link StateTransition} to the {@link #stateTransitionList} of this {@link State}. <br>
     * Transitions are checked by the {@link #checkTransitions(Simulation)} function in the order they were added.
     * @param stateTransition The {@link StateTransition} which should be added.
     */
    public void addTransition(StateTransition stateTransition) {
        this.stateTransitionList.add(stateTransition);
    }

    /**
     * Checks, if any transition is met. If yes, return the next state for this transition otherwise return null.
     * @param simulation The current {@link Simulation} data.
     * @return The next {@link State} or null.
     */
    public State checkTransitions(Simulation simulation) {
        for (StateTransition stateTransition : stateTransitionList) {
            if (stateTransition.shouldTransition(simulation)) {
                return stateTransition.getNextState(simulationObject);
            }
        }
        return null;
    }
}