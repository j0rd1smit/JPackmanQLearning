package nl.smit.jpackman_qlearning.agent;

import nl.smit.jpackman_qlearning.interfaces.Action;
import nl.smit.jpackman_qlearning.interfaces.Environment;
import nl.smit.jpackman_qlearning.interfaces.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the implementation functionality of the {@link ReinforcementAgent}.
 *
 * @author Jordi Smit, 11-7-2017.
 */
class ReinforcementAgentTest {
    /**
     * Default values.
     */
    private static final double REWARD = 5;
    private static final int AMOUNT_OF_ITERATIONS = 1;


    /**
     * Depandecies required for the object to be tested.
     * That will be mocked.
     */
    private Environment environment;
    private ReinforcementLogic logic;
    private State state1;
    private Action action;

    /**
     * The object under testing.
     */
    private ReinforcementAgent<State> agent;

    /**
     * Resets the testing variables.
     */
    @BeforeEach
    void setUpReinforcementAgentTest() {
        setUpMocks();

        //noinspection unchecked can not be done for mocking.
        agent = new ReinforcementAgent<State>(environment, logic);
    }

    private void setUpMocks() {
        state1 = mock(State.class);
        action = mock(Action.class);

        setUpLogicMock();
        setUpEnviromentMock();
    }


    private void setUpLogicMock() {
        logic = mock(ReinforcementLogic.class);

        //noinspection unchecked can not be done for mocking.
        when(logic.selectAction(state1)).thenReturn(action);
        when(logic.selectRandomAction()).thenReturn(action);
    }


    private void setUpEnviromentMock() {
        environment = mock(Environment.class);
        when(environment.observeState()).thenReturn(state1);
        when(environment.observeReward()).thenReturn(REWARD);
    }


    /**
     * Verify that the enviroment will be started and stopped after learning.
     */
    @Test
    void startAndStopTest() {
        when(environment.isActive()).thenReturn(true);
        agent.preformMoves(AMOUNT_OF_ITERATIONS);

        Mockito.verify(environment, times(1)).start();
        Mockito.verify(environment, times(1)).stop();
    }

    /**
     * Verify that the environment will be restarted during learning,
     * when the enviroment has become in active.
     */
    @Test
    //only usage not worth to make a instance variable of it
    @SuppressWarnings("MagicNumber")
    void restartTest() {
        when(environment.isActive()).thenReturn(true, false, true);
        int requiredIterationToTriggerARestart = 3;

        agent.preformMoves(requiredIterationToTriggerARestart);
        Mockito.verify(environment, times(2)).start();
        Mockito.verify(environment, times(1)).reset();
    }


    /**
     * Verify that when the agent is not learning it will always select the best action.
     */
    @Test
    void preformBestAction() {
        agent.setLearning(false);
        int amountOfIterations = 100;

        agent.preformMoves(amountOfIterations);

        verify(logic, times(amountOfIterations)).selectAction(any());
    }

}
