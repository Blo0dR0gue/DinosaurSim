package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * TODO
 *
 * @author Daniel Czeschner
 */
public class MoveToFoodSource extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    private Vector2D target;
    private double targetInteractionRange;

    /**
     * The normalized direction {@link Vector2D} to the target
     */
    private Vector2D direction;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public MoveToFoodSource(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (dinosaur.getTarget() == null && target == null) {
            if (dinosaur.isHungry() && dinosaur.isThirsty()) {

                //TODO

            } else if (dinosaur.isHungry()) {

                dinosaur.setTarget(simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(),
                        dinosaur.canSwim(), dinosaur.canClimb()));

                target = dinosaur.getTarget().getPosition();
                targetInteractionRange = dinosaur.getTarget().getInteractionRange();
                direction = simulationObject.getPosition().direction(target);

                dinosaur.setTest(target);

            } else if (dinosaur.isThirsty()) {

                //TODO

            }
        }

        if (target != null) {

            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));

        }

    }

    @Override
    public void initTransitions() {

        //We can do this, because the update is called before the next check transitions
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getTarget() == null));

        //If the target is a dinosaur go to the hunt state.
        addTransition(new StateTransition(StateFactory.States.hunt, simulation -> dinosaur.getTarget() instanceof Dinosaur));

        //If we reached the target
        addTransition(new StateTransition(StateFactory.States.ingestion, this::reached));

    }

    private boolean reached(Simulation simulation){
        return simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getInteractionRange(), target, targetInteractionRange);
    }

}
