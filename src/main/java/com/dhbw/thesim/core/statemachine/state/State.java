package com.dhbw.thesim.core.statemachine.state;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;

/**
 * Represents a single State for a dinosaur. <br>
 * Each state handles one behaviour for a dinosaur.
 *
 * @author Daniel Czeschner
 */
public abstract class State {

    protected SimulationObject simulationObject;

    public State(SimulationObject simulationObject) {
        this.simulationObject = simulationObject;
    }

    public abstract void update(double deltaTime, Simulation simulation);

}
