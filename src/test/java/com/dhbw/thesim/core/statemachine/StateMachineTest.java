package com.dhbw.thesim.core.statemachine;

import com.dhbw.thesim.core.entity.Dinosaur;
import com.dhbw.thesim.core.entity.SimulationObject;
import com.dhbw.thesim.core.simulation.Simulation;
import com.dhbw.thesim.core.statemachine.state.State;
import com.dhbw.thesim.core.statemachine.state.StateFactory;
import com.dhbw.thesim.core.statemachine.state.dinosaur.Stand;
import com.dhbw.thesim.core.util.Vector2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link StateMachine}, {@link StateTransition}, {@link ITransition}.
 *
 * @author Daniel Czeschner
 */
class StateMachineTest {

    @DisplayName("Check a State transition")
    @Test
    void stateMachineTickWithTranstion() {
        //arrange
        TestStateMachine testStateMachine = new TestStateMachine();
        Simulation simulation = mock(Simulation.class);
        Dinosaur simulationObject = mock(Dinosaur.class);
        TestState testState = new TestState(simulationObject, true);
        testStateMachine.setState(testState);
        //act
        testStateMachine.stateMachineTick(simulation);
        //assert
        assertEquals(Stand.class.getSimpleName(), testStateMachine.getState().getClass().getSimpleName(), "The new State should be stand.");
    }

    @DisplayName("Don't do the transition")
    @Test
    void stateMachineTickWithNoTransition() {
        //arrange
        TestStateMachine testStateMachine = new TestStateMachine();
        Simulation simulation = mock(Simulation.class);
        Dinosaur simulationObject = mock(Dinosaur.class);
        TestState testState = new TestState(simulationObject, false);
        testStateMachine.setState(testState);
        //act
        testStateMachine.stateMachineTick(simulation);
        //assert
        assertEquals(TestState.class.getSimpleName(), testStateMachine.getState().getClass().getSimpleName(), "The new State should be the old state.");
    }

    /**
     * Test {@link StateMachine}.
     */
    static class TestStateMachine extends StateMachine {
        /**
         * Helper function to get the current state.
         *
         * @return The current {@link State}.
         */
        public State getState() {
            return this.currentState;
        }
    }

    /**
     * Test {@link State} class with an transition to stand.
     */
    static class TestState extends State {

        boolean doTransition;

        /**
         * Constructor for a normal {@link State}-object.
         *
         * @param simulationObject The handled {@link SimulationObject}
         */
        public TestState(SimulationObject simulationObject, boolean doTransition) {
            super(simulationObject);
            this.doTransition = doTransition;
        }

        @Override
        public void onExit() {

        }

        @Override
        public void update(double deltaTime, Simulation simulation) {

        }

        @Override
        public void initTransitions() {
            addTransition(new StateTransition(StateFactory.States.wander, simulation -> false));
            addTransition(new StateTransition(StateFactory.States.stand, simulation -> doTransition));
        }
    }

}