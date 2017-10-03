package  nl.smit.jpackman_qlearning.environment.builder;


import nl.smit.jpackman_qlearning.environment.JPacmanFeatureEnvironment;
import nl.smit.jpackman_qlearning.environment.model.numerical.factory.AbstractNumericalModelFactory;
import nl.smit.jpackman_qlearning.environment.model.numerical.factory.FeatureModelFactory;

/**
 * A builder for the {@link JPacmanFeatureEnvironmentBuilder}.
 *
 * @author Jordi Smit, 24-7-2017.
 */
public class JPacmanFeatureEnvironmentBuilder
        extends AbstractJPacmanEnvironmentBuilder<JPacmanFeatureEnvironmentBuilder> {


    private AbstractNumericalModelFactory numericalModelFactory = new FeatureModelFactory();

    /**
     * Tells the builder to use the provided model during the construction.
     *
     * @param factory A factory that can create a NumericalModel.
     * @return The updated builder.
     */
    public JPacmanFeatureEnvironmentBuilder useNumericalModelFactory(
            AbstractNumericalModelFactory factory) {
        this.numericalModelFactory = factory;
        return this;
    }


    /**
     * Build the JPacmanEnvironment.
     *
     * @return The newly created JPacmanEnvironment.
     */
    public JPacmanFeatureEnvironment build() {
        String levelFilePath = getLevelFilePath();

        return new JPacmanFeatureEnvironment(getGameEnvironmentFactory(), levelFilePath,
                numericalModelFactory);
    }
}
