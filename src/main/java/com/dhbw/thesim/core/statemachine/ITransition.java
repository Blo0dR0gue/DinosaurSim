package com.dhbw.thesim.core.statemachine;

import com.dhbw.thesim.core.simulation.Simulation;

/**
 * This interface is used to create a transition between {@link com.dhbw.thesim.core.statemachine.state.State}s
 *
 * @author Daniel Czeschner
 */
public interface ITransition {

    /**
     * Checks, if a transition is true
     * @param simulation The current {@link Simulation} data
     * @return true, if the transition conditions are met.
     */
    boolean isMet(Simulation simulation);

}
