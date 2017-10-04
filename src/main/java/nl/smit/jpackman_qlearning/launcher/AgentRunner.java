package nl.smit.jpackman_qlearning.launcher;

import lombok.AllArgsConstructor;
import nl.smit.jpackman_qlearning.agent.ReinforcementAgent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Runs the agent.
 *
 * @author Jordi Smit, 3-10-2017.
 */
public class AgentRunner {
    private static final int AMOUNT_OF_SILENT_TRAINING_MOVES = 1000;

    private final ReinforcementAgent agent;
    private boolean inProgress = false;
    private boolean isTrainingAtMaxSpeed = false;


    private final Object startLock = new Object();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    /**
     * The default constructor.
     * @param agent The agent that will be used.
     */
    public AgentRunner(ReinforcementAgent agent) {
        this.agent = agent;
    }

    /**
     * Starts the agent.
     */
    public void start() {
        synchronized (startLock) {
            if (inProgress) {
                return;
            }
            this.inProgress = true;
            this.service = Executors.newSingleThreadScheduledExecutor();
            service.schedule(new AgentTask(agent, service), 0, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stops the agent.
     */
    public void stop() {
        synchronized (startLock) {
            if (!inProgress) {
                return;
            }
            this.inProgress = false;
            this.service.shutdown();
        }
    }

    /**
     * Trains the agent at max speed.
     */
    public void trainingAtMaxSpeed() {
        boolean wasInProgress = false;
        boolean originalTrainingMode = agent.isLearning();

        if (inProgress) {
            stop();
            wasInProgress = true;
        }

        agent.setLearning(true);
        agent.preformMoves(AMOUNT_OF_SILENT_TRAINING_MOVES);
        agent.setLearning(originalTrainingMode);

        if (wasInProgress) {
            start();
        }
    }

    /**
     * Toggles the agents training mode between on and off.
     */
    public void toggleTrainingMode() {
        boolean currentMode = agent.isLearning();
        agent.setLearning(!currentMode);
    }

    @AllArgsConstructor
    private static class AgentTask implements Runnable {
        private static final long INTERVAL = 500;

        private final ReinforcementAgent agent;
        private final ScheduledExecutorService service;


        @Override
        public void run() {
            agent.preformMoves(1);
            service.schedule(this, INTERVAL, TimeUnit.MILLISECONDS);
        }
    }
}
