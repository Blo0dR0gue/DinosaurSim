package com.dhbw.thesim.core.entity;

import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.dinosaur.Stand;
import com.dhbw.thesim.core.util.SimulationTime;
import com.dhbw.thesim.gui.Display;
import javafx.scene.image.Image;

/**
 * Represents a dinosaur object in our simulation.
 * Takes care to update each State and switch between them.
 *
 * @author Daniel Czeschner, Kai Grübener, Lucas Schaffer
 * @see com.dhbw.thesim.core.statemachine.StateMachine
 * @see State
 */
public class Dinosaur extends SimulationObject {

    /**
     * Enum with all available diet types for a {@link Dinosaur}.
     */
    public enum dietType {
        CARNIVORE("Fleischfresser"),
        HERBIVORE("Pflanzenfresser"),
        OMNIVORE("Allesfresser");

        public final String translatedText;

        dietType(String translatedText) {
            this.translatedText = translatedText;
        }
    }

    //region variables

    //region dinosaur properties
    /**
     * The maximum level for the nutrition value.
     *
     * @see #nutrition
     */
    private double nutritionFull;

    /**
     * The maximum level for the hydration value.
     *
     * @see #hydration
     */
    private double hydrationFull;

    /**
     * The current level of nutrition. <br>
     * If it is 0, the dinosaur dies.
     */
    private double nutrition;

    /**
     * The current level of hydration. <br>
     * If it is 0, the dinosaur dies.
     */
    private double hydration;

    /**
     * The strength of the dinosaur.
     */
    private final double strength;

    /**
     * The movement speed of the dinosaur.
     */
    private final double speed;

    /**
     * The increase rate for the reproduction value.
     * @see #reproductionValue
     */
    private final double reproductionRate;

    /**
     * The weight of the dinosaur. <br>
     * Has no effect on the simulation
     */
    private final double weight;

    /**
     * The length of the dinosaur. <br>
     * Has no effect on the simulation
     */
    private final double length;

    /**
     * The height of the dinosaur. <br>
     * Has no effect on the simulation
     */
    private final double height;

    /**
     * Describes whether the dino can swim <br>
     * true = dinosaur can swim.
     */
    private final boolean canSwim;

    /**
     * Describes whether the dinosaur can climb <br>
     * true = dinosaur can climb.
     */
    private final boolean canClimb;

    /**
     * The diet type of the dinosaur.
     * @see dietType
     */
    private final dietType diet;

    /**
     * The range in pixels the dinosaur can see.
     */
    private final double viewRange;

    /**
     * The time this dinosaur got created.
     */
    private final SimulationTime timeOfBirth;

    /**
     * The gender of the dinosaur.
     */
    private final char gender;

    /**
     * The current value of reproduction will. <br>
     * A dinosaur willing to mate, if this value reaches his maximum amount.
     * @see #REPRODUCTION_VALUE_FULL
     */
    private double reproductionValue;

    //endregion

    /**
     * Used, when the {@link Dinosaur} got caught by a hunter. <br>
     * The dinosaur should not run away when his dies.
     */
    private boolean forceNoOp = false;

    /**
     * The current target of the {@link Dinosaur}. <br>
     * E.g. the hunted {@link Dinosaur} or the hunter.
     *
     * @see #isChased
     */
    private SimulationObject target;

    /**
     * Defines, if the dinosaur is chased.
     */
    private boolean isChased;

    /**
     * The current target {@link Dinosaur} for mating.
     */
    private Dinosaur partner;

    //region constants
    /**
     * Used as a decrease rate for nutrition  in the simulation.
     */
    private static final double NUTRITION_REDUCTION_RATE = 0.3;

    /**
     * Used as a decrease rate for hydration in the simulation.
     */
    private static final double HYDRATION_REDUCTION_RATE = 0.45;

    /**
     * Defines the max level of the reproduction value.
     *
     * @see #reproductionValue
     */
    private static final double REPRODUCTION_VALUE_FULL = 100;

    /**
     * Defines the proximity range of a dinosaur to a target point.
     */
    public static final double PROXIMITY_RANGE = 5;

    //endregion

    //endregion

    //region constructors

    /**
     * Constructor for a {@link Dinosaur} object.
     */
    public Dinosaur(String name, Image image, double nutrition, double hydration,
                    double strength, double speed, double reproductionRate, double weight, double length, double height,
                    boolean canSwim, boolean canClimb, char diet, double viewRange,
                    double interactionRange, char gender) {
        super(name, interactionRange, image);

        if (diet == 'a')
            this.diet = dietType.OMNIVORE;
        else if (diet == 'f')
            this.diet = dietType.CARNIVORE;
        else
            this.diet = dietType.HERBIVORE;

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
        this.timeOfBirth = new SimulationTime();

        this.nutritionFull = this.nutrition;
        this.hydrationFull = this.hydration;

        //Initial reproduction value as specified in the software design. This value increases over time.
        this.reproductionValue = 0;

        this.target = null;
        this.isChased = false;

        setState(new Stand(this));
    }

