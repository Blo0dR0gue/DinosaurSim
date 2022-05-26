package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;

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
    private boolean running;
    private boolean paused;
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
     */
    private Thread simulationLoop;

    //endregion

    /**
     * Constructor for a simulation runner
     * @param landscapeName
     * @param backgroundGraphicsContext
     * @param simulationSpeedMultiplier
     * @param stepRangeMultiplier
     * @param simulationOverlay
     */
    public SimulationLoop(String landscapeName, GraphicsContext backgroundGraphicsContext, int simulationSpeedMultiplier, int stepRangeMultiplier, SimulationOverlay simulationOverlay) {
        currentSimulation = new Simulation(landscapeName, backgroundGraphicsContext, simulationOverlay);
        this.simulationSpeedMultiplier = simulationSpeedMultiplier;
        this.stepRangeMultiplier = stepRangeMultiplier;

        //TODO remove
        /* Zucken bei den bewegungen :(
        this.simulationLoop = new Timeline(new KeyFrame(Duration.millis(1), event -> runner()));
        this.simulationLoop.setCycleCount(Timeline.INDEFINITE);*/

        nextDebugStatsTime = System.currentTimeMillis() + 1000;
    }

    private final Runnable simLoop = () -> {
        running = true;
        double deltaTime = 0;
        double frameAccumulator = 0;
        long currentTime, lastUpdate = System.currentTimeMillis();
        nextDebugStatsTime = System.currentTimeMillis() + 1000;

        while (running) {
            currentTime = System.currentTimeMillis();
            double lastUpdateTimeInSeconds = (currentTime - lastUpdate) / 1000d;
            deltaTime += lastUpdateTimeInSeconds;
            frameAccumulator += lastUpdateTimeInSeconds;
            lastUpdate = currentTime;

            //Limit the update rate
            if (deltaTime >= updateRate) {
                while (deltaTime >= updateRate){
                    if(!paused)
                        update(deltaTime * simulationSpeedMultiplier);
                    deltaTime -= updateRate;
                }
            }

            //Limit the frames per second
            if (frameAccumulator >= fpsRate) {
                while (frameAccumulator >= fpsRate) {
                    if(!paused)
                        updatePositions();
                    frameAccumulator -= fpsRate;
                }
            }

            //Print debug stats
            printStats();
        }
    };

    private void update(double deltaTime) {
        ups++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.update(deltaTime, currentSimulation);
        }
    }

    private void updatePositions() {
        fps++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.updatePosition();
        }
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
        for (int i = 0; i < 30 * stepRangeMultiplier; i++){
            update(0.1);
        }
        updatePositions();
    }

    public void startSimulationRunner() {
        //TODO maybe use AnimationTimer (test performance)
        new Thread(simLoop).start();
    }

    public void stopSimulationRunner() {
        running = false;
    }

    public void togglePause(){
        paused = !paused;
    }

}
