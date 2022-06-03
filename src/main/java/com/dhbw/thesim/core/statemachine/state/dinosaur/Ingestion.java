package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;

/**
 * TODO
 *
 * @author Daniel Czeschner
 */
public class Ingestion extends State {

    private double ingestionTime = 2;

    private boolean done = false;


    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link SimulationObject}
     */
    public Ingestion(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        ingestionTime -=deltaTime;
        System.out.println(ingestionTime);
        System.out.println(dinosaur.getTarget());

        if(ingestionTime <= 0){
            if(dinosaur.getTarget() == null){
                //we drink
                dinosaur.setHydration(dinosaur.getMaxHydration());
            }else {
                //we eat
                dinosaur.setNutrition(dinosaur.getMaxNutrition());
                dinosaur.setTarget(null);
            }
            done = true;
        }

    }

    @Override
    public void initTransitions() {

        addTransition(new StateTransition(StateFactory.States.moveToFoodSource,
                simulation -> done && dinosaur.isThirsty() &&
                        simulation.getClosestReachableWaterSource(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.canSwim(), dinosaur.canClimb()) != null));

        addTransition(new StateTransition(StateFactory.States.moveToFoodSource,
                simulation ->
                        done && dinosaur.isHungry() &&
                                simulation.getClosestReachableFoodSourceInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getDiet(), dinosaur.getType(),
                                        dinosaur.canSwim(), dinosaur.canClimb()) != null));

        addTransition(new StateTransition(StateFactory.States.wander, simulation -> done));


    }

}
