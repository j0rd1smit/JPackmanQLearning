package nl.smit.jpackman_qlearning.agent;

import nl.smit.jpackman_qlearning.interfaces.Action;
import nl.smit.jpackman_qlearning.interfaces.State;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Tests the implementation of {@link QTable}.
 *
 * @author Jordi Smit, 4-7-2017.
 */
class QTableTest {
    /**
     * The random seed to controll the randomness during testing.
     */
    private static final long SEED = 1;

    /**
     * The allowed double error offset.
     */
    private static final double OFFSET = 1e-6;

    /**
     * The default reward input.
     */
    private static final double REWARD = 10.0;

    /**
     * The discount the future reward factor.
     */
    private static final double DISCOUNT_FACTOR = 0.9;

    /**
     * The learning rate for the q values.
     */
    private static final double LEARNING_RATE = 0.1;

    /**
     * A mock of Action.
     */
    @Mock
    private Action action1;

    /**
     * A mock of Action.
     */
    @Mock
    private Action action2;

    /**
     * A mock of State.
     */
    @Mock
    private State state1;

    /**
     * A mock of State.
     */
    @Mock
    private State state2;
    /**
     * A mock of State.
     */
    @Mock
    private State state3;

    /**
     * The object under testing.
     */
    private QTable qTable;

    /**
     * Sets up the testing variables.
     */
    @BeforeEach
    void setUpQTableTest() {
        MockitoAnnotations.initMocks(this);

        List<Action> actions = Arrays.asList(action1, action2);
        List<State> states =  Arrays.asList(state1, state2, state3);

        QTable.setSeed(SEED);
        qTable = new QTable(LEARNING_RATE, DISCOUNT_FACTOR, states, actions);
    }

    /**
     * Calculates the current Q value after a update.
     * Q[s,a] = Q[s,a] + α(r + γmaxa' Q[s',a'] - Q[s,a])
     *
     * @param originalValue   Orignal q value.
     * @param maxFutureReward Max future reward
     * @return The newly expected Q value.
     */
    private double expectedQValueUpdate(double originalValue, double maxFutureReward) {
        return originalValue + LEARNING_RATE
                * (REWARD + DISCOUNT_FACTOR * maxFutureReward - originalValue);
    }

    /**
     * Functional test.
     * Will test if the action with highest q value will be chosen.
     */
    @Test
    void selectActionTest() {
        QTransition<State> qTransition = new QTransition<>(state3, state2, action1, REWARD);

        qTable.update(qTransition);

        assertThat(qTable.selectAction(state3)).isEqualTo(action1);
    }

    /**
     * Functional test
     * Test if a random action will be returned.
     * Based on the random seed this should be action1.
     */
    @Test
    void selectRandomAction() {
        QTable.setSeed(SEED);

        Action result = qTable.selectRandomAction();

        assertThat(result).isEqualTo(action2);
    }

    /**
     * Functional test.
     * Will test if the Q value for a given state and action has been update to the correct value.
     */
    @SuppressWarnings("UnnecessaryLocalVariable") //make the test more readable.
    @Test
    void updateQTableTest() {
        double defaultValue = qTable.getQValue(state1, action1);
        //gets the arg max future reward using the select best action function.
        double defaultMaxFuture = qTable.getQValue(state2, qTable.selectAction(state2));
        double expected = expectedQValueUpdate(defaultValue, defaultMaxFuture);

        QTransition<State> qTransition = new QTransition<>(state1, state2, action1, REWARD);
        qTable.update(qTransition);

        assertThat(qTable.getQValue(state1, action1)).isCloseTo(expected, Offset.offset(OFFSET));
    }

    /**
     * Functional test.
     * Will test if the Q value for a given state
     * and action has been update to the correct value for multiple updates.
     */
    @SuppressWarnings("UnnecessaryLocalVariable") //make the test more readable.
    @Test
    void updateQTableMultipleTimeTest() {
        double defaultValue = qTable.getQValue(state1, action1);
        //gets the arg max future reward using the select best action function.
        double defaultMaxFuture = qTable.getQValue(state2, qTable.selectAction(state2));
        double expected = expectedQValueUpdate(defaultValue, defaultMaxFuture);
        expected = expectedQValueUpdate(expected, defaultMaxFuture);

        QTransition<State> qTransition = new QTransition<>(state1, state2, action1, REWARD);
        qTable.update(qTransition);
        qTable.update(qTransition);

        assertThat(qTable.getQValue(state1, action1)).isCloseTo(expected, Offset.offset(OFFSET));
    }

    /**
     * Functional test.
     * Will test if the Q value for a given state and action has been update to the correct value,
     * for a non default future reward value.
     */
    @Test
    void updateQTableNonDefaultFutureReward() {
        QTransition<State> qTransition = new QTransition<>(state2, state3, action2, REWARD);
        qTable.update(qTransition);
        double currentQValue = qTable.getQValue(state1, action1);
        double maxFutureReward = qTable.getQValue(state2, action2);
        double expected = expectedQValueUpdate(currentQValue, maxFutureReward);

        QTransition<State> otherQTransition = new QTransition<>(state1, state2, action1, REWARD);
        qTable.update(otherQTransition);

        assertThat(qTable.getQValue(state1, action1)).isCloseTo(expected, Offset.offset(OFFSET));
    }

}
