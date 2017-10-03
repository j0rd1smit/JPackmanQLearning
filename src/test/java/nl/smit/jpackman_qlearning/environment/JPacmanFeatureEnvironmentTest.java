package nl.smit.jpackman_qlearning.environment;


import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.model.numerical.FeatureModel;
import nl.smit.jpackman_qlearning.environment.model.numerical.factory.FeatureModelFactory;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the implementation of the {@link JPacmanFeatureEnvironment}.
 *
 * @author Jordi Smit, 26-7-2017.
 */
public class JPacmanFeatureEnvironmentTest extends JPacmanEnvironmentTest {

    /**
     * The mocked inputs.
     */
    private FeatureModel model;
    private FeatureModelFactory featureModelFactory;

    /**
     * The object under testing.
     */
    private JPacmanFeatureEnvironment jPacmanFeatureEnvironment;


    @Override
    protected JPacmanEnvironment createJPacmanEnvironment(
            GameEnvironmentFactory gameEnvironmentFactory, String levelFilePath) {

        setUpNumericalModelMock();
        initNumericalModelFactoryMock();

        jPacmanFeatureEnvironment = new JPacmanFeatureEnvironment(gameEnvironmentFactory,
                levelFilePath, featureModelFactory);

        //must be done after both the jPacmanFeatureEnvironment
        // and featureModelFactory have been created.
        setUpNumericalModelFactoryFunctionalityMock();

        return jPacmanFeatureEnvironment;
    }

    private void setUpNumericalModelMock() {
        model = mock(FeatureModel.class);
    }

    private void initNumericalModelFactoryMock() {
        featureModelFactory = mock(FeatureModelFactory.class);

    }

    private void setUpNumericalModelFactoryFunctionalityMock() {
        when(featureModelFactory.create(jPacmanFeatureEnvironment)).thenReturn(model);
    }

    /**
     * Verifies that model decadency will be used to find the current state.
     */
    @Test
    void observeState() {

        jPacmanFeatureEnvironment.observeState();

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

        verify(featureModelFactory, times(1))
                .create(jPacmanFeatureEnvironment);
    }

    private void observeStateMultipleTimes() {
        jPacmanFeatureEnvironment.observeState();
        jPacmanFeatureEnvironment.observeState();
        jPacmanFeatureEnvironment.observeState();
        jPacmanFeatureEnvironment.observeState();
    }

    /**
     * Verifies that the model decadency will be used to the amount of features.
     */
    @Test
    void getAmountStateFeatures() {

        jPacmanFeatureEnvironment.getAmountStateFeatures();

        verify(model).getAmountOfFeatures();
    }
}
