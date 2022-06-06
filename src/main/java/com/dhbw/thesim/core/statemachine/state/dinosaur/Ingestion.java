package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries to eat/drink a source.
 *
 * @author Daniel Czeschner
 */
public class Ingestion extends State {

    /**
     * The time the dinosaur needs to eat/drink the source.
     */
    private double ingestionTime = 2;

    /**
     * Helper to trigger a transition, if the dinosaur finished this state.
     */
    private boolean done = false;

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Ingestion(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        ingestionTime -= deltaTime;

        if (ingestionTime <= 0) {
            if (dinosaur.getTarget() == null) {
                //we drink
                dinosaur.setHydration(dinosaur.getMaxHydration());
            } else {
                //we eat
                dinosaur.getTarget().eat();
                dinosaur.setNutrition(dinosaur.getMaxNutrition());
                dinosaur.setTarget(null);
            }
            done = true;
        }

    }

    @Override
    public void initTransitions() {
        //TODO check transitions / transitions oder


        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //If the dinosaur have a simulationobject target (e.g. a dinosaur or plant, and it can no longer be eaten (because the object got eaten), transition to wander.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getTarget() != null && !dinosaur.getTarget().canBeEaten(dinosaur.getStrength())));

        //If the dinosaur is thirsty and a water tile is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> done && dinosaur.isThirsty()));

        //If the dinosaur is hungry and a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> done && dinosaur.isHungry()));

        //If we are done, go to the wander-state.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> done));


    }

}
