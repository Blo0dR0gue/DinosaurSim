package com.dhbw.thesim.core.statemachine;

/**
 * This interface is used to create a transition between {@link com.dhbw.thesim.core.statemachine.state.State}s
 *
 * @author Daniel Czeschner
 */
public interface ITransition {

    boolean isMet();

}
