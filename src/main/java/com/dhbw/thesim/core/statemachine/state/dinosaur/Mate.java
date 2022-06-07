package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries mate with another {@link Dinosaur}.
 *
 * @author Daniel Czeschner
 */
public class Mate extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Mate(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        //TODO make it work
        //Neuen dino erzeugen, der die eigenschaften von vater und mutter erbt wie im entwurf beschrieben. (Mutationen, etc)
    }

    @Override
    public void initTransitions() {
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));


    }

}
