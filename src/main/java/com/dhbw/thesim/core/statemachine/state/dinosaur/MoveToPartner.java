package com.dhbw.thesim.core.statemachine.state.dinosaur;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.StateTransition;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.util.Vector2D;

import java.util.List;

/**
 * Represents a {@link State} a {@link Dinosaur} can be in. <br>
 * In this {@link State} the handled {@link Dinosaur} tries to move to partner.
 *
 * @author Daniel Czeschner, Lucas Schaffer
 */
public class MoveToPartner extends State {

    //region variable

    /**
     * Helper {@link Dinosaur} variable, to get dinosaur specific variables
     */
    private final Dinosaur dinosaur;

    /**
     * The interaction range of the mate {@link Dinosaur}.
     */
    private double targetInteractionRange;

    /**
     * The direction {@link Vector2D} the partner from the current position to this {@link #dinosaur}.
     */
    private Vector2D direction;

    /**
     * Helper variable to check if the target got reached.
     */
    private boolean reached = false;

    //endregion

    /**
     * Constructor
     *
     * @param simulationObject The handled {@link Dinosaur}
     */
    public MoveToPartner(Dinosaur simulationObject) {
        super(simulationObject);
        this.dinosaur = (Dinosaur) this.simulationObject;
    }

    /**
     * Is called each update call in the {@link com.dhbw.thesim.core.simulation.SimulationLoop}.
     *
     * @param deltaTime  The delta time since the last update call. (in seconds)
     * @param simulation The {@link Simulation} data of the currently running simulation.
     */
    @Override
    public void update(double deltaTime, Simulation simulation) {
        if (dinosaur.getPartner() == null) {
            dinosaur.setPartner((Dinosaur) simulation.getClosestReachableSuitablePartnerInRange(dinosaur.getPosition(), dinosaur.getViewRange(), dinosaur.getType(), dinosaur.canSwim(), dinosaur.canClimb(), dinosaur.getGender()));

            if (dinosaur.getPartner() != null)
                dinosaur.getPartner().setPartner(dinosaur);
        }

        if (dinosaur.getPartner() != null) {
            targetInteractionRange = dinosaur.getPartner().getInteractionRange();
            direction = dinosaur.getPosition().directionToTarget(dinosaur.getPartner().getPosition());
            dinosaur.flipImage(direction);
        }

        if (direction != null) {
            //Only move if it is the male dinosaur.
            if (dinosaur.getGender() == 'm')
                simulationObject.setPosition(simulationObject.getPosition().add(direction.multiply(dinosaur.getSpeed() * deltaTime)));
        }


    }

    /**
     * Is called on state exit
     */
    @Override
    public void onExit() {
        if (dinosaur.getPartner().getPartner() != dinosaur || !reached) {
            dinosaur.setPartner(null);
        }
    }

    /**
     * Use to initialize all transitions using {@link #addTransition(StateTransition)}.
     */
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
        addTransition(new StateTransition(StateFactory.States.mate, this::partnerReached));

        //If we can't reach the target anymore -> transition to wander. (Maybe because another dinosaur blocked the direction.)
        addTransition(new StateTransition(StateFactory.States.wander, simulation -> dinosaur.getGender() == 'm' && !simulation.canMoveTo(dinosaur.getPosition(), dinosaur.getPartner().getPosition(), 0, dinosaur.canSwim(), dinosaur.canClimb(), null, true, true, List.of(dinosaur.getPartner()))));

    }

    /**
     * Checks if the partner got reached.
     *
     * @param simulation The current {@link Simulation} data.
     * @return true, if the dinosaur has reached the partner.
     */
    private boolean partnerReached(Simulation simulation) {
        reached = simulation.doTheCirclesIntersect(dinosaur.getPosition(), dinosaur.getInteractionRange(), dinosaur.getPartner().getPosition(), targetInteractionRange);
        return reached;
    }

}
