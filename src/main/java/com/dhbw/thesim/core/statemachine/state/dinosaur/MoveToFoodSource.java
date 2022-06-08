package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries to move to a food or water source.
 *
 * @author Daniel Czeschner
 */
public class MoveToFoodSource extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * The target {@link Vector2D} where the dinosaur is walking to.
     */
    private Vector2D target;

    /**
     * The interaction range of the target, which should be hit.
     */
    private double targetInteractionRange;

    /**
     * The normalized direction {@link Vector2D} to the target
     */
    private Vector2D direction;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public MoveToFoodSource(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (target == null) {

            if (dinosaur.isHungry() && dinosaur.isThirsty()) {

                SimulationObject target1 = simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(),
                        dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength());

                Vector2D target2 = simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb());

                if (target1 == null && target2 != null) {
                    target = target2;
                    System.out.println("Found water source");

                    targetInteractionRange = Tile.TILE_SIZE / 2 + Dinosaur.PROXIMITY_RANGE;
                    direction = dinosaur.getPosition().directionToTarget(target);

                    dinosaur.setTest(target);
                } else if (target1 != null && target2 == null) {
                    dinosaur.setTarget(target1);

                    System.out.println("Found food source");

                    target = dinosaur.getTarget().getPosition();
                    targetInteractionRange = dinosaur.getTarget().getInteractionRange();
                    direction = dinosaur.getPosition().directionToTarget(target);

                    dinosaur.setTest(target);

                } else if (target1 != null) {

                    if (Vector2D.distance(dinosaur.getPosition(), target2) < Vector2D.distance(dinosaur.getPosition(), target1.getPosition())) {
                        //water is closer go to water

                        target = target2;
                        System.out.println("Found water source");

                        targetInteractionRange = Tile.TILE_SIZE / 2 + Dinosaur.PROXIMITY_RANGE;

                    } else {
                        //Food is closer go to food
                        dinosaur.setTarget(target1);

                        System.out.println("Found food source");

                        target = dinosaur.getTarget().getPosition();
                        targetInteractionRange = dinosaur.getTarget().getInteractionRange();

                    }
                    direction = dinosaur.getPosition().directionToTarget(target);
                    dinosaur.setTest(target);

                }

            } else if (dinosaur.isHungry()) {

                dinosaur.setTarget(simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(),
                        dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getStrength()));

                if (dinosaur.getTarget() != null) {

                    System.out.println("Found food source");

                    target = dinosaur.getTarget().getPosition();
                    targetInteractionRange = dinosaur.getTarget().getInteractionRange();
                    direction = dinosaur.getPosition().directionToTarget(target);

                    dinosaur.setTest(target);
                }


            } else if (dinosaur.isThirsty()) {

                target = simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb());

                if (target != null) {

                    System.out.println("Found water source");

                    targetInteractionRange = Tile.TILE_SIZE / 2 + Dinosaur.PROXIMITY_RANGE;
                    direction = dinosaur.getPosition().directionToTarget(target);

                    dinosaur.setTest(target);

                }

            }
            if(direction != null){
                dinosaur.flipImage(direction);
            }
        }

        if (direction != null) {

            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));

        }

    }

    @Override
    public void onExit() {
        if(!dinosaur.isChased())
            dinosaur.setTarget(null);
    }

    @Override
    public void initTransitions() {
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        addTransition(new StateTransition(StateFactory.States.stand, simulation -> dinosaur.isForcedToNoOp()));

        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //We can do this, because the update is called before the next check transitions
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> target == null));

        //If we hava simulationobject target (e.g. a dinosaur or plant, and it can no longer be eaten (because the object got eaten), transition to wander.
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getTarget() != null && !dinosaur.getTarget().canBeEaten(dinosaur.getStrength())));

        //If the target is a dinosaur go to the hunt state.
        addTransition(new StateTransition(StateFactory.States.hunt, simulation -> dinosaur.getTarget() instanceof Dinosaur));

        //If we reached the target
        addTransition(new StateTransition(StateFactory.States.ingestion, this::reached));

        //If we can't reach the target anymore -> transition to moveToFoodSource (check for another food/water source in range). (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.moveToFoodSource, simulation -> !simulation.canMoveTo(dinosaur.getPosition(), target, dinosaur.getInteractionRange(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getRenderOffset(), true, true)));
    }

    /**
     * Do we have reached the target?
     *
     * @param simulation The current {@link Simulation} data.
     * @return true, if we have reached the target.
     */
    private boolean reached(Simulation simulation) {
        return simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getInteractionRange(), target, targetInteractionRange);
    }

}
