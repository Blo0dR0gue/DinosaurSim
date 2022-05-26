package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/**
 * Updates the simulation data (Engine)
 *
 * @author Daniel Czeschner
 * @see Simulation
 */
public class SimulationLoop {

    //region variables

    //region simulation update rates
    private static final int UPDATES_PER_SECOND = 60;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int UPDATES_PER_STEP = 30;

    private final double updateRate = 1.0d / UPDATES_PER_SECOND;
    private final double fpsRate = 1.0d / FRAMES_PER_SECOND;
    //endregion

    /**
     * Is multiplied on the update rate. <1 means more updates per second
     */
    private int simulationSpeedMultiplier;
    /**
     * Is multiplied on the amount of steps in the simulation step proceedings
     */
    private int stepRangeMultiplier;

    //region runner variables

    private double deltaTime = 0;
    private double deltaTimeFPS = 0;
    private long currentTime, lastUpdate = System.currentTimeMillis();

    //region Status variables (Debug)
    private long nextDebugStatsTime;
    private int fps, ups;
    //endregion

    //endregion

    /**
     * The current simulation data
     */
    private Simulation currentSimulation;

    /**
     * The simulation loop thread handler
     * @see Timeline
     */
    private Timeline simulationLoop;

    //endregion

    /**
     * Constructor for a simulation runner
     * @param landscapeName
     * @param backgroundGraphicsContext
     * @param simulationSpeedMultiplier
     * @param stepRangeMultiplier
     */
    public SimulationLoop(String landscapeName, GraphicsContext backgroundGraphicsContext, int simulationSpeedMultiplier, int stepRangeMultiplier) {
        currentSimulation = new Simulation(landscapeName, backgroundGraphicsContext);
        this.simulationSpeedMultiplier = simulationSpeedMultiplier;
        this.stepRangeMultiplier = stepRangeMultiplier;

        //TODO maybe use AnimationTimer?
        this.simulationLoop = new Timeline(new KeyFrame(Duration.millis(1), event -> runner()));
        this.simulationLoop.setCycleCount(Timeline.INDEFINITE);

        nextDebugStatsTime = System.currentTimeMillis() + 1000;

        //TODO remove
        this.simulationLoop.play();
    }

    private void runner() {
        currentTime = System.currentTimeMillis();
        double lastUpdateTimeInSeconds = (currentTime - lastUpdate) / 1000d;
        deltaTime += lastUpdateTimeInSeconds * simulationSpeedMultiplier;
        deltaTimeFPS += lastUpdateTimeInSeconds;
        lastUpdate = currentTime;

        //Limits the update rate
        if (deltaTime >= updateRate) {
            while (deltaTime >= updateRate) {
                update(deltaTime);
                deltaTime -= updateRate;
            }
        }

        //Limits the frame per second
        if (deltaTimeFPS >= fpsRate) {
            while (deltaTimeFPS >= fpsRate) {
                updatePositions();
                deltaTimeFPS -= fpsRate;
            }
        }

        //Debug: Print out the current ups and fps.
        printStats();
    }

    private void update(double deltaTime) {
        ups++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.update(deltaTime, currentSimulation);
        }
    }

    private void updatePositions() {
        fps++;
    }

    private void printStats() {
        if (System.currentTimeMillis() > nextDebugStatsTime) {
            System.out.printf("FPS: %d, UPS: %d%n", fps, ups);
            fps = 0;
            ups = 0;
            nextDebugStatsTime = System.currentTimeMillis() + 1000;
        }
    }

    public void triggerUpdates() {

    }

    public void startSimulationRunner() {

    }

    public void pauseSimulationRunner() {

    }

    public void stopSimulationRunner() {

    }

}
