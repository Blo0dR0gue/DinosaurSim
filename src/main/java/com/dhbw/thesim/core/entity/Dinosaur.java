package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.dinosaur.Stand;
import com.dhbw.thesim.core.util.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents a dinosaur object in our simulation.
 * Takes care to update each State and switch between them.
 *
 * @author Daniel Czeschner, Kai Grübener
 * @see com.dhbw.thesim.core.statemachine.StateMachine
 * @see State
 */
public class Dinosaur extends SimulationObject {

    public enum dietType {
        carnivore,
        herbivore,
        omnivore
    }

    // TODO comments pls; make final
    private double nutritionFull;
    private double hydrationFull;
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
    private dietType diet;
    private double viewRange;

    private char gender;
    private double reproductionValue;

    /**
     * Used, when the dinosaur got caught by a hunter. <br>
     * The dinosaur should not run away when his dies.
     */
    private boolean forceNoOp = false;


    private SimulationObject target;
    private boolean isChased;

    //TODO check values?
    private final static double nutritionReductionRate = 0.1;
    private final static double hydrationReductionRate = 0.25;

    public final static double PROXIMITY_RANGE = 5;

    //TODO remove, if json2object is implemented
    public Dinosaur() {
        super("test", 0, null);
    }

    /**
     * Constructor for a dinosaur object
     */
    public Dinosaur(String name, Image image, double nutrition, double hydration,
                    double strength, double speed, double reproductionRate, double weight, double length, double height,
                    boolean canSwim, boolean canClimb, char diet, double viewRange,
                    double interactionRange, char gender) {
        super(name, interactionRange, image);
        //TODO

        this.diet = diet == 'a' ? dietType.omnivore : diet == 'f' ? dietType.carnivore : dietType.herbivore;

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

        this.nutritionFull = this.nutrition;
        this.hydrationFull = this.hydration;

        //TODO remove test objects
        this.circle = new Circle(0, 0, interactionRange, this.diet == dietType.herbivore ? Color.GREEN : this.diet == dietType.omnivore ? Color.BLUE : Color.RED);

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
        stateMachineTick(currentSimulationData);
        //TODO move to stateMachineTick?
        currentState.update(deltaTime, currentSimulationData);

        decreaseLifeStats(deltaTime);
    }

    @Override
    public void eat() {
        setNutrition(-100);
        setHydration(-100);
    }

    @Override
    public boolean canBeEaten(double checkValue) {
        //If the other dinosaur is stronger than this one, it can be eaten by the other.
        //And this dino needs to be alive.
        return checkValue > getStrength() && getHydration() > 0 && getNutrition() > 0;
    }

    /**
     * Reduces the {@link #nutrition} and {@link #hydration} values of the dinosaur.
     *
     * @param deltaTime The time since the last update call in seconds.
     */
    private void decreaseLifeStats(double deltaTime) {
        this.hydration -= hydrationReductionRate * deltaTime;
        this.nutrition -= nutritionReductionRate * deltaTime;
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
        circle.setTranslateX(position.getX());
        circle.setTranslateY(position.getY());
    }

    public void setTest(Vector2D target) {
        test.setTranslateX(target.getX());
        test.setTranslateY(target.getY());
    }

    /**
     * Getter & Setter Methods for a {@link Dinosaur}-object.
     */
    public dietType getDiet() {
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

    /**
     * Force the dinosaur to the {@link com.dhbw.thesim.core.statemachine.state.dinosaur.NoOp} state. <br>
     * Used when the dinosaur got caught by his hunter.
     */
    public void forceNoOp() {
        this.forceNoOp = true;
    }

    /**
     * Resets the force.
     *
     * @see #forceNoOp
     */
    public void resetForceNoOp() {
        this.forceNoOp = false;
    }

    /**
     * Is the dinosaur forced to the stand state
     *
     * @return true, if it is forced to the stand state.
     */
    public boolean isForcedToNoOp() {
        return this.forceNoOp;
    }

    public void setIsChased(boolean chased) {
        isChased = chased;
    }

    /**
     * Checks, if the {@link Dinosaur} is hungry. <br>
     * A dinosaur is hungry, if his {@link #nutrition} is 50% or less of the max level.
     *
     * @return true, if the dinosaur is hungry.
     */
    public boolean isHungry() {
        return nutrition / nutritionFull <= 0.5;
    }

    public double getMaxNutrition() {
        return nutritionFull;
    }

    public double getMaxHydration() {
        return hydrationFull;
    }

    /**
     * Checks, if the {@link Dinosaur} is thirsty. <br>
     * A dinosaur is thirsty, if his {@link #hydration} is 50% or less of the max level.
     *
     * @return true, if the dinosaur is thirsty.
     */
    public boolean isThirsty() {
        return hydration / hydrationFull <= 0.5;
    }

    /**
     * Checks, if the {@link Dinosaur} died of thirst. <br>
     * A dinosaur died of thirst, if his {@link #hydration} is 0.
     *
     * @return true, if the dinosaur died of thirst.
     */
    public boolean diedOfThirst() {
        return hydration <= 0;
    }

    /**
     * Checks, if the {@link Dinosaur} died of starvation. <br>
     * A dinosaur died of starvation, if his {@link #nutrition} is 0.
     *
     * @return true, if the dinosaur died of starvation.
     */
    public boolean diedOfHunger() {
        return nutrition <= 0;
    }

}
