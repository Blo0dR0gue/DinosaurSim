package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;

/**
 * It's a plant-object in our simulation, which can be eaten.
 * <p>
 * TODO
 *
 * @author Daniel Czeschner, Kai Grübener
 */
public class Plant extends SimulationObject {

    //TODO do we need this?
    public enum plantType {
        busch,
        baum
    }


    /**
     * The level, where a plant is grown.
     */
    private static final double maxGrowth = 100;

    //TODO comments, make final
    private Plant.plantType plantType;
    private double growth;
    private double growthRate; //TODO brauchen wir das noch? Oder ist das global für alle Pflanzen gültig und in der Logik gespeichert?

    //TODO remove, if json2object is implemented
    public Plant(){
        super("test" , 0, "nNn");
    }

    public Plant(String name, String imgName, double interactionRange, double growthRate) {
        super(name, interactionRange, "/plant/"+imgName);
        this.growthRate = growthRate;
    }

    /**
     * Updates this {@link Plant} each update call.
     *
     * @param deltaTime             The time since the last update call in seconds.
     * @param currentSimulationData The current {@link Simulation}-Object with all information to the currently running simulation.
     * @see com.dhbw.thesim.core.simulation.SimulationLoop
     */
    @Override
    public void update(double deltaTime, Simulation currentSimulationData) {
        //TODO
    }

    /**
     * Updates the visuals for a {@link Plant}-object.
     */
    @Override
    public void updateGraphics() {
        //TODO
    }

    /**
     * Getter & Setter Methods for a {@link Plant}-object.
     */
    public Plant.plantType getPlantType() {
        return plantType;
    }

    public double getGrowth() {
        return growth;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowth(double growth) {
        this.growth = growth;
    }

    /**
     * Is this {@link Plant} grown
     * @return true, if the {@link #growth} reached {@link #maxGrowth}
     */
    public boolean isGrown(){
        return growth >= maxGrowth;
    }
}
