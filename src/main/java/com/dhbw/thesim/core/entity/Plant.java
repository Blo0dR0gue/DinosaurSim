package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.plant.Growing;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
    public static final double MAX_GROWTH = 100;

    //TODO comments, make final
    private Plant.plantType plantType;
    private double growth;
    private double growthRate; //TODO brauchen wir das noch? Oder ist das global für alle Pflanzen gültig und in der Logik gespeichert?

    //TODO remove, if json2object is implemented
    public Plant() {
        super("test", 0, "nNn");
    }

    public Plant(String name, String imgName, double interactionRange, double growthRate) {
        super(name, interactionRange, "/plant/" + imgName);
        this.growthRate = growthRate;
        this.circle = new Circle(0, 0, interactionRange, Color.GREEN);
        this.setState(new Growing(this));
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
        stateMachineTick(currentSimulationData);
        currentState.update(deltaTime, currentSimulationData);
    }

    /**
     * Updates the visuals for a {@link Plant}-object.
     */
    @Override
    public void updateGraphics() {
        //TODO do only once
        imageObj.setTranslateX(position.getX() - renderOffset.getX());
        imageObj.setTranslateY(position.getY() - renderOffset.getY());
        circle.setTranslateX(position.getX());
        circle.setTranslateY(position.getY());
    }

    @Override
    public void eat() {
        setGrowth(0);
    }

    @Override
    public boolean canBeEaten(double checkValue) {
        return isGrown();
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
     *
     * @return true, if the {@link #growth} reached {@link #MAX_GROWTH}
     */
    public boolean isGrown() {
        return growth >= MAX_GROWTH;
    }
}
