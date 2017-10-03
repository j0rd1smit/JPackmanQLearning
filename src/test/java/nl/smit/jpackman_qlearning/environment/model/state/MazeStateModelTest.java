package nl.smit.jpackman_qlearning.environment.model.state;

import nl.smit.jpackman_qlearning.interfaces.State;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;

import static nl.tudelft.jpacman.board.Direction.WEST;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test the {@link MazeStateModel}.
 *
 * @author Jordi Smit, 11-7-2017.
 */
class MazeStateModelTest {
    /**
     * The default values.
     */
    private static final Location STARTING_LOCATION = new Location(1, 1);
    private static final Location PELLET_LOCATION = new Location(0, 1);
    private static final State STARTING_STATE = new MazeState(STARTING_LOCATION, WEST);
    private static final int WIDTH = 2;
    private static final int HEIGHT = 2;


    /**
     * mocked input.
     */
    @Mock
    private JPacmanPercepts precepts;
    private JPacmanInitPercepts initPrecepts;

    /**
     * The dependencies.
     */
    @Spy
    private MazeStateFactory factory;


    /**
     * The object under testing.
     */
    private MazeStateModel mazeStateModel;

    /**
     * Set up the testing variables.
     */
    @BeforeEach
    void setUpMazeStateModelTest() {
        factory = spy(new MazeStateFactory());
        precepts = mock(JPacmanPercepts.class);
        setUpJPacmanInitPerceptsMock();

        mazeStateModel = new MazeStateModel(factory, initPrecepts);
    }

    private void setUpJPacmanInitPerceptsMock() {
        initPrecepts = mock(JPacmanInitPercepts.class);

        when(initPrecepts.getBoardHeight()).thenReturn(HEIGHT);
        when(initPrecepts.getBoardWidth()).thenReturn(WIDTH);
    }

    /**
     * Test if the correct init state will be returned.
     */
    @Test
    void initStateIsNull() {
        State result = mazeStateModel.getCurrentState();

        assertThat(result).isNull();
    }


    /**
     * Test if the current state will be updated to the correct starting value.
     * and current state should non longer be null.
     */
    @Test
    void updateWithStartingValues() {
        setUpInitEnviromentPrecept();

        mazeStateModel.update(precepts);

        assertThat(mazeStateModel.getCurrentState()).isEqualTo(STARTING_STATE);
    }


    private void setUpInitEnviromentPrecept() {
        when(precepts.getPlayerLocations()).thenReturn(Arrays.asList(STARTING_LOCATION));
        when(precepts.getPelletLocations()).thenReturn(Arrays.asList(PELLET_LOCATION));
    }



    /**
     * Test if the current state has been updated to the correct new state based on the precepts.
     */
    @Test
    void getCurrentStateAfterMoving() {
        goToStartingState();
        setUpAfterMovingPrecepts();

        mazeStateModel.update(precepts);

        State result = mazeStateModel.getCurrentState();
        State expected = expectedAfterMovingEastState();
        assertThat(result).isEqualTo(expected);
    }

    private void goToStartingState() {
        setUpInitEnviromentPrecept();
        mazeStateModel.update(precepts);
        assertThat(mazeStateModel.getCurrentState()).isEqualTo(STARTING_STATE);
    }


    private void setUpAfterMovingPrecepts() {
        Location playerLocation = STARTING_LOCATION.locationTo(WEST);
        when(precepts.getPlayerLocations()).thenReturn(Arrays.asList(playerLocation));
        when(precepts.getPelletLocations()).thenReturn(new ArrayList<>());
    }

    private State expectedAfterMovingEastState() {
        Location playerLocation = precepts.getPlayerLocations().get(0);

        return new MazeState(playerLocation);
    }

}
