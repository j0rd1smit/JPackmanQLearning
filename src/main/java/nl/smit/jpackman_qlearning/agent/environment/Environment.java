package nl.smit.jpackman_qlearning.agent.environment;

import java.util.Collection;

/**
 * Specifies the required functionality of a environment for a agent to act in it.
 * @param <StateClass> The class that is used to represent the states.
 *
 * @author Jordi Smit, 11-7-2017.
 */
public interface Environment<StateClass> {

    /**
     * Observes the current state and find the corresponding value for it.
     * @return The current state.
     */
    StateClass observeState();

    /**
     * Observes the current state and finds the corresponding value for.
     * @return The the reward value for the current state.
     */
    double observeReward();

    /**
     *
     * @return true iff there are still valid moves left and the agent is able to continue.
     */
    boolean isActive();

    /**
     * Starts the {@link Environment} in the current state.
     * The {@link Environment} will become active in this state.
     */
    void start();

    /**
     * Will pauze the {@link Environment} in the current state.
     * The {@link Environment} will become inactive in this state.
     */
    void stop();

    /**
     * Resets the {@link Environment} by changing the current state to starting state.
     */
    void reset();

    /**
     *
     * @return A list containing all possible actions in the {@link Environment}.
     */
    Collection<Action> getAllActions();
}
