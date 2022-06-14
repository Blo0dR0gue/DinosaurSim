package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

import java.util.List;

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
     * {@link Vector2D}-variables for paths.
     */
    private Vector2D target;
    private Vector2D direction;

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
        //Init
        if (target == null && dinosaur.isHungry()) {
            dinosaur.setTarget(simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getInteractionRange(), dinosaur.getDiet(), dinosaur.getType(),
                    dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()));
            if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur targetDino) {
                targetDino.setIsChased(true);
                targetDino.setTarget(dinosaur);
                target = targetDino.getPosition();
                direction = dinosaur.getPosition().directionToTarget(target);
            }
        }
        if (direction != null) {
            //set the chased state (Can be retested, if multiple dinos hunt the target.)
            if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur targetDino) {
                targetDino.setIsChased(true);
            }
            target = dinosaur.getTarget().getPosition();
            direction = dinosaur.getPosition().directionToTarget(target);
            dinosaur.flipImage(direction);
            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));
        } else {
            //Invalid state
            target = null;
            if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur targetDino) {
                dinosaur.setTarget(null);
                targetDino.setIsChased(false);
            }
        }
    }

    @Override
    public void onExit() {
        //reset the target
        if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur targetDino) {
            targetDino.setIsChased(false);
        }
    }

    @Override
    public void initTransitions() {
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        //We have no target -> transition to wander.
        //We can do this here, because the update is called before the next check transitions
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> target == null || dinosaur.getTarget() == null));

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //The other dinosaur escaped
        addTransition(new StateTransition(
                StateFactory.States.moveToFoodSource, simulation -> dinosaur.getTarget() != null &&
                !simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getViewRange() + Dinosaur.PROXIMITY_RANGE, dinosaur.getTarget().getPosition(), dinosaur.getTarget().getInteractionRange())));

        //If we reached the target
        addTransition(new StateTransition(StateFactory.States.ingestion, this::reached));

        //If we have a simulationobject target (e.g. a dinosaur or plant, and it can no longer be eaten (because the object got eaten), transition to moveToFoodSource.
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.getTarget() != null && !dinosaur.getTarget().canBeEaten(dinosaur.getStrength())));

        //If we can't reach the target anymore -> transition to moveToFoodSource (check for another food/water source in range). (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> !simulation.canMoveTo(dinosaur.getPosition(), dinosaur.getTarget().getPosition(), dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), true, false, List.of(dinosaur.getTarget()))));
    }

    private boolean reached(Simulation simulation) {
        boolean caught = simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getInteractionRange(), dinosaur.getTarget().getPosition(), dinosaur.getTarget().getInteractionRange());
        if (caught) {
            Dinosaur huntedDino = (Dinosaur) dinosaur.getTarget();
            huntedDino.forceNoOp();
        }
        return caught;
    }
}