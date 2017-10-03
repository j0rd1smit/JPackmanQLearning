package nl.smit.jpackman_qlearning.environment;


import nl.smit.jpackman_qlearning.interfaces.FeatureBasedEnvironment;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.model.numerical.AbstractNumericalModel;
import nl.smit.jpackman_qlearning.environment.model.numerical.factory.AbstractNumericalModelFactory;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * A Feature Based implementation of {@link JPacmanEnvironment}.
 *
 * @author Jordi Smit, 24-7-2017.
 */
public class JPacmanFeatureEnvironment
        extends JPacmanEnvironment<INDArray>
        implements FeatureBasedEnvironment {

    /**
     * A factory that is able to create a numerical model of this environment.
     */
    private AbstractNumericalModelFactory numericalModelFactory;

    /**
     * A numerical model of the environment.
     */
    @MonotonicNonNull
    private AbstractNumericalModel model;


    /**
     * The default constructor for the {@link JPacmanFeatureEnvironment}.
     *
     * @param gameEnvironmentFactory A factory that creates new GameEnvironments.
     * @param levelFilePath          The path to the file that will be used to parse the level.
     * @param numericalModelFactory  A factory that is able to create a numerical model
     *                               of this environment.
     */
    public JPacmanFeatureEnvironment(GameEnvironmentFactory gameEnvironmentFactory,
                                     String levelFilePath,
                                     AbstractNumericalModelFactory numericalModelFactory) {
        super(gameEnvironmentFactory, levelFilePath);
        this.numericalModelFactory = numericalModelFactory;
    }

    private AbstractNumericalModel getModel() {
        if (model == null) {
            this.model = numericalModelFactory.create(this);
        }
        return model;
    }

    @Override
    public INDArray observeState() {
        return getModel().getCurrentState();
    }

    @Override
    public int getAmountStateFeatures() {
        return getModel().getAmountOfFeatures();
    }
}