    /**
     * Private constructor to create a copy of a {@link Dinosaur}.
     */
    private Dinosaur(String name, Image image, double nutrition, double hydration,
                     double strength, double speed, double reproductionRate, double weight, double length, double height,
                     boolean canSwim, boolean canClimb, char diet, double viewRange,
                     double interactionRange, char gender, double maxNutrition, double maxHydration, double reproductionValue, SimulationObject target, boolean isChased, State state) {
        super(name, interactionRange, image);

        if (diet == 'a')
            this.diet = dietType.OMNIVORE;
        else if (diet == 'f')
            this.diet = dietType.CARNIVORE;
        else
            this.diet = dietType.HERBIVORE;

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
        this.timeOfBirth = new SimulationTime();

        this.nutritionFull = maxNutrition;
        this.hydrationFull = maxHydration;

        //Initial reproduction value as specified in the software design. This value increases over time.
        this.reproductionValue = reproductionValue;

        this.target = target;
        this.isChased = isChased;

        setState(state);
    }

    //endregion

    /**
     * Creates a complete copy of the {@link Dinosaur}.
     *
     * @return A copy of the {@link Dinosaur} object.
     */
    public Dinosaur copyOf() {
        Dinosaur copy = new Dinosaur(this.getType(), this.getJavaFXObj().getImage(), this.nutrition, this.hydration,
                this.strength, this.speed, this.reproductionRate, this.weight, this.length, this.height,
                this.canSwim, this.canClimb, this.getCharDiet(), this.viewRange,
                this.interactionRange, this.gender);

        copy.setReproductionValue(this.reproductionValue);
        copy.setTarget(this.target);
        copy.setIsChased(this.isChased);
        copy.setState(this.currentState);
        copy.setMaxNutrition(this.nutritionFull);
        copy.setMaxHydration(this.hydrationFull);

        return copy;
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
        currentState.update(deltaTime, currentSimulationData);

        updateStats(deltaTime);
    }

    /**
     * Call if this dinosaur gets eaten.
     */
    @Override
    public void eat() {
        setNutrition(0);
        setHydration(0);
    }

    /**
     * Checks, if this {@link Dinosaur} can be eaten by another {@link Dinosaur}.
     *
     * @param checkValue A value of another object, which is used to check, if the other object can eat this object.
     * @return true, if the other {@link Dinosaur} can eat this one.
     */
    @Override
    public boolean canBeEaten(double checkValue) {
        //If the other dinosaur is stronger than this one, it can be eaten by the other.
        //And this dino needs to be alive.
        return checkValue > getStrength() && getHydration() > 0 && getNutrition() > 0;
    }

    /**
     * Checks if this {@link Dinosaur} is willing to mate.
     *
     * @return true if the {@link Dinosaur} is willing to mate.
     */
    public boolean isWillingToMate() {
        return reproductionValue >= REPRODUCTION_VALUE_FULL && !isHungry() && !isThirsty() && !isChased();
    }

    /**
     * Reduces the {@link #nutrition} and {@link #hydration} values of the dinosaur.
     *
     * @param deltaTime The time since the last update call in seconds.
     */
    private void updateStats(double deltaTime) {
        this.hydration -= HYDRATION_REDUCTION_RATE * deltaTime;
        this.nutrition -= NUTRITION_REDUCTION_RATE * deltaTime;
        if (this.reproductionValue < REPRODUCTION_VALUE_FULL)
            this.reproductionValue += reproductionRate * deltaTime;
    }

    /**
     * Updates the position of the graphical representation for this {@link Dinosaur}-object. <br>
     * It also changes the image if necessary.
     */
    @Override
    public void updateGraphics() {
        //Center the image and update his position.
        imageObj.setTranslateX(position.getX() - renderOffset.getX());
        imageObj.setTranslateY(position.getY() - renderOffset.getY());
        this.selectionRing.setTranslateX(position.getX());
        this.selectionRing.setTranslateY(position.getY());
    }

    //region getter & setter
    public dietType getDiet() {
        return diet;
    }

    public char getCharDiet() {

        if (diet == dietType.OMNIVORE)
            return 'a';
        else if (diet == dietType.CARNIVORE)
            return 'f';
        else if (this.diet == dietType.HERBIVORE)
            return 'p';
        else
            return 'x';
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
        return Display.adjustScale(viewRange, Display.SCALE_X);
    }

    public double getRealViewRange() {
        return viewRange;
    }

    public SimulationObject getTarget() {
        return target;
    }

    public Dinosaur getPartner() {
        return partner;
    }

    public void setTimeOfBirth(double time) {
        timeOfBirth.setTime(time);
    }

    public SimulationTime getTimeOfBirth() {
        return timeOfBirth;
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


    private void setMaxHydration(double hydrationFull) {
        this.hydrationFull = hydrationFull;
    }

    private void setMaxNutrition(double nutritionFull) {
        this.nutritionFull = nutritionFull;
    }

    public void setReproductionValue(double reproductionValue) {
        this.reproductionValue = reproductionValue;
    }

    public void setTarget(SimulationObject target) {
        this.target = target;
    }

    public void setPartner(Dinosaur partner) {
        this.partner = partner;
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

    //endregion

}