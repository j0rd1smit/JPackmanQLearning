package nl.smit.jpackman_qlearning.agent;



import nl.smit.jpackman_qlearning.interfaces.Action;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 * A template class used as logic to choice actions based on reinforcement learning.
 * This layer of abstraction is responsible for the general action select.
 * The specific implementation will be done by the sub classes.
 * @param <StateClass> The class that is used to represent the states.
 *
 * @author Jordi Smit, 17-7-2017.
 */

public abstract class ReinforcementLogic<StateClass> {
    /**
     * Random object used to init the table with random values.
     */
    protected static final Random RANDOM = new Random();

    /**
     * The available action of the environment.
     */
    private final List<Action> actions;

    /**
     * Sets the random seed.
     * Should only be used for testing purposes.
     * @param seed The random seed.
     */
    public static void setSeed(long seed) {
        RANDOM.setSeed(seed);
    }

    /**
     * Creates the abstract reinforcement object.
     * @param actions All the possible actions that can be preformed.
     */
    public ReinforcementLogic(Collection<Action> actions) {
        this.actions = new ArrayList<>(new LinkedHashSet<>(actions));
    }


    /**
     * @return A random action from all the possible actions.
     */
    public Action selectRandomAction() {
        int size = actions.size();
        int randomIndex = RANDOM.nextInt(size);

        return actions.get(randomIndex);
    }

    /**
     *
     * @param state The state for which an action is sought.
     * @return The action with the maximum Q value for the given state.
     */
    public Action selectAction(StateClass state) {
        int actionIndex = selectActionIndexWithMaxQValue(state);

        assert actionIndex >= 0 && actionIndex < actionsSize()
                : "The action index must point to a existing action.";

        return actions.get(actionIndex);

    }

    /**
     * Finds the action index with the maximum q value.
     * @param state The state for which the action must be seaerched.
     * @return The index of the action with maximum Q value for the given state.
     */
    protected abstract int selectActionIndexWithMaxQValue(StateClass state);


    /**
     * Updates the underling Q function based on the observed transition.
     * pre: the starting and end state must be valid states.
     * pre: the preformed action must be a known action.
     * post :The underling function wil be updated with new value.
     * @param qTransition The observed transition.
     */
    public abstract void update(QTransition<StateClass> qTransition);

    /**
     * @param action The action how index we want to get.
     * @return The column index of the action.
     */
    protected int getActionIndex(Action action) {
        int index = actions.indexOf(action);

        assert index != -1 : "Requested index of unknown action: " + action;
        return index;
    }

    /**
     *
     * @return The amount of unique actions.
     */
    protected int actionsSize(@UnknownInitialization ReinforcementLogic<StateClass>this) {
        //required by the compiler to ensure that null has not been assigned as final value.
        assert actions != null;

        return actions.size();
    }
}
