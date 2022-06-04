package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * Represents a {@link State} an {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is moving to a random position in range.
 */
public class Wander extends State {

    /**
     * The target {@link Vector2D} position.
     */
    private Vector2D target;

    /**
     * The normalized direction {@link Vector2D}
     */
    private Vector2D direction;

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    public Wander(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (target == null) {
            target = simulation.getRandomMovementTargetInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset());
            if(target != null){
                direction = simulationObject.getPosition().directionToTarget(target);
                System.out.println("Moving to " + target);
                dinosaur.setTest(target);
            }

        }
        if(direction != null)
            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));
    }

    @Override
    public void initTransitions() {
        //When target is reached -> transition to Stand-state.

        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        addTransition(new StateTransition(StateFactory.States.stand, simulation -> arrived()));
        addTransition(new StateTransition(StateFactory.States.stand, simulation -> target==null));

        addTransition(new StateTransition(StateFactory.States.wander, simulation -> {
            boolean back = !simulation.canMoveTo(dinosaur.getPosition(), target, dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), false, false);
            if(back)
                System.out.println("cant move there");
            return back;
        }));

        //TODO
        //&& (simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb()) != null || simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null)
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty() && dinosaur.isHungry()
                && (simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb()) != null || simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null)));

        //&& simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isThirsty()
                && simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null));

        //&& simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb()) != null
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.isHungry()
                && simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb()) != null));
    }

    /**
     * Are we in range of our {@link #target}
     *
     * @return true, if we are in the {@link Dinosaur#PROXIMITY_RANGE} to the {@link #target}.
     */
    private boolean arrived() {
        return target != null && simulationObject.getPosition().isInRangeOf(target, Dinosaur.PROXIMITY_RANGE);
    }

}
