package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.dinosaur.Stand;

import java.util.HashMap;

/**
 * Represents a dinosaur object in our simulation.
 * Takes care to update each State and switch between them.
 *
 * @author Daniel Czeschner, Kai Grübener
 * @see com.dhbw.thesim.core.statemachine.StateMachine
 * @see State
 */
public class Dinosaur extends SimulationObject {

    //English?
    //TODO do we need this? Maybe to convert the diet char to a enum for easy usage.
    public enum dietType {
        carnivore,
        herbivore,
        omnivore
    }
    // TODO comments pls; make final
    private double nutrition;
    private double hydration;
    private double strength;
    private double speed;
    private double reproductionRate;
    private double weight;
    private double length;
    private double height;
    private boolean canSwim;
    private boolean canClimb;
    private char diet;
    private double viewRange;

    private char gender;
    private double reproductionValue;


    private SimulationObject target;
    private boolean isChased;

    //TODO check values?
    private final static double nutritionReductionRate = 0.4;
    private final static double hydrationReductionRate = 0.4;

    public final static int PROXIMITY_RANGE = 5;

    //TODO remove, if json2object is implemented
    public Dinosaur(){
        super("test",0, "NaN");
    }

    /**
     * Constructor for a dinosaur object
     */
    public Dinosaur(String name, String imgName, double nutrition, double hydration,
                    double strength, double speed, double reproductionRate, double weight, double length, double height,
                    boolean canSwim, boolean canClimb, char diet, double viewRange,
                    double interactionRange, char gender){
        super(name, interactionRange, "/dinosaur/"+imgName);
        //TODO
        //this.dinoType = dinoType;
        this.diet = diet;
        this.nutrition = nutrition;
        this.hydration = hydration;
        this.strength = strength;
        this.speed = speed;
        this.weight = weight;
        this.length = length;
        this.height = height;
        this.gender = gender;
        this.canSwim = canSwim;
        this.canClimb = canClimb;
        this.reproductionRate = reproductionRate;
        this.viewRange = viewRange;

        //Initial reproduction value as specified in the software design. This value increases over time.
        this.reproductionValue = 0;

        //TODO check - maybe in states?
        this.target = null;
        this.isChased = false;

        setState(new Stand(this));
    }

    /**
     * Handles the updates for each {@link State} of a {@link Dinosaur}-object.
     *
     * @param deltaTime             The time since the last update call in seconds.
     * @param currentSimulationData The current {@link Simulation}-Object with all information to the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation currentSimulationData) {
        //Check all transitions for the current state and switch to next state is one met.
        stateMachineTick();
        //TODO move to stateMachineTick?
        currentState.update(deltaTime, currentSimulationData);
    }

    /**
     * Updates the position of the representation for a {@link Dinosaur}-object. <br>
     * It also changes the image if necessary.
     */
    @Override
    public void updateGraphics() {
        //Center the image and update his position.
        imageObj.setTranslateX(position.getX() - renderOffset.getX());
        imageObj.setTranslateY(position.getY() - renderOffset.getY());
    }

    /**
     * Getter & Setter Methods for a {@link Dinosaur}-object.
     */
    public char getDiet() {
        return diet;
    }

    public double getNutrition() {
        return nutrition;
    }

    public double getHydration() {
        return hydration;
    }

    public double getStrength() {
        return strength;
    }

    public double getSpeed() {
        return speed;
    }

    public double getWeight() {
        return weight;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }

    public char getGender() {
        return gender;
    }

    public boolean canSwim() {
        return canSwim;
    }

    public boolean canClimb() {
        return canClimb;
    }

    public double getReproductionRate() {
        return reproductionRate;
    }

    public double getReproductionValue() {
        return reproductionValue;
    }

    public double getViewRange() {
        return viewRange;
    }

    public SimulationObject getTarget() {
        return target;
    }

    public boolean isChased() {
        return isChased;
    }

    public void setNutrition(double nutrition) {
        this.nutrition = nutrition;
    }

    public void setHydration(double hydration) {
        this.hydration = hydration;
    }

    public void setStrength(int strength) { //TODO final?
        this.strength = strength;
    }

    public void setSpeed(int speed) { //TODO final?
        this.speed = speed;
    }

    public void setWeight(double weight) { //TODO final?
        this.weight = weight;
    }

    public void setLength(double length) { //TODO final?
        this.length = length;
    }

    public void setHeight(double height) { //TODO final?
        this.height = height;
    }

    public void setReproductionValue(double reproductionValue) {
        this.reproductionValue = reproductionValue;
    }

    public void setTarget(SimulationObject target) {
        this.target = target;
    }

    public void setChased(boolean chased) {
        isChased = chased;
    }
}
