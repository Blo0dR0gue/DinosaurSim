package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * Represents a {@link State} an {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is moving to a random position in range. (TODO)
 */
public class Wander extends State {

    /**
     * The target {@link Vector2D} position.
     */
    private final Vector2D target;

    /**
     * The normalized direction {@link Vector2D}
     */
    private final Vector2D direction;


    public Wander(Dinosaur simulationObject) {
        super(simulationObject);

        //TODO
        target = new Vector2D(500, 180);
        direction = simulationObject.getPosition().direction(target);
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        //TODO (speed, etc)
        simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(40 * deltaTime)));
    }

    @Override
    public void initTransitions() {
        //When target is reached -> transition to Stand-state.
        addTransition(new StateTransition(StateFactory.States.stand, this::arrived));
    }

    /**
     * Are we in range of our {@link #target}
     * @return true, if we are in the {@link Dinosaur#PROXIMITY_RANGE} to the {@link #target}.
     */
    private boolean arrived() {
        return target != null && simulationObject.getPosition().isInRangeOf(target, Dinosaur.PROXIMITY_RANGE);
    }

}
