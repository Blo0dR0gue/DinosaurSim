package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;

/**
 * Represents a {@link State} an {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is dead.
 *
 * @author Daniel Czeschner
 */
public class Dead extends State {

    private boolean triggered = false;

    private final Dinosaur dinosaur;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Dead(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (!triggered) {
            //Reset the hunted dinosaur
            if (dinosaur.getTarget() != null && dinosaur.getTarget() instanceof Dinosaur target) {
                target.resetForceNoOp();
                target.setIsChased(false);
                target.setTarget(null);
            }
            triggered = true;
            //Remove the object from the simulation
            simulation.deleteObject(this.simulationObject);
        }

    }

    @Override
    public void initTransitions() {
        //Dead = end
    }
}
