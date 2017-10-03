package nl.smit.jpackman_qlearning.agent;


import nl.smit.jpackman_qlearning.interfaces.Action;
import nl.smit.jpackman_qlearning.interfaces.State;
import org.checkerframework.checker.initialization.qual.UnderInitialization;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.indexaccum.IMax;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Creates a Q table for all the possible states and actions.
 *
 * @author Jordi Smit, 3-7-2017.
 */
public class QTable extends ReinforcementLogic<State> {
    /**
     * The available action of the environment.
     */
    private final List<State> states;
    /**
     * The table containing the q values.
     */
    private final INDArray table;
    /**
     * The discount the future reward factor.
     */
    private final double discountFactor;
    /**
     * The learning rate for the q values.
     */
    private final double learningRate;

    /**
     * The default constructor of the {@link QTable}.
     *
     * @param learningRate   The learning rate for the q values.
     * @param discountFactor The discount the future reward factor.
     * @param states         All the possible states.
     * @param actions        The available action of the environment.
     */
    public QTable(double learningRate, double discountFactor, Collection<State> states,
                  Collection<Action> actions) {
        super(actions);
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.states = new ArrayList<>(new LinkedHashSet<>(states));
        this.table  = createTable(this.states);
    }

    private INDArray createTable(@UnderInitialization QTable this, List<State> states) {
        int rows = states.size();
        int cols = actionsSize();
        return Nd4j.rand(rows, cols, RANDOM.nextLong());
    }


    @Override
    public void update(QTransition<State> qTransition) {

        assert updateQTablePreConditions(qTransition);

        int startingStateIndex = getStateIndex(qTransition.getStartingState());
        int preformedActionIndex = getActionIndex(qTransition.getPreformedAction());

        double currentQValue = getQValue(startingStateIndex, preformedActionIndex);
        double updatedQValue = calcUpdatedQValue(qTransition, currentQValue);

        table.put(startingStateIndex, preformedActionIndex, updatedQValue);
    }

    private boolean updateQTablePreConditions(QTransition qTransition) {
        assert states.contains(qTransition.getStartingState())
                : "The stating state must be in the table.";
        assert states.contains(qTransition.getEndState())
                : "The end state must be in the table.";

        return true;
    }

    private int getStateIndex(State state) {
        int index = states.indexOf(state);
        assert index != -1;
        return index;
    }

    private double calcUpdatedQValue(QTransition<State> qTransition, double currentQValue) {

        int endStateIndex = getStateIndex(qTransition.getEndState());
        double discoutedMaxFutureReward = calcDiscoutedMaxFutureReward(endStateIndex);

        double observedReward = qTransition.getObservedReward();

        //Q[s,a] = Q[s,a] + α(r + γmaxa' Q[s',a'] - Q[s,a])
        return currentQValue + learningRate
                * (observedReward + discoutedMaxFutureReward - currentQValue);
    }

    private double calcDiscoutedMaxFutureReward(int endStateIndex) {
        int maxFutureRewardIndex = selectActionWithMaxQValueIndex(endStateIndex);
        return discountFactor
                * table.getRow(endStateIndex).getDouble(maxFutureRewardIndex);
    }

    private double getQValue(int environmentStateIndex, int environmentActionIndex) {
        return table.getDouble(environmentStateIndex, environmentActionIndex);
    }

    @Override
    protected int selectActionIndexWithMaxQValue(State state) {
        assert states.contains(state)
                : "The state must be in the table.";

        int stateIndex = getStateIndex(state);

        return selectActionWithMaxQValueIndex(stateIndex);
    }

    /**
     * @param stateIndex The index of the given state.
     * @return The index of the action with maximum Q value for the given state.
     */
    private int selectActionWithMaxQValueIndex(int stateIndex) {
        INDArray stateRow = table.getRow(stateIndex);
        //System.out.println(stateRow);
        return Nd4j.getExecutioner().execAndReturn(new IMax(stateRow)).getFinalResult();
    }

    /**
     * @param state  The state how's Q value we want.
     * @param action The action how's Q value we want.
     * @return The Q value for a given state and and action.
     */
    double getQValue(State state, Action action) {
        assert states.contains(state)
                : "The state must be in the table.";

        int stateIndex = getStateIndex(state);
        int actionIndex = getActionIndex(action);

        return getQValue(stateIndex, actionIndex);
    }
}
