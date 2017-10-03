package nl.smit.jpackman_qlearning.environment.model.numerical;


import nl.smit.jpackman_qlearning.environment.percepts.JPacmanPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the implementation of {@link FeatureModel}.
 *
 * @author Jordi Smit, 26-7-2017.
 */
class FeatureModelTest {

    /**
     * The default values.
     */
    protected static final int ROW_INDEX = 0;
    private static final int NUMERICAL_TRUE = 1;
    private static final Location STARTING_LOCATION = new Location(0, 0);
    private static final Location PELLET_LOCATION = new Location(1, 1);
    private static final Location PELLET_LOCATION_2 = new Location(2, 1);
    private static final List<Location> STARTING_LOCATIONS = Arrays.asList(STARTING_LOCATION);
    private static final List<Location> PELLETS_LOCATIONS = Arrays.asList(PELLET_LOCATION,
            PELLET_LOCATION_2);
    private static final List<Direction> LIST_WITH_ALL_DIRECTIONS = Arrays.asList(Direction.values());

    /**
     * Default featureModel properties.
     */
    private static final int AMOUNT_OF_FEATURES = 5;
    private static final int PELLET_DIST_INDEX = 4;

    /**
     * mocked input.
     */
    private JPacmanPercepts precepts;

    /**
     * The object under testing.
     */
    private FeatureModel featureModel;

    /**
     * Sets up the testing variables.
     */
    @BeforeEach
    void setUpFeatureModelTest() {
        setUpPreceptsMock();

        featureModel = new FeatureModel();
    }

    private void setUpPreceptsMock() {
        precepts = mock(JPacmanPercepts.class);

        when(precepts.getPelletLocations()).thenReturn(PELLETS_LOCATIONS);
        when(precepts.getPlayerLocations()).thenReturn(STARTING_LOCATIONS);
        when(precepts.getMovablePlayerDirection()).thenReturn(LIST_WITH_ALL_DIRECTIONS);
    }


    /**
     * Test if the correct default value will be returned.
     */
    @Test
    void getCurrentStateDefaultValue() {
        INDArray expected = Nd4j.zeros(AMOUNT_OF_FEATURES);

        INDArray result = featureModel.getCurrentState();

        assertThat(result).isEqualTo(expected);
    }


    /**
     * Test if the correct updated state will be returend.
     */
    @Test
    void getCurrentStateAfterFirstUpdate() {
        INDArray expected = expectedVectorForMovingInAllDirectionAndDistanceFirstPellet();

        featureModel.update(precepts);
        INDArray result = featureModel.getCurrentState();

        assertThat(result).isEqualTo(expected);
    }

    private INDArray expectedVectorForMovingInAllDirectionAndDistanceFirstPellet() {
        INDArray expected = Nd4j.ones(AMOUNT_OF_FEATURES);
        double distance = STARTING_LOCATION.distance(PELLET_LOCATION);
        expected.put(ROW_INDEX, PELLET_DIST_INDEX, distance);

        return expected;
    }
}
