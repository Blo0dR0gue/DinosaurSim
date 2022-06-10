package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

import java.util.Random;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is waiting {@link #WAIT_TIME_IN_SECONDS} seconds.
 *
 * @author Daniel Czeschner
 */
public class Stand extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * Time since we entered this state.
     */
    private double timeSinceStart;

    /**
     * A {@link Random} object used to define the {@link #WAIT_TIME_IN_SECONDS}.
     */
    private static final Random RANDOM =  new Random();

    /**
     * Max time, we stay in this state.
     */
    private static final double WAIT_TIME_IN_SECONDS = RANDOM.nextDouble(1,5);

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Stand(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        if (timeSinceStart <= WAIT_TIME_IN_SECONDS)
            timeSinceStart += deltaTime;
    }

    @Override
    public void onExit() {
        //Nothing to do here
    }

    @Override
    public void initTransitions() {
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //If the dinosaur is hungry and thirsty and a water tile or a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty() && dinosaur.isHungry()
                && (simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()) != null || simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null)));

        //If the dinosaur is thirsty and a water tile is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty()
                && simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null));

        //If the dinosaur is hungry and a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isHungry()
                && simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()) != null));

        addTransition(new StateTransition(StateFactory.States.wander, (simulation) -> timeSinceStart >= WAIT_TIME_IN_SECONDS));
    }
}