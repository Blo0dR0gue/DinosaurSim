package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

import java.util.Random;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is waiting {@link #waitTimeInSeconds} seconds.
 *
 * @author Daniel Czeschner
 */
public class Stand extends State {

    //region variables

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * Time since we entered this state.
     */
    private double timeSinceStart;

    /**
     * A {@link Random} object used to define the {@link #waitTimeInSeconds}.
     */
    private static final Random RANDOM = new Random();

    /**
     * Max time, we stay in this state.
     */
    private final double waitTimeInSeconds = RANDOM.nextDouble(1,5);

    //endregion

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Stand(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     *
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation simulation) {
        if (timeSinceStart <= waitTimeInSeconds)
            timeSinceStart += deltaTime;
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
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        //The dinosaur is hunted.
        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //If the dinosaur is hungry and thirsty and a water tile or a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty() && dinosaur.isHungry()
                && (simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()) != null || simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null)));

        //If the dinosaur is thirsty and a water tile is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty()
                && simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null));

        //If the dinosaur is hungry and a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isHungry()
                && simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()) != null));

        //Move to a partner, if a suitable partner is in range.
        addTransition(new StateTransition(StateFactory.States.moveToPartner, simulation -> (dinosaur.isWillingToMate()
                && simulation.getClosestReachableSuitablePartnerInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getGender()) != null)
                || dinosaur.getPartner() != null));

        //Go to wander, if the time is up.
        addTransition(new StateTransition(StateFactory.States.wander, (simulation) -> timeSinceStart >= waitTimeInSeconds));
    }
}