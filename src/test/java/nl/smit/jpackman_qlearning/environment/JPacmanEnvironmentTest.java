package nl.smit.jpackman_qlearning.environment;

import nl.smit.jpackman_qlearning.agent.environment.Action;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment.GameEnvironmentObserver;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.helper.JPacmanFactory;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanPercepts;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The case for the {@link JPacmanEnvironment} that can be used for all implementations.
 *
 * @author Jordi Smit, 26-7-2017.
 */
abstract class JPacmanEnvironmentTest {
    /**
     * The location of smaller testing environment.
     */
    private static final String TEST_FILE_NAME = "/environmentLauncherTest.txt";



    /**
     * Default values.
     */
    private static final double STARTING_REWARD = 0.0;
    private static final double DOUBLE_MARGIN = 1e-8;
    private static final int ACTION_INDEX = 0;
    private static final int MOVE_TO_PELLET_INDEX = 3;
    private static final double VALUE_OF_A_PELLET = 10;
    private static final JPacmanFactory JPACMAN_FACTORY = new JPacmanFactory();

    /**
     * Depandancies that are to complex to mock.
     * They will be created using the {@link JPacmanFactory}.
     */
    private Player player;
    private GameEnvironment gameEnvironment;
    private GameEnvironment gameEnvironmentAfterRestart;
    private Player playerAfterRestart;

    /**
     * Depandancies that can be mocked.
     */
    private GameEnvironmentObserver gameEnvironmentObserver;
    private GameEnvironmentFactory gameEnvironmentFactory;

    /**
     * The object under testing.
     */
    private JPacmanEnvironment jPacmanEnvironment;




    /**
     * Sets up the testing variables.
     */
    @BeforeEach
    void setUpJPacmanEnvironmentTest() {
        setUpGameEnvironmentObserverMock();

        setUpGameEnvironmentFactory();

        jPacmanEnvironment = createJPacmanEnvironment(gameEnvironmentFactory, TEST_FILE_NAME);

        jPacmanEnvironment.addGameObserver(gameEnvironmentObserver);
    }

    private void setUpGameEnvironmentObserverMock() {
        gameEnvironmentObserver = mock(GameEnvironmentObserver.class);
    }

    /**
     * Creates the testing object.
     * @param gameEnvironmentFactory The GameEnvironmentFactory depandancy.
     * @param levelFilePath The File path constructor input.
     * @return The object to be tested.
     */
    protected abstract JPacmanEnvironment createJPacmanEnvironment(
            GameEnvironmentFactory gameEnvironmentFactory, String levelFilePath);

    private void setUpGameEnvironmentFactory() {
        gameEnvironmentFactory = mock(GameEnvironmentFactory.class);

        setUpGameEnvironmentAndPlayer();
        setUpGameEnvironmentAndPlayerAfterRestart();

        when(gameEnvironmentFactory.createGameEnvironment(TEST_FILE_NAME))
                .thenReturn(gameEnvironment, gameEnvironmentAfterRestart);
    }

    private void setUpGameEnvironmentAndPlayer() {
        Level level = Mockito.spy(JPACMAN_FACTORY.makeLevelFormFile(TEST_FILE_NAME));
        player = Mockito.spy(JPACMAN_FACTORY.createPlayer());
        gameEnvironment = Mockito.spy(new GameEnvironment(player, level));
    }

    private void setUpGameEnvironmentAndPlayerAfterRestart() {
        Level level = Mockito.spy(JPACMAN_FACTORY.makeLevelFormFile(TEST_FILE_NAME));
        playerAfterRestart = Mockito.spy(JPACMAN_FACTORY.createPlayer());
        gameEnvironmentAfterRestart = Mockito.spy(new GameEnvironment(playerAfterRestart, level));
    }




    /**
     * Test if the game does not start by default.
     */
    @Test
    void isActiveDefaultTest() {
        assertThat(jPacmanEnvironment.isActive()).isFalse();
    }

    /**
     * State transition test.
     * Test if the start and stop methods change the isActive value to the correct value.
     */
    @Test
    void stopTest() {
        jPacmanEnvironment.start();
        assertThat(jPacmanEnvironment.isActive()).isTrue();

        jPacmanEnvironment.stop();
        assertThat(jPacmanEnvironment.isActive()).isFalse();
    }

    /**
     * Verify that the change game method has been called.
     */
    @Test
    void restartTest() {

        jPacmanEnvironment.reset();

        GameEnvironment result = jPacmanEnvironment.getGame();
        assertThat(result).isEqualTo(gameEnvironmentAfterRestart);
    }

    /**
     * Test that the reset method does not yet start the game.
     */
    @Test
    void restartLeavesTheGamePauzed() {
        jPacmanEnvironment.start();

        jPacmanEnvironment.reset();

        assertThat(jPacmanEnvironment.isActive()).isFalse();
    }

    /**
     * Test if the update method with the correct percepts has been called,
     * After a action that changes the state has been executed.
     */
    @Test
    void observeStateTest() {
        JPacmanPercepts perceptsMock = setUpPerceptMock(gameEnvironment);

        performActionSoAnUpdateIsRequired();

        verify(gameEnvironmentObserver, Mockito.times(1)).update(perceptsMock);
    }

    private JPacmanPercepts setUpPerceptMock(GameEnvironment gameEnvironment) {
        JPacmanPercepts jPacmanPercepts = mock(JPacmanPercepts.class);
        when(gameEnvironment.getPercepts()).thenReturn(jPacmanPercepts);

        return jPacmanPercepts;
    }

    private void performActionSoAnUpdateIsRequired() {
        jPacmanEnvironment.start();
        Action action = ((List<Action>) jPacmanEnvironment.getAllActions()).get(ACTION_INDEX);
        action.execute();
    }



    /**
     * Test if the update method with the correct percepts has been called,
     * After a action that changes the state has been executed and the game has been reseted.
     */
    @Test
    void observeStateAfterReseting() {
        JPacmanPercepts perceptsMock = setUpPerceptMock(gameEnvironmentAfterRestart);

        resetAndperformActionSoAnUpdateIsRequired();

        verify(gameEnvironmentObserver, Mockito.times(2)).update(perceptsMock);
    }



    private void resetAndperformActionSoAnUpdateIsRequired() {
        jPacmanEnvironment.reset();
        performActionSoAnUpdateIsRequired();
    }

    /**
     * Test if the model has been added to the observer list.
     * Must happen else the model will not update.
     */
    @Test
    void modelHasBeenAddedToTheObserverList() {
        verify(gameEnvironment, Mockito.times(1)).addObserver(gameEnvironmentObserver);
    }

    /**
     * Test if the model has been added to the observer list after the game has been reseted.
     * Must happen else the model will not update.
     */
    @Test
    void modelHasBeenAddedToTheObserverListAfterRestingTheGame() {

        jPacmanEnvironment.reset();

        verify(gameEnvironment, Mockito.times(1)).addObserver(gameEnvironmentObserver);
    }

    /**
     * State transion test.
     * Tests the following state transition:
     * 0 reward -> 1 pellet reward.
     * Triggered by moving to a pellet.
     */
    @Test
    void observeRewardTest() {
        startEnviromentAndCheckStartingReward();

        moveToPellet();

        assertThat(jPacmanEnvironment.observeReward())
                .isCloseTo(VALUE_OF_A_PELLET, Offset.offset(DOUBLE_MARGIN));
    }

    private void startEnviromentAndCheckStartingReward() {
        jPacmanEnvironment.start();
        assertThat(jPacmanEnvironment.observeReward())
                .isCloseTo(STARTING_REWARD, Offset.offset(DOUBLE_MARGIN));
    }

    private void moveToPellet() {
        Action action = ((List<Action>) jPacmanEnvironment.getAllActions())
                .get(MOVE_TO_PELLET_INDEX);

        action.execute();
    }


    /**
     * State transion test.
     * Tests the following state transition:
     * 0 reward -> 1 pellet reward -> 0 reward -> 1 pellet reward.
     * Triggered by moving to a pellet, then reseting, and moving to the same pellet.
     */
    @Test
    void observeRewardWithReset() {
        observeRewardTest();

        jPacmanEnvironment.reset();
        jPacmanEnvironment.start();

        observeRewardTest();
    }

    /**
     * Test if all the action execute the correct move action.
     *
     * @param actionIndex The index of the correct action.
     * @param direction   The direction in which the action should move.
     */
    @ParameterizedTest(name = "move in direction {1} action")
    @CsvSource({
            "0, NORTH",
            "1, EAST",
            "2, SOUTH",
            "3, WEST"
    })
    void getAllActionsAndExecuteTest(int actionIndex, Direction direction) {
        jPacmanEnvironment.start();
        Action action = ((List<Action>) jPacmanEnvironment.getAllActions()).get(actionIndex);

        action.execute();

        verify(gameEnvironment, Mockito.times(1)).move(player, direction);
    }

    /**
     * Verifies that the game environment decadency will be asked to provided the init precepts.
     */
    @Test
    void getInitPercepts() {

        jPacmanEnvironment.getInitPercepts();

        verify(gameEnvironment).getInitPercepts();
    }
}
