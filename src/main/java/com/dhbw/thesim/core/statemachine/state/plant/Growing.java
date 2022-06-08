package com.dhbw.thesim.core.statemachine.state.plant;

import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * Represents a {@link State} an {@link Plant} can be in. <br>
 * In this {@link State} the handled {@link Plant} is growing.
 *
 * @author Daniel Czeschner
 */
public class Growing extends State {

    Plant plant;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Growing(Plant simulationObject) {
        super(simulationObject);
        plant = (Plant) this.simulationObject;
    }

    @Override
    public void onExit() {
        //Nothing to do here
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        grow(deltaTime);
    }

    @Override
    public void initTransitions() {
        addTransition(new StateTransition(StateFactory.States.grown, simulation -> plant.isGrown()));
    }

    private void grow(double deltaTime) {
        if (plant.getGrowth() < Plant.MAX_GROWTH) {
            plant.setGrowth(plant.getGrowth() + plant.getGrowthRate() * deltaTime);
        }
    }

}
