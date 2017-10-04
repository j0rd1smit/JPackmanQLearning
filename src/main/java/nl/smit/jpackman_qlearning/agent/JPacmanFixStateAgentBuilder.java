package nl.smit.jpackman_qlearning.agent;



import nl.smit.jpackman_qlearning.environment.JPacmanStateEnvironment;
import nl.smit.jpackman_qlearning.interfaces.Action;
import nl.smit.jpackman_qlearning.interfaces.State;

import java.util.Collection;

/**
 * Build {@link ReinforcementAgent} for the JPacmanEnvironment.
 *
 * @author Jordi Smit, 14-7-2017.
 */
public class JPacmanFixStateAgentBuilder {
    /**
     * The default init values.
     */
    private static final double LEARNING_RATE = 0.5;
    private static final double DISCOUNTED_FUTURE_REWARD_FACTOR = 0.9;

    /**
     * The build variable that will be used to build the {@link ReinforcementAgent}.
     */
    private double learningRate = LEARNING_RATE;
    private double discountedFutureRewardFactor = DISCOUNTED_FUTURE_REWARD_FACTOR;
    private JPacmanStateEnvironment environment;

    /**
     * Creates a new {@link JPacmanFixStateAgentBuilder}.
     * @param environment The JPacmanEnvironment for which the agent will be build.
     */
    public JPacmanFixStateAgentBuilder(JPacmanStateEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Tells the builder to use this learning rate.
     * @param learningRate The learning rate to be used.
     * @return The updated builder.
     */
    public JPacmanFixStateAgentBuilder useLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    /**
     * Tells the builder to use this discounted Future Reward Factor.
     * @param factor the discounted Future Reward Factor to be used.
     * @return The updated builder.
     */
    public JPacmanFixStateAgentBuilder useDiscountedFutureRewardFactor(double factor) {
        this.discountedFutureRewardFactor = factor;
        return this;
    }

    /**
     * Builds a {@link ReinforcementAgent} with q table a decision making logic.
     * @return A implemented agent.
     */
    public ReinforcementAgent buildQTableAgent() {
        ReinforcementLogic<State> logic = createQTable();

        return new ReinforcementAgent<State>(environment, logic);
    }

    private ReinforcementLogic<State> createQTable() {
        Collection<Action> actions = environment.getAllActions();
        Collection<State> states = environment.getAllStates();

        return new QTable(learningRate, discountedFutureRewardFactor, states, actions);
    }
}
