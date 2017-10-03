package nl.smit.jpackman_qlearning.environment;

import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.smit.jpackman_qlearning.environment.JPacmanStateEnvironment;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateModel;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateModelFactory;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link JPacmanEnvironment}.
 *
 * @author Jordi Smit, 11-7-2017.
 */
class JPacmanStateEnvironmentTest extends JPacmanEnvironmentTest {
    /**
     * The mocked decadencies.
     */
    private MazeStateModelFactory mazeStateModelFactory;
    private MazeStateModel model;

    /**
     * The object under testing.
     */
    private JPacmanStateEnvironment jPacmanStateEnvironment;


    @Override
    protected JPacmanEnvironment createJPacmanEnvironment(
            GameEnvironmentFactory gameEnvironmentFactory, String levelFilePath) {

        setUpMazeStateModelMock();
        initMazeStateModelFactoryMock();

        jPacmanStateEnvironment = new JPacmanStateEnvironment(gameEnvironmentFactory,
                levelFilePath, mazeStateModelFactory);

        setUpMazeStateModelFactoryFunctionalityMock();

        return jPacmanStateEnvironment;
    }

    private void setUpMazeStateModelMock() {
        model = mock(MazeStateModel.class);
    }

    private void initMazeStateModelFactoryMock() {
        mazeStateModelFactory = mock(MazeStateModelFactory.class);
    }

    private void setUpMazeStateModelFactoryFunctionalityMock() {
        when(mazeStateModelFactory.createMazeStateModel(jPacmanStateEnvironment))
                .thenReturn(model);
    }

    /**
     * Verifies that model decadency will be used to find the current state.
     */
    @Test
    void observeState() {

        jPacmanStateEnvironment.observeState();

        //Return value is not needed for the test.
        //noinspection ResultOfMethodCallIgnored
        verify(model).getCurrentState();
    }

    /**
     * Verifies that model will only be created ones.
     */
    @Test
    void modelWillOnlyBeCreatedOnes() {

        observeStateMultipleTimes();

        verify(mazeStateModelFactory, times(1))
                .createMazeStateModel(jPacmanStateEnvironment);
    }

    private void observeStateMultipleTimes() {
        jPacmanStateEnvironment.observeState();
        jPacmanStateEnvironment.observeState();
        jPacmanStateEnvironment.observeState();
        jPacmanStateEnvironment.observeState();
    }

    /**
     * Verifies that the model decadency will be used to get all states.
     */
    @Test
    void getAmountStateFeatures() {

        jPacmanStateEnvironment.getAllStates();

        //Return value is not needed for the test.
        //noinspection ResultOfMethodCallIgnored
        verify(model).getAllStates();
    }
}
