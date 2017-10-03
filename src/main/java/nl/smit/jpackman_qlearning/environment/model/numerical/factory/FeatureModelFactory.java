package nl.smit.jpackman_qlearning.environment.model.numerical.factory;


import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.smit.jpackman_qlearning.environment.model.numerical.AbstractNumericalModel;
import nl.smit.jpackman_qlearning.environment.model.numerical.FeatureModel;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Implements the AbstractNumericalModelFactory by creating a FeatureModel.
 *
 * @author Jordi Smit, 26-7-2017.
 */
public class FeatureModelFactory extends AbstractNumericalModelFactory {
    @Override
    public AbstractNumericalModel create(JPacmanEnvironment<INDArray> environment) {

        AbstractNumericalModel model = new FeatureModel();

        environment.addGameObserver(model);

        return model;
    }
}
