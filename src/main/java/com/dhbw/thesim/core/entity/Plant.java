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

    public enum type{
        busch,
        baum
        // TODO Welche Pflanzentypen gibt es?
    }
    //TODO comments, make final
    private type plantType;
    private double growth;
    private double growthRate; //TODO brauchen wir das noch? Oder ist das global für alle Pflanzen gültig und in der Logik gespeichert?

    //TODO remove, if json2object is implemented
    public Plant(){
        super(0);
    }

    public Plant(type plantType, double growth, double growthRate, double interactionRange) {
        super(interactionRange);
        this.plantType = plantType;
        this.growth = growth;
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
    public type getPlantType() {
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
}
