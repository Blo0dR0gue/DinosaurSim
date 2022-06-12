package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.plant.Growing;
import com.dhbw.thesim.core.statemachine.state.plant.Grown;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * It's a plant-object in our simulation, which can be eaten.
 * <p>
 *
 * @author Daniel Czeschner, Kai Grübener
 */
@SuppressWarnings("unused")
public class Plant extends SimulationObject {

    /**
     * The level, where a plant is grown.
     */
    public static final double MAX_GROWTH = 100;

    /**
     * Used to specify the properties for a {@link Plant}-object.
     */
    private double growth;
    private final double growthRate;

    /**
     * Constructor for a normal {@link Plant}-object.
     * @param name The name of the plant.
     * @param image The image for the graphical illustration.
     * @param interactionRange The range for interactionis with other {@link SimulationObject}-objects.
     * @param growthRate The rate for a plant to grow.
     */
    public Plant(String name, Image image, double interactionRange, double growthRate) {
        super(name, interactionRange, image);
        this.growthRate = growthRate;

        //75% chance, that a plant is already grown at the start.
        if(Math.random() > 0.25 ){
            this.growth = MAX_GROWTH;
            this.setState(new Grown(this));
        }else {
            this.growth = 0;
            this.setState(new Growing(this));
            this.getJavaFXObj().setVisible(false);
        }

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
        imageObj.setTranslateX(position.getX() - renderOffset.getX());
        imageObj.setTranslateY(position.getY() - renderOffset.getY());
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
