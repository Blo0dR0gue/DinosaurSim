package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.util.SimulationTime;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Updates the simulation (Engine)
 *
 * @author Daniel Czeschner
 * @see Simulation
 */
public class SimulationLoop {

    //region variables

    /**
     * Defines how many updated are called per second.
     */
    private static final int UPDATES_PER_SECOND = 60;

    /**
     * Defines how often the screen gets updated per second.
     */
    private static final int FRAMES_PER_SECOND = 30;

    /**
     * Defines how many updates are called per simulation step (in simulation step proceedings)
     */
    private static final int UPDATES_PER_STEP = 30;

    /**
     * The used update rate.
     */
    private static final double UPDATE_RATE = 1.0d / UPDATES_PER_SECOND;

    /**
     * The used frame rate.
     */
    private static final double FRAME_RATE = 1.0d / FRAMES_PER_SECOND;

    /**
     * Is multiplied on the update rate. value < 1 means more updates per second
     */
    private int simulationSpeedMultiplier;

    /**
     * Is multiplied on the amount of steps in the simulation step proceedings
     */
    private final int stepRangeMultiplier;

    //region runner variables
    /**
     * Is the simulation thread running?
     */
    private boolean running;

    /**
     * Is the simulation thread paused?
     */
    private boolean paused;

    //region Status variables (Debug)
    private long nextDebugStatsTime;
    private int fps;
    private int ups;
    //endregion

    //endregion

    /**
     * The current simulation data
     */
    private Simulation currentSimulation;

    /**
     * The simulation loop thread handler
     */
    private Thread simulationLoopThread;

    /**
     * The current loop time. Used for timing stuff e.g. stats handling.
     */
    private SimulationTime loopTime;

    /**
     * Max amount of steps in the simulation step proceedings.
     */
    private int maxStepAmount;

    /**
     * Max amount of time a simulation is running.
     */
    private int maxRunTimeInMinutes;

    /**
     * At what percentage of the max run time a new stats point should be added.
     */
    private static final double STAT_UPDATES_IN_PERCENTAGE_OF_MAX_RUNTIME = 0.05;

    /**
     * Current SimulationOverlay instance for callbacks.
     */
    private SimulationOverlay simulationOverlay;

    //endregion

    /**
     * Constructor for a simulation runner
     *
     * @param simulationSpeedMultiplier The speed multiplier for the automatic simulation mode.
     * @param stepRangeMultiplier       The range multiplier for how many update calls are made in the step simulation mode.
     * @param simulation                The simulation data which should be used/updated.
     * @param maxStepAmount             The max amount of steps, that can be triggered.
     * @param maxRunTimeInMinutes       The max amount of time a simulation is running (In Minutes)
     * @param simulationOverlay         The simulation overlay which is used to display the GUI for the simulation
     */
    public SimulationLoop(int simulationSpeedMultiplier, int stepRangeMultiplier, Simulation simulation, int maxStepAmount, int maxRunTimeInMinutes, SimulationOverlay simulationOverlay) {
        this.currentSimulation = simulation;
        this.simulationSpeedMultiplier = simulationSpeedMultiplier;
        this.stepRangeMultiplier = stepRangeMultiplier;
        this.maxStepAmount = maxStepAmount;
        this.maxRunTimeInMinutes = maxRunTimeInMinutes;

        //Set the time, for the first debug message.
        nextDebugStatsTime = System.currentTimeMillis() + 1000;

        this.loopTime = new SimulationTime();

        this.simulationOverlay = simulationOverlay;

        updateGraphics();
    }

