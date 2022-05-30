package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.util.Vector2D;

public class Wander extends State {

    private Dinosaur dinosaur;

    private Vector2D target;
    private Vector2D direction;

    public Wander(Dinosaur simulationObject) {
        super(simulationObject);
        dinosaur = (Dinosaur) this.simulationObject;

        //TODO
        target = new Vector2D(500,180);
        //TODO speed, etc
        direction = dinosaur.getPosition().direction(target);
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {
        //TODO
        System.out.println(dinosaur.getPosition());
        if(arrived()){
            dinosaur.setState(new Stand(dinosaur));
        }
        dinosaur.setPosition(dinosaur.getPosition().add(direction.multiply(40*deltaTime)));
    }

    private boolean arrived() {
        return target != null && dinosaur.getPosition().isInRangeOf(target, Dinosaur.PROXIMITY_RANGE);
    }

    private void moveToTarget() {

    }

}
