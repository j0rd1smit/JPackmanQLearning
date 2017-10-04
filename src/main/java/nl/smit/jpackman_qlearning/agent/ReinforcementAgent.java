package nl.smit.jpackman_qlearning.agent;



import lombok.Getter;
import lombok.Setter;
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

    @Getter
    @Setter
    private boolean learning = true;
    @Getter
    private double randomActionChange = RANDOM_ACTION_CHANGES;
    @Getter
    private int totalMovesMade = 0;
    @Getter
    private int movesMadeSinceReset = 0;



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



    /**
     * Performs an N amount of learning iterations,
     * which will be used to improve the agents decision making.
     * @param maxIterations The max amount of iterations.
     */
    public void preformMoves(int maxIterations) {
        environment.start();

        for (int i = 0; i < maxIterations; i++) {

            if (learning) {
                preformQLearningCycle();
            }
            else {
                preformBestAction();
            }
            this.movesMadeSinceReset++;
            this.totalMovesMade++;

            if (!environment.isActive()) {
                restartEnvironment();
                this.movesMadeSinceReset = 0;
            }

        }

        environment.stop();
    }

    private void preformQLearningCycle() {
        QTransition<StateClass> transition =  preformAndSaveTransition();

        reinforcementLogic.update(transition);
    }

    private QTransition<StateClass> preformAndSaveTransition() {

        StateClass startingState  = environment.observeState();
        Action selectedAction = selectEpsGreedyAction(startingState);


        selectedAction.execute();
        double immediateReward = environment.observeReward();
        StateClass endState = environment.observeState();

        assert startingState != null;
        assert endState != null;

        return new QTransition<>(startingState, endState, selectedAction, immediateReward);
    }

    private Action selectEpsGreedyAction(StateClass state) {
        if (RANDOM.nextDouble() < randomActionChange) {
            return reinforcementLogic.selectRandomAction();
        }

        return reinforcementLogic.selectAction(state);
    }

    private void preformBestAction() {
        StateClass state = environment.observeState();
        Action action = reinforcementLogic.selectAction(state);
        action.execute();
    }

    private void restartEnvironment() {
        environment.reset();
        environment.start();
    }

}
