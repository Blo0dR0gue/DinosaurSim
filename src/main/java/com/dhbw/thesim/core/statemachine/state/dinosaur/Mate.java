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
 * @author Daniel Czeschner, Lucas Schaffer
 */
public class Mate extends State {

    /**
     * The time the dinosaur needs to mate.
     */
    private double mateTime = 2;

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
    public Mate(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (dinosaur.getGender() == 'f') {
            mateTime -= deltaTime;

            if (mateTime <= 0 && !done) {
                if (dinosaur.getPartner().getPartner() == dinosaur) {
                    simulation.makeBaby(dinosaur, dinosaur.getPartner());
                    dinosaur.getPartner().setReproductionValue(0);
                    dinosaur.setReproductionValue(0);
                }
                done = true;
            }

        }

    }

    @Override
    public void onExit() {
        if (dinosaur.getPartner() != null && dinosaur.getPartner().getPartner() == dinosaur) {
            dinosaur.getPartner().setPartner(null);
        }
        dinosaur.setPartner(null);
    }

    @Override
    public void initTransitions() {
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //If the partner isn't willing to mate anymore, transition to wander
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getPartner() != null && !dinosaur.getPartner().isWillingToMate()));

        //If the dinosaur is thirsty and a water tile is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> done && dinosaur.isThirsty()));

        //If the dinosaur is hungry and a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> done && dinosaur.isHungry()));

        //If we are done, go to the wander-state.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> done));

        //If the partner is null, got to the wander-state. (Exit for male dinosaurs)
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getPartner() == null));


    }

    private boolean stillInReach(Simulation simulation) {

        return simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getInteractionRange(), dinosaur.getPartner().getPosition(), dinosaur.getPartner().getInteractionRange());
    }

}
