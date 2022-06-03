package com.dhbw.thesim.core.statemachine.state;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.statemachine.state.dinosaur.*;

/**
 * State factory, which creates {@link State}-objects. <br>
 * Using the Factory Pattern. <br>
 * We use this state factory, because we cant create a object when creating a {@link com.dhbw.thesim.core.statemachine.StateTransition}. This would end in a {@link StackOverflowError}
 * <br>
 * TODO maybe simplify this whole transition, state, etc. thing. (Maybe pass the simobject in the updates?)
 *
 * @author Daniel Czeschner
 */
public class StateFactory {

    /**
     * Enum with all states, which can be used.
     */
    public enum States {
        //Dinosaur states
        stand,
        wander,
        moveToFoodSource,
        mate,
        ingestion,
        hunt,
        escape,
        dead,
        //Plant states
        grow,
        grown
    }

    /**
     * Creates a {@link State}-object based on the {@link States} enum.
     * @param state The {@link States} enum entry for the next state.
     * @param simulationObject The {@link SimulationObject} which is creating the new state.
     * @return The next {@link State}.
     */
    public static State createState(States state, SimulationObject simulationObject){
        if(simulationObject instanceof Dinosaur dinosaur)
            return createDinosaurState(state, dinosaur);
        //TODO plant
        return null;
    }

    /**
     * Creates a State, a {@link Dinosaur} can be in.
     * @param state The {@link States} enum entry for the next state.
     * @param dinosaur The {@link Dinosaur} which is creating the new state.
     * @return The next {@link State}.
     */
    private static State createDinosaurState(States state, Dinosaur dinosaur) {
        return switch (state) {
            case wander -> new Wander(dinosaur);
            case moveToFoodSource -> new MoveToFoodSource(dinosaur);
            case mate -> new Mate(dinosaur);
            case ingestion -> new Ingestion(dinosaur);
            case hunt -> new Hunt(dinosaur);
            case escape -> new Escape(dinosaur);
            case dead -> new Dead(dinosaur);
            case stand -> new Stand(dinosaur);
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }

}
