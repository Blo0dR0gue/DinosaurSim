package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries to escape his hunter.
 *
 * @author Daniel Czeschner
 */
public class Escape extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    private Vector2D target;
    private Vector2D directionOfHunter;
    private Vector2D direction;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Escape(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (target == null || reachedTarget()) {
            System.out.println("check");
            if (dinosaur.getTarget() != null && dinosaur.isChased() && dinosaur.getTarget() instanceof Dinosaur hunter) {
                directionOfHunter = hunter.getPosition().directionToTarget(dinosaur.getPosition());
                target = simulation.getRandomMovementTargetInRangeInDirection(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(),
                        dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), directionOfHunter);

                if (target == null) {
                    //If we can't get a direction away from the hunter get a random direction.
                    target = simulation.getRandomMovementTargetInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(),
                            dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset());
                }
            }
            if(target != null)
                dinosaur.setTest(target);
        }

        if (target != null) {

            direction = dinosaur.getPosition().directionToTarget(target);

            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));

        }

    }

    @Override
    public void initTransitions() {

        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        //Dinosaur got caught
        addTransition(new StateTransition(StateFactory.States.noop, simulation -> dinosaur.isForcedToNoOp()));

        //We have no target -> transition to stand.
        //We can do this here, because the update is called before the next check transitions
        addTransition(new StateTransition(StateFactory.States.stand, simulation -> target == null));

        //If the dinosaur can no longer move to the target. (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.escape, simulation -> !simulation.canMoveTo(dinosaur.getPosition(), target, dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), false, false)
        ));

        //If the dinosaur is no longer been chased.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> !dinosaur.isChased()));

    }


    private boolean reachedTarget() {
        return target != null && simulationObject.getPosition().isInRangeOf(target, Dinosaur.PROXIMITY_RANGE);
    }

}
