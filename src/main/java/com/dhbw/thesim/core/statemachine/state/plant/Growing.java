package com.dhbw.thesim.core.statemachine.state.plant;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import javafx.application.Platform;

/**
 * Represents a {@link State} an {@link Plant} can be in. <br>
 * In this {@link State} the handled {@link Plant} is growing.
 *
 * @author Daniel Czeschner
 */
public class Growing extends State {

    //region variables

    /**
     * Helper {@link Plant} variable, to get plant specific variables
     */
    Plant plant;

    //endregion

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Growing(Plant simulationObject) {
        super(simulationObject);
        plant = (Plant) this.simulationObject;
    }

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     *
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation simulation) {
        grow(deltaTime);
    }

    /**
     * Is called on state exit
     */
    @Override
    public void onExit() {
        //Make the plant visible again.
        Platform.runLater(() -> plant.getJavaFXObj().setVisible(true));

    }

    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
    @Override
    public void initTransitions() {
        //If the plant is grown, transition to the grown state.
        addTransition(new StateTransition(StateFactory.States.grown, simulation -> plant.isGrown()));
    }

    /**
     * Grows the {@link Plant} by its {@link Plant#getGrowthRate()}
     *
     * @param deltaTime The since the last update call. (in seconds)
     */
    private void grow(double deltaTime) {
        if (plant.getGrowth() < Plant.MAX_GROWTH) {
            plant.setGrowth(plant.getGrowth() + plant.getGrowthRate() * deltaTime);
        }
    }
}