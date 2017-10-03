package nl.smit.jpackman_qlearning.interfaces;

/**
 * Required functionality for a model of a {@link Environment}.
 * @param <StateClass> The class that is used to represent the states.
 *
 * @author Jordi Smit, 18-7-2017.
 */
public interface Model<StateClass> {

    /**
     * Updates the models state if the enviroment has changed.
     * Then return the current representation of the {@link Environment}.
     * @return current representation of the {@link Environment}.
     */
    StateClass getCurrentState();

}
