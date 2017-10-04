package nl.smit.jpackman_qlearning.launcher;


import nl.smit.jpackman_qlearning.agent.ReinforcementAgent;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.smit.jpackman_qlearning.ui.JPacmanEnvironmentUI;
import nl.smit.jpackman_qlearning.ui.JPacmanEnvironmentUIBuilder;

/**
 * Launches the GUI of a {@link JPacmanEnvironment}.
 *
 * @author Jordi Smit, 14-7-2017.
 */
public final class Launcher {


    /**
     * The environment that will be runned.
     */
    private final JPacmanEnvironment environment;

    private final AgentRunner agentRunner;
    private final ReinforcementAgent agent;


    private boolean hasBeenStarted = false;

    /**
     * Creates a new Launcher object.
     * @param environment The {@link JPacmanEnvironment} the will be used.
     */
    public Launcher(JPacmanEnvironment environment, ReinforcementAgent agent) {
        this.environment = environment;
        this.agent = agent;
        this.agentRunner = new AgentRunner(agent);
    }


    /**
     * Launches the GUI of for the given enviroment with and the given agent.
     */
    public void launch() {
        if (!hasBeenStarted) {
            JPacmanEnvironmentUI ui =  buildUI();

            ui.start();
            hasBeenStarted = true;
        }
    }

    private JPacmanEnvironmentUI buildUI() {
        JPacmanEnvironmentUIBuilder builder = new JPacmanEnvironmentUIBuilder()
                .addButton("Start", agentRunner::start)
                .addButton("Stop", agentRunner::stop)
                .addButton("train max speed", agentRunner::trainingAtMaxSpeed)
                .addButton("Toggle training", agentRunner::toggleTrainingMode);
        
        return builder.build(environment, agent);
    }




}
