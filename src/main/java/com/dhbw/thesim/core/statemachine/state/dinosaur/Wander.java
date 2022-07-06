package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is moving to a random position in range.
 *
 * @author Daniel Czeschner
 */
public class Wander extends State {

    //region variables.

    /**
     * The targeted position {@link Vector2D} the {@link Dinosaur} is moving to.
     */
    private Vector2D target;

    /**
     * The direction the {@link #target} from the current position to this {@link #dinosaur}.
     */
    private Vector2D direction;

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
    public Wander(Dinosaur simulationObject) {
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
        if (target == null) {
            target = simulation.getRandomMovementTargetInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset());
            if (target != null) {
                direction = simulationObject.getPosition().directionToTarget(target);
                dinosaur.flipImage(direction);
            }
        }
        if (direction != null)
            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));
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

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //If we have no target, go to stand.
        addTransition(new StateTransition(StateFactory.States.stand, simulation -> target == null));

        //When target is reached -> transition to Stand-state.
        addTransition(new StateTransition(StateFactory.States.stand, simulation -> targetReached()));

        //If the dinosaur can no longer move to the target. (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.wander,
                simulation -> !simulation.canMoveTo(dinosaur.getPosition(), target, dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), false, false, null)
        ));

        //If the dinosaur is hungry and thirsty and a water tile or a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty() && dinosaur.isHungry()
                && (simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()) != null || simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null)));

        //If the dinosaur is thirsty and a water tile is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty()
                && simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null));

        //If the dinosaur is hungry and a food source is in range, transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isHungry()
                && simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()) != null));

        //If there is a partner in range, transition to moveToPartner
        addTransition(new StateTransition(StateFactory.States.moveToPartner, simulation -> (dinosaur.isWillingToMate()
                && simulation.getClosestReachableSuitablePartnerInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getGender()) != null)
                || dinosaur.getPartner() != null));

    }

    /**
     * Checks if the dinosaur reached the {@link #target}
     *
     * @return true if the dinosaur is in the {@link Dinosaur#PROXIMITY_RANGE} to the {@link #target}.
     */
    private boolean targetReached() {
        return target != null && simulationObject.getPosition().isInRangeOf(target, Dinosaur.PROXIMITY_RANGE);
    }
}