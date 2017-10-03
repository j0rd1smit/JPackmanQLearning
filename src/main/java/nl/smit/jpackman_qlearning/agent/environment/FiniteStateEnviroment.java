package nl.smit.jpackman_qlearning.agent.environment;

import java.util.Collection;

/**
 * A interface for environment that have a finite amount of states that can be calculated,
 * at the after initializing the {@link Environment}.
 *
 * @author Jordi Smit, 18-7-2017.
 */
public interface FiniteStateEnviroment extends  Environment<State> {

    /**
     *
     * @return A list containing all possible states in the {@link Environment}.
     */
    Collection<State> getAllStates();
}