    /**
     * The Runnable for the {@link #simulationLoopThread}-thread.  <br>
     * In this Thread/Runnable the automatic updates for a the {@link SimulationObject}s of ann {@link Simulation} are handled.
     */
    private final Runnable simLoopRunnable = () -> {
        running = true;
        double deltaTime = 0;
        double frameAccumulator = 0;

        long lastUpdate = System.currentTimeMillis();
        long currentTime;

        //The time, when the sim is finished
        SimulationTime runtime = new SimulationTime();
        runtime.addMinutesTime(maxRunTimeInMinutes);

        SimulationTime lastStatisticsUpdateTime = new SimulationTime();
        int intervalUntilStatisticsUpdateInSeconds = (int) (runtime.getTime() * STAT_UPDATES_IN_PERCENTAGE_OF_MAX_RUNTIME);

        while (running) {
            //update the loop variables.
            currentTime = System.currentTimeMillis();
            double lastUpdateTimeInSeconds = (currentTime - lastUpdate) / 1000d;
            deltaTime += lastUpdateTimeInSeconds * simulationSpeedMultiplier;
            frameAccumulator += lastUpdateTimeInSeconds;
            lastUpdate = currentTime;
            //Limit the update rate
            if (deltaTime >= UPDATE_RATE) {
                while (deltaTime >= UPDATE_RATE) {
                    //If we are not paused, trigger an update.
                    if (!paused) {
                        update(deltaTime);
                        loopTime.addDeltaTime(deltaTime / simulationSpeedMultiplier);
                    }
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

            //adding statistics update at intervals
            double runPercentage = round((loopTime.getTime()) / (runtime.getTime()), 3);
            if (Math.abs(loopTime.timeSince(lastStatisticsUpdateTime)) > intervalUntilStatisticsUpdateInSeconds) {
                updateStatistics();
                System.out.println("stat update: " + (runPercentage));
                lastStatisticsUpdateTime = new SimulationTime(loopTime.getTime());
            }

            //Check if over
            if (runtime.getTime() <= loopTime.getTime() || this.currentSimulation.isOver()) {
                this.stopSimulationRunner();
                Platform.runLater(() -> simulationOverlay.showStatisticsEndcard());
            }
        }
    };

    /**
     * Rounds a number to a given decimal place.
     *
     * @param value  The number which should be rounded.
     * @param places The amount of decimal places.
     * @return The rounded value.
     */
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Adds a new statistics value to the end statistics.
     */
    private void updateStatistics() {
        simulationOverlay.getStatistics().addSimulationObjectList(getCurrentSimulation().getSimulationObjects(), loopTime);
    }

    /**
     * Is called each update call. <br>
     * This method calls the {@link SimulationObject#update(double, Simulation)} method.
     *
     * @param deltaTime The time since the last update call. (in seconds)
     */
    private void update(double deltaTime) {
        ups++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.update(deltaTime, currentSimulation);
        }
        currentSimulation.getCurrentSimulationTime().addDeltaTime(deltaTime);
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
     *
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
     * Triggers updates for the step proceeding simulation mode.
     */
    public void triggerUpdates() {
        for (int i = 0; i < UPDATES_PER_STEP * stepRangeMultiplier; i++) {
            update(0.1);
        }
        updateGraphics();
        this.maxStepAmount--;
        //check if over
        if (maxStepAmount <= 0 || currentSimulation.isOver()) {
            updateStatistics();
            simulationOverlay.showStatisticsEndcard();
        }
    }

    /**
     * Starts the automatic simulation runner.
     *
     * @see #simulationLoopThread
     */
    public void startSimulationRunner() {
        simulationLoopThread = new Thread(simLoopRunnable);
        simulationLoopThread.start();
    }

    /**
     * Stops the {@link #simulationLoopThread}.
     */
    public void stopSimulationRunner() {
        running = false;
    }

    /**
     * Pause/Unpause the automatic simulation runner.
     *
     * @see #simLoopRunnable
     * @see #simulationLoopThread
     */
    public void togglePause() {
        paused = !paused;
        updateGraphics();
    }

    /**
     * Gets the current simulation data
     *
     * @return A {@link Simulation} object
     */
    public Simulation getCurrentSimulation() {
        return this.currentSimulation;
    }

    /**
     * Gets the boolean if simulation is currently paused
     *
     * @return A {@link Boolean} telling if simulation is paused
     */
    public boolean getSimulationPaused() {
        return this.paused;
    }

    /**
     * Gets the current loop time of the simulation
     *
     * @return The {@link #loopTime} object.
     */
    public SimulationTime getLoopTime() {
        return loopTime;
    }

}