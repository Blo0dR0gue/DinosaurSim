package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.map.Tile;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries to move to partner.
 *
 * @author Daniel Czeschner, Lucas Schaffer
 */
public class MoveToPartner extends State {

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;


    private double targetInteractionRange;

    /**
     * The normalized direction {@link Vector2D} to the partner
     */
    private Vector2D direction;

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public MoveToPartner(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    @Override
    public void update(double deltaTime, Simulation simulation) {

        if (dinosaur.getPartner() == null) {
            dinosaur.setPartner((Dinosaur) simulation.getClosestReachableSuitablePartnerInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getGender()));

            if(dinosaur.getPartner() != null)
                dinosaur.getPartner().setPartner(dinosaur);
        }

        if(dinosaur.getPartner() != null){
            targetInteractionRange = dinosaur.getPartner().getInteractionRange();
            direction = dinosaur.getPosition().directionToTarget(dinosaur.getPartner().getPosition());
            dinosaur.flipImage(direction);
        }

        if (direction != null) {

            simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));

        }

    }

    @Override
    public void onExit() {
        if (dinosaur.getPartner().getPartner() != dinosaur) {
            dinosaur.setPartner(null);
        }
    }

    @Override
    public void initTransitions() {
        //The dinosaur died.
        addTransition(new StateTransition(StateFactory.States.dead, simulation -> dinosaur.diedOfHunger() || dinosaur.diedOfThirst()));

        //The dinosaur got chased
        addTransition(new StateTransition(StateFactory.States.escape, simulation -> dinosaur.isChased()));

        //The dinosaur lost the partner
        addTransition(new StateTransition(StateFactory.States.stand, simulation -> dinosaur.getPartner() == null));

        //If the other dinosaur found another partner
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getPartner().getPartner() != dinosaur));

        //If we reached the target
        addTransition(new StateTransition(StateFactory.States.mate, this::reached));

        //If we can't reach the target anymore -> transition to wander. (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> !simulation.canMoveTo(dinosaur.getPosition(), dinosaur.getPartner().getPosition(), 0, dinosaur.canSwim(), dinosaur.canClimb(), null, true, true)));

    }

    /**
     * Do we have reached the partner?
     *
     * @param simulation The current {@link Simulation} data.
     * @return true, if we have reached the target.
     */
    private boolean reached(Simulation simulation) {

        return simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getInteractionRange(), dinosaur.getPartner().getPosition(), targetInteractionRange);
    }

}
