package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;

/**
 * Represents a {@link State} an {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} is dead.
 *
 * @author Daniel Czeschner
 */
public class Dead extends State {

    //region variables

    /**
     * Variable to trigger the state just once.
     */
    private boolean triggered = false;
    private final Dinosaur dinosaur;

    //endregion

    /**
     * Constructor for this {@link State}.
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public Dead(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = simulationObject;
    }

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     *
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
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

    /**
     * Is called on state exit
     */
    @Override
    public void onExit() {
        //Nothing to do here.
    }

    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
    @Override
    public void initTransitions() {
        //Dead = end
    }
}