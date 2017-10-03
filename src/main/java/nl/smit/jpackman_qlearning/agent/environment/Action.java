package nl.smit.jpackman_qlearning.agent.environment;

/**
 * A action that can be preformed to the {@link Environment}.
 *
 * @author Jordi Smit, 3-7-2017.
 */
@FunctionalInterface
public interface Action {

    /**
     * execute's the action function.
     */
    void execute();
}
