package nl.smit.jpackman_qlearning.environment.model.numerical.factory;


import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.smit.jpackman_qlearning.environment.model.numerical.AbstractNumericalModel;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Defines the expected shape of the NumericalModelFactories.
 *
 * @author Jordi Smit, 26-7-2017.
 */
public abstract class AbstractNumericalModelFactory {

    /**
     * @param environment The Environment that will be modeled.
     * @return A newly created NumericalModel.
     */
    public abstract AbstractNumericalModel create(JPacmanEnvironment<INDArray> environment);
}
