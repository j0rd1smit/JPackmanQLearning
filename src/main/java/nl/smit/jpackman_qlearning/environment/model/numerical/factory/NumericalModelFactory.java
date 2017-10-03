package nl.smit.jpackman_qlearning.environment.model.numerical.factory;



import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.smit.jpackman_qlearning.environment.model.numerical.AbstractNumericalModel;
import nl.smit.jpackman_qlearning.environment.model.numerical.NumericalModel;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;
import org.nd4j.linalg.api.ndarray.INDArray;


/**
 * A factory class for the {@link NumericalModel}.
 *
 * @author Jordi Smit, 25-7-2017.
 */
public class NumericalModelFactory extends AbstractNumericalModelFactory {

    /**
     * @param environment The Environment that will be modeled.
     * @return A newly created {@link NumericalModel}.
     */
    public AbstractNumericalModel create(JPacmanEnvironment<INDArray> environment) {
        JPacmanInitPercepts initPercepts = environment.getInitPercepts();
        NumericalModel model = new NumericalModel(initPercepts);

        environment.addGameObserver(model);

        return model;
    }


}
