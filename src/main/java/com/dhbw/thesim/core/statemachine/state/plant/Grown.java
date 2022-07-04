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

    Plant plant;

    /**
     * Constructor
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Grown(SimulationObject simulationObject) {
        super(simulationObject);
        this.plant = (Plant) this.simulationObject;
    }

    @Override
    public void onExit() {
        //Hide the plant
        Platform.runLater(() -> {
            plant.getJavaFXObj().setVisible(false);
        });
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