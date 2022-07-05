package com.dhbw.thesim.core.statemachine.state.plant;

import com.dhbw.thesim.core.entity.Plant;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import javafx.application.Platform;

/**
 * Represents a {@link State} an {@link Plant} can be in. <br>
 * In this {@link State} the handled {@link Plant} is grown and can be eaten.
 *
 * @author Daniel Czeschner
 */
public class Grown extends State {


    //region variables

    /**
     * Helper {@link Plant} variable, to get plant specific variables
     */
    Plant plant;

    //endregion

    /**
     * Constructor
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Grown(SimulationObject simulationObject) {
        super(simulationObject);
        this.plant = (Plant) this.simulationObject;
    }

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     *
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation simulation) {
        //Nothing to do here
    }

    /**
     * Is called on state exit
     */
    @Override
    public void onExit() {
        //Hide the plant
        Platform.runLater(() -> plant.getJavaFXObj().setVisible(false));
    }


    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
    @Override
    public void initTransitions() {
        //If the plant got eaten, transition to the growing state.
        addTransition(new StateTransition(StateFactory.States.growing, simulation -> !plant.isGrown()));
    }
}