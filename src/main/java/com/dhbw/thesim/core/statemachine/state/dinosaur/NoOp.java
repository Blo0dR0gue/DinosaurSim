package com.dhbw.thesim.core.statemachine.state.dinosaur;


import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this state a {@link Dinosaur} got caught by another dinosaur. <br>
 * The dinosaur should stay on the map until the ingestion state of the hunter finished.
 *
 * @author Daniel Czeschner
 */
public class NoOp extends State {

    //region variables

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    //endregion

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public NoOp(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = simulationObject;
    }


    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     *
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation simulation) {
        //Nothing to do here
    }

    /**
     * Is called on state exit
     */
    @Override
    public void onExit() {
        //Nothing to do here
    }

    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
    @Override
    public void initTransitions() {
        //The dinosaur got eaten.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));
        //The dinosaur died before this one got eaten.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> !dinosaur.isForcedToNoOp()));
    }
}