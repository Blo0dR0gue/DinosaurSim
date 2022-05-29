package com.dhbw.thesim.core.simulation;

import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.gui.SimulationOverlay;
import javafx.scene.canvas.GraphicsContext;

/**
 * TODO Comments
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

    private final double UPDATE_RATE = 1.0d / UPDATES_PER_SECOND;
    private final double FRAME_RATE = 1.0d / FRAMES_PER_SECOND;
    //endregion

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

    //endregion

    /**
     * Constructor for a simulation runner (TODO rework to add the most to the Simulation-Class?)
     *
     * @param landscapeName             The name for the landscape, which should be used.
     * @param backgroundGraphicsContext The {@link GraphicsContext} for the background canvas.
     * @param simulationSpeedMultiplier The speed multiplier for the automatic simulation mode.
     * @param stepRangeMultiplier       The range multiplier for how many update calls are made in the step simulation mode.
     * @param simulationOverlay         The {@link SimulationOverlay} in which we handle our {@link SimulationObject}s
     */
    public SimulationLoop(String landscapeName, GraphicsContext backgroundGraphicsContext, int simulationSpeedMultiplier, int stepRangeMultiplier, SimulationOverlay simulationOverlay) {
        currentSimulation = new Simulation(landscapeName, backgroundGraphicsContext, simulationOverlay);
        this.simulationSpeedMultiplier = simulationSpeedMultiplier;
        this.stepRangeMultiplier = stepRangeMultiplier;

        //TODO remove
        /* Zucken bei den bewegungen :(
        this.simulationLoop = new Timeline(new KeyFrame(Duration.millis(1), event -> runner()));
        this.simulationLoop.setCycleCount(Timeline.INDEFINITE);*/

        //Set the time, for the first debug message.
        nextDebugStatsTime = System.currentTimeMillis() + 1000;
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
        }
    };

    /**
     * Is called each update call. <br>
     * This method calls the {@link SimulationObject#update(double, Simulation)} method.
     *
     * @param deltaTime The time since the last update call.
     */
    private void update(double deltaTime) {
        ups++;
        for (SimulationObject obj : currentSimulation.getSimulationObjects()) {
            obj.update(deltaTime, currentSimulation);
        }
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
     * Triggers updates for the step simulation mode.
     */
    public void triggerUpdates() {
        for (int i = 0; i < UPDATES_PER_STEP * stepRangeMultiplier; i++) {
            update(0.1);
        }
        updateGraphics();
    }

    /**
     * Stats the automatic simulation runner.
     *
     * @see #simulationLoop
     */
    public void startSimulationRunner() {
        //TODO maybe use AnimationTimer (test performance)
        new Thread(simLoopRunnable).start();
    }

    /**
     * Stops the {@link #simulationLoop}.
     */
    public void stopSimulationRunner() {
        running = false;
    }

    /**
     * Pause/Unpause the automatic simulation runner.
     *
     * @see #simLoopRunnable
     * @see #simulationLoop
     */
    public void togglePause() {
        paused = !paused;
    }

}
