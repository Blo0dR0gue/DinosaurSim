package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;

public class Stand extends State {

    private Dinosaur dinosaur;

    public Stand(Dinosaur simulationObject) {
        super(simulationObject);
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

    }
}
