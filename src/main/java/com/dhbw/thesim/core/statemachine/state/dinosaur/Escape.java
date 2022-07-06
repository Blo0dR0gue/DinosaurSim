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

    //region variables.

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * The targeted position {@link Vector2D} the {@link Dinosaur} is moving to.
     */
    private Vector2D target;

    /**
     * The direction the {@link #target} from the current position to this {@link #dinosaur}.
     */
    private Vector2D direction;

    /**
     * The direction {@link Vector2D} of the hunter to this {@link #dinosaur}.
     */
    private Vector2D directionOfHunter;

    //endregion

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Escape(Dinosaur simulationObject) {
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
        if (target == null || reachedTarget()) {
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
        }
        if (target != null) {
            direction = dinosaur.getPosition().directionToTarget(target);
            dinosaur.flipImage(direction);
            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));
        }
    }

    /**
     * Is called on state exit
     */
    @Override
    public void onExit() {
    }

    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
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
        addTransition(new StateTransition(StateFactory.States.escape, simulation -> !simulation.canMoveTo(dinosaur.getPosition(), target, dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), false, false, null)));

        //If the dinosaur is no longer been chased.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> !dinosaur.isChased()));
    }

    /**
     * Checks if the {@link #target} point is reached.
     *
     * @return true if the dinosaur is in the {@link Dinosaur#PROXIMITY_RANGE} to the {@link #target}.
     */
    private boolean reachedTarget() {
        return target != null && simulationObject.getPosition().isInRangeOf(target, Dinosaur.PROXIMITY_RANGE);
    }
}