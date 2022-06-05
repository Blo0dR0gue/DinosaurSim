package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

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
        //TODO what happens if multiple hunters hunt one target.

        //Init
        if (target == null && dinosaur.isHungry()) {
            dinosaur.setTarget(simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(),
                    dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()));

            if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur targetDino) {
                //TODO let hunted dino run away when the target dino can see this dino
                targetDino.setIsChased(true);
                targetDino.setTarget(dinosaur);
            }

            target = dinosaur.getTarget().getPosition();
            direction = dinosaur.getPosition().directionToTarget(target);

        }

        if (direction != null) {

            //Reset the chased state (Can be retested, if multiple dinos hunt the target.)
            if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur targetDino) {
                targetDino.setIsChased(true);
            }

            target = dinosaur.getTarget().getPosition();
            direction = dinosaur.getPosition().directionToTarget(target);

            //TODO Debug variable
            //dinosaur.setTest(target);

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
    public void initTransitions() {

        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        //We have no target -> transition to wander.
        //We can do this here, because the update is called before the next check transitions
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> target == null || dinosaur.getTarget() == null));

        //The other dinosaur escaped
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> dinosaur.getTarget() != null && !simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getTarget().getPosition(), dinosaur.getTarget().getInteractionRange())));

        //If we reached the target
        addTransition(new StateTransition(StateFactory.States.ingestion, this::reached));

        //If we have a simulationobject target (e.g. a dinosaur or plant, and it can no longer be eaten (because the object got eaten), transition to wander.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getTarget() != null && !dinosaur.getTarget().canBeEaten(dinosaur.getStrength())));

        //If we can't reach the target anymore -> transition to moveToFoodSource (check for another food/water source in range). (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> !simulation.canMoveTo(dinosaur.getPosition(), simulationObject.getPosition(), 0, dinosaur.canSwim(), dinosaur.canClimb(), null, true, true)));

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
