package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.plant.Growing;
import com.dhbw.thesim.core.statemachine.state.plant.Grown;
import javafx.scene.image.Image;

/**
 * Represents a plant inside the simulation, which can be eaten. <br>
 * Takes care of updating and switching between plant states.
 *
 * @author Daniel Czeschner, Kai Grübener
 * @see SimulationObject
 * @see com.dhbw.thesim.core.statemachine.StateMachine
 * @see State
 */
public class Plant extends SimulationObject {

    //region variables

    /**
     * The current level the {@link Plant} is grown.
     */
    private double growth;

    /**
     * The rate the {@link Plant} is growing per update.
     */
    private final double growthRate;

    //region constants

    /**
     * The level, where a {@link Plant} is grown.
     */
    public static final double MAX_GROWTH = 100;

    //endregion

    //endregion

    /**
     * Constructor for a {@link Plant}-object.
     *
     * @param type             The type of the {@link Plant}.
     * @param image            The image for the graphical representation.
     * @param interactionRange The range for interactions with other {@link SimulationObject}-objects. (in pixels)
     * @param growthRate       The rate this {@link Plant} is growing per update.
     * @see SimulationObject
     */
    public Plant(String type, Image image, double interactionRange, double growthRate) {
        super(type, interactionRange, image);
        this.growthRate = growthRate;

        //75% chance, that a plant is already grown at the start.
        if (Math.random() > 0.25) {
            this.growth = MAX_GROWTH;
            this.setState(new Grown(this));
        } else {
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
        //Check all transitions for the current state and switch to next state is one met
        stateMachineTick(currentSimulationData);
        //Update the current state
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

    /**
     * Call if this {@link Plant} gets eaten.
     */
    @Override
    public void eat() {
        setGrowth(0);
    }

    /**
     * Checks, if this {@link Plant} can be eaten.
     *
     * @param checkValue A value of another {@link SimulationObject}, which is used to check, if the other {@link SimulationObject} can eat this {@link Plant}.
     * @return true, if the {@link Plant} is grown.
     * @see #isGrown()
     */
    @Override
    public boolean canBeEaten(double checkValue) {
        return isGrown();
    }

    //region getter & setter

    /**
     * Gets the current level the {@link Plant} is grown.
     *
     * @return The {@link #growth} value.
     */
    public double getGrowth() {
        return growth;
    }

    /**
     * Gets the rate the {@link Plant} is growing per update.
     *
     * @return The {@link #growthRate} value.
     */
    public double getGrowthRate() {
        return growthRate;
    }

    /**
     * Sets the current level the {@link Plant} in grown.
     *
     * @param growth The new {@link #growth} value.
     */
    public void setGrowth(double growth) {
        this.growth = growth;
    }

    /**
     * Is this {@link Plant} grown?
     *
     * @return true, if the {@link #growth} reached {@link #MAX_GROWTH}
     */
    public boolean isGrown() {
        return growth >= MAX_GROWTH;
    }

    //endregion

}
