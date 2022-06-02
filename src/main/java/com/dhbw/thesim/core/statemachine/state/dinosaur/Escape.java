package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;

/**
 * TODO
 *
 * @author Daniel Czeschner
 */
public class Escape extends State {

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Escape(Dinosaur simulationObject) {
        super(simulationObject);
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

    }

    @Override
    public void initTransitions() {

    }

}
