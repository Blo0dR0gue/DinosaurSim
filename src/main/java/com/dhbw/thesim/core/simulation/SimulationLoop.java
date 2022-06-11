package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;

/**
 * Updates the simulation (Engine)
 *
 * @author Daniel Czeschner
 * @see Simulation
 */
@SuppressWarnings("unused")
public class SimulationLoop {

    //region variables

    /**
     * Used to specify update rates.
     */
    private static final int UPDATES_PER_SECOND = 60;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int UPDATES_PER_STEP = 30;
    private final double UPDATE_RATE = 1.0d / UPDATES_PER_SECOND;
    private final double FRAME_RATE = 1.0d / FRAMES_PER_SECOND;

    /**
     * Is multiplied on the update rate. <1 means more updates per second
     */
    private int simulationSpeedMultiplier;
    /**
     * Is multiplied on the amount of steps in the simulation step proceedings
     */
    private final int stepRangeMultiplier;

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

    /**
     * Max amount of steps.
     */
    private int maxStepAmount;

    /**
     * Max amount of time a simulation is running.
     */
    private int maxRunTimeInMinutes;

    //endregion

    /**
     * Constructor for a simulation runner
     * @param simulationSpeedMultiplier The speed multiplier for the automatic simulation mode.
     * @param stepRangeMultiplier       The range multiplier for how many update calls are made in the step simulation mode.
     * @param maxStepAmount             The max amount of steps, that can be triggered.
     * @param maxRunTimeInMinutes       The max amount of time a simulation is running (In Minutes)
     */
    public SimulationLoop(int simulationSpeedMultiplier, int stepRangeMultiplier, Simulation simulation, int maxStepAmount, int maxRunTimeInMinutes) {
        this.currentSimulation = simulation;
        this.simulationSpeedMultiplier = simulationSpeedMultiplier;
        this.stepRangeMultiplier = stepRangeMultiplier;
        this.maxStepAmount = maxStepAmount;
        this.maxRunTimeInMinutes = maxRunTimeInMinutes;
        //Set the time, for the first debug message.
        nextDebugStatsTime = System.currentTimeMillis() + 1000;

        updateGraphics();
    }

    /**
     * The Runnable for the {@link #simulationLoop}-thread.  <br>
     * In this Thread/Runnable the automatic updates for a the {@link SimulationObject}s of ann {@link Simulation} are handled.
     */
    private final Runnable simLoopRunnable = () -> {
        running = true;
        double deltaTime = 0;
        double frameAccumulator = 0;
        long currentTime, lastUpdate = System.currentTimeMillis();
        //The time, when the sim is finished
        long runtime = lastUpdate + (long) this.maxRunTimeInMinutes * 60 * 1000;

        while (running) {
            //update the loop variables.
            currentTime = System.currentTimeMillis();
            double lastUpdateTimeInSeconds = (currentTime - lastUpdate) / 1000d;
            deltaTime += lastUpdateTimeInSeconds;
            frameAccumulator += lastUpdateTimeInSeconds;
            lastUpdate = currentTime;

            //Limit the update rate
            if (deltaTime >= UPDATE_RATE) {
                while (deltaTime >= UPDATE_RATE) {
                    //If we are not paused, trigger an update.
                    if (!paused)
                        update(deltaTime * simulationSpeedMultiplier);
                    deltaTime -= UPDATE_RATE;
                }
            }
            //Limit the frames per second
            if (frameAccumulator >= FRAME_RATE) {
                while (frameAccumulator >= FRAME_RATE) {
                    //If we are not paused, trigger a re-render.
                    if (!paused)
                        updateGraphics();
                    frameAccumulator -= FRAME_RATE;
                }
            }
            //Print debug stats
            printStats();
            //Check if over
            if (runtime <= currentTime || this.currentSimulation.isOver()) {
                this.stopSimulationRunner();
                //TODO callback to GUI
                System.err.println("ende");
            }
        }
    };

    /**
     * Is called each update call. <br>
     * This method calls the {@link SimulationObject#update(double, Simulation)} method.
     * @param deltaTime The time since the last update call.
     */
    private void update(double deltaTime) {
        ups++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.update(deltaTime, currentSimulation);
        }
        currentSimulation.removeDeletedObjects();
        currentSimulation.spawnNewObjects();
    }

    /**
     * Updates the visuals for each {@link SimulationObject}. <br>
     * It calls the {@link SimulationObject#updateGraphics()} method.
     */
    private void updateGraphics() {
        fps++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.updateGraphics();
        }
    }

    /**
     * Debug method, which prints out the current updates per second and frames per second. <br>
     * Is only used in the automatic updates.
     * @see #simLoopRunnable
     */
    private void printStats() {
        if (System.currentTimeMillis() > nextDebugStatsTime) {
            System.out.printf("FPS: %d, UPS: %d%n", fps, ups);
            fps = 0;
            ups = 0;
            nextDebugStatsTime = System.currentTimeMillis() + 1000;
        }
    }

    /**
     * Triggers updates for the step simulation mode.
     */
    public void triggerUpdates() {
        for (int i = 0; i < UPDATES_PER_STEP * stepRangeMultiplier; i++) {
            update(0.1);
        }
        updateGraphics();
        this.maxStepAmount--;
        //check if over
        if (maxStepAmount <= 0 || currentSimulation.isOver()) {
            //TODO Callback to gui
        }
    }

    /**
     * Stats the automatic simulation runner.
     * @see #simulationLoop
     */
    public void startSimulationRunner() {
        simulationLoop = new Thread(simLoopRunnable);
        simulationLoop.start();
    }

    /**
     * Stops the {@link #simulationLoop}.
     */
    public void stopSimulationRunner() {
        running = false;
    }

    /**
     * Pause/Unpause the automatic simulation runner.
     * @see #simLoopRunnable
     * @see #simulationLoop
     */
    public void togglePause() {
        paused = !paused;
    }

    /**
     * Gets the current simulation data
     * @return A {@link Simulation} object
     */
    public Simulation getCurrentSimulation() {
        return this.currentSimulation;
    }

    /**
     * Gets the boolean if simulation is currently paused
     * @return A {@link Boolean} telling if simulation is paused
     */
    public boolean getSimulationPaused() {
        return this.paused;
    }
}