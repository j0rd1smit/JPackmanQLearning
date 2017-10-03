package nl.smit.jpackman_qlearning.interfaces;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * A interface for environment that have a will use models that represent it based on features.
 *
 * @author Jordi Smit, 24-7-2017.
 */
public interface FeatureBasedEnvironment extends Environment<INDArray> {

    /**
     *
     * @return The amount of features that will be used to represent a state.
     */
    int getAmountStateFeatures();
}
