package nl.smit.jpackman_qlearning.agent;



import nl.smit.jpackman_qlearning.interfaces.Action;
import nl.smit.jpackman_qlearning.interfaces.Environment;

import java.util.Random;

/**
 * A implementation of a reinforcement learning agent.
 * @param <StateClass> The class that is used to represent the states.
 *
 * @author Jordi Smit, 11-7-2017.
 */
public class ReinforcementAgent<StateClass> {
    private static final Random RANDOM = new Random();

    private static final double RANDOM_ACTION_CHANGES = 0.1;

    /**
     * The environment on which the agent is able to act.
     */
    private Environment<StateClass> environment;
    /**
     * The decision making logic of the agent.
     */
    private ReinforcementLogic<StateClass> reinforcementLogic;



    /**
     * Creates a new Reinforcement Agent.
     * @param environment The {@link Environment} on which the age will act
     * @param reinforcementLogic The implementation of the logic that agent should use.
     */
    public ReinforcementAgent(Environment<StateClass> environment,
                              ReinforcementLogic<StateClass> reinforcementLogic) {
        this.environment = environment;
        this.reinforcementLogic = reinforcementLogic;
    }

    private void preformQLearningCycle() {
        QTransition<StateClass> transition =  preformAndSaveTransition();

        reinforcementLogic.update(transition);
    }

    private QTransition<StateClass> preformAndSaveTransition() {


        StateClass startingState  = environment.observeState();
        Action selectedAction = selectAction(startingState);


        selectedAction.execute();
        double immediateReward = environment.observeReward();
        StateClass endState = environment.observeState();

        assert startingState != null;
        assert endState != null;

        return new QTransition<>(startingState, endState, selectedAction, immediateReward);
    }

    private Action selectAction(StateClass state) {
        if (RANDOM.nextDouble() < RANDOM_ACTION_CHANGES) {
            //System.out.print("random action: ");
            return reinforcementLogic.selectRandomAction();
        }

        return reinforcementLogic.selectAction(state);
    }

    /**
     * Performs an N amount of learning iterations,
     * which will be used to improve the agents decision making.
     * @param maxIterations The max amount of iterations.
     */
    public void learn(int maxIterations) {
        environment.start();

        for (int i = 0; i < maxIterations; i++) {
            preformQLearningCycle();

            if (!environment.isActive()) {
                restartEnvironment();
            }
        }

        environment.stop();
    }

    private void restartEnvironment() {
        environment.reset();
        environment.start();
    }

}
