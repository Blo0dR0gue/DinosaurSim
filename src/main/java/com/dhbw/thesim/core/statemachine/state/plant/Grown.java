package com.dhbw.thesim.core.statemachine.state.plant;

import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

public class Grown extends State {

    Plant plant;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Grown(SimulationObject simulationObject) {
        super(simulationObject);
        this.plant = (Plant) this.simulationObject;
    }

    @Override
    public void onExit() {
        //Nothing to do here
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        //Nothing to do here
    }

    @Override
    public void initTransitions() {
        addTransition(new StateTransition(StateFactory.States.growing, simulation -> !plant.isGrown()));
    }
}
