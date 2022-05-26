package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;
import javafx.scene.canvas.GraphicsContext;

/**
 * Updates the simulation data (Engine)
 *
 * @author Daniel Czeschner
 * @see Simulation
 */
public class SimulationLoop {

    private int simulationSpeedMultiplier;
    private int stepRangeMultiplier;
    private Simulation currentSimulation;

    public SimulationLoop(String landscapeName, GraphicsContext backgroundGraphicsContext, int simulationSpeedMultiplier, int stepRangeMultiplier){
        currentSimulation = new Simulation(landscapeName, backgroundGraphicsContext);
        this.simulationSpeedMultiplier = simulationSpeedMultiplier;
        this.stepRangeMultiplier = stepRangeMultiplier;
    }

    private void update(double deltaTime){
        for (SimulationObject obj: currentSimulation.getSimulationObjects()) {

        }
    }

    private void updatePositions(){

    }

    public void triggerUpdates(){

    }

    public void startSimulationRunner(){

    }

    public void pauseSimulationRunner(){

    }

    public void stopSimulationRunner(){

    }

}
