package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries to hunt another {@link Dinosaur}.
 *
 * @author Daniel Czeschner
 */
public class Hunt extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Hunt(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

    }

    @Override
    public void initTransitions() {

        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        //If we have a simulationobject target (e.g. a dinosaur or plant, and it can no longer be eaten (because the object got eaten), transition to wander.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getTarget() != null && !dinosaur.getTarget().canBeEaten(dinosaur.getStrength())));

    }

}
