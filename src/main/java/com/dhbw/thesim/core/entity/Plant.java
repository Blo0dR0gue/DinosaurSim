package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;

/**
 * It's a plant-object in our simulation, which can be eaten.
 *
 * TODO
 * @author Daniel Czeschner
 */
public class Plant extends SimulationObject{

    public Plant() {
        super();
    }

    /**
     * Updates this {@link Plant} each update call.
     * @param deltaTime The time since the last update call in seconds.
     * @param currentSimulationData The current {@link Simulation}-Object with all information to the currently running simulation.
     * @see com.dhbw.thesim.core.simulation.SimulationLoop
     */
    @Override
    public void update(double deltaTime, Simulation currentSimulationData) {
        //TODO
    }

    /**
     * Updates the visuals for a {@link Plant}-object.
     */
    @Override
    public void updateGraphics() {
        //TODO
    }
}
