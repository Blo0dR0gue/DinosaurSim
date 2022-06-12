package com.dhbw.thesim.core.util;

public class SimulationTime {

    /**
     * The time in seconds.
     */
    private double timeInSimulation;

    public SimulationTime(){
        timeInSimulation = 0;
    }

    public void addDeltaTime(double deltaTime){
        timeInSimulation += deltaTime;
    }

    public double getTime(){
        return timeInSimulation;
    }

    public void setTime(double time){
        timeInSimulation = time;
    }

    public void addMinutesTime(double minutes){
        timeInSimulation += minutes * 60;
    }

    public double timeSince(SimulationTime simulationTime){
        return simulationTime.getTime() - this.getTime();
    }


}
