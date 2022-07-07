package com.dhbw.thesim.core.util;

/**
 * Holds the current time of a running simulation.
 *
 * @author Daniel Czeschner
 */
public class SimulationTime {

    //region variables

    /**
     * Time in seconds inside the simulation.
     */
    private double timeInSimulation;

    //endregion

    /**
     * Constructor which sets the {@link #timeInSimulation} to 0.
     */
    public SimulationTime() {
        timeInSimulation = 0;
    }

    /**
     * Constructor
     *
     * @param timeInSimulation The time in seconds for this handler.
     */
    public SimulationTime(double timeInSimulation) {
        this.timeInSimulation = timeInSimulation;
    }

    /**
     * Adds a delta time to the current {@link #timeInSimulation}.
     *
     * @param deltaTime The time in seconds which should be added.
     */
    public void addDeltaTime(double deltaTime) {
        timeInSimulation += deltaTime;
    }

    /**
     * Gets the current time.
     *
     * @return The {@link #timeInSimulation} value.
     */
    public double getTime() {
        return timeInSimulation;
    }

    /**
     * Sets the current time {@link #timeInSimulation}.
     *
     * @param time The time inside the simulation.
     */
    public void setTime(double time) {
        timeInSimulation = time;
    }

    /**
     * Adds a specific amount of minutes to the {@link #timeInSimulation}.
     *
     * @param minutes The amount of minutes which should be added.
     */
    public void addMinutesToTime(double minutes) {
        timeInSimulation += minutes * 60;
    }

    /**
     * Calculates the time passed.
     *
     * @param simulationTime The {@link SimulationTime} from where we want to check.
     * @return The time since the given simulationTime.
     */
    public double timeSince(SimulationTime simulationTime) {
        return simulationTime.getTime() - this.getTime();
    }

    /**
     * To get the current time as a string. <br>
     * For debug usage.
     *
     * @return Debug string.
     */
    @Override
    public String toString() {
        return "SimulationTime{" +
                "timeInSimulation=" + timeInSimulation +
                '}';
    }
}
