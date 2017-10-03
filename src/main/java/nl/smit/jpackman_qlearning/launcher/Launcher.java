package nl.smit.jpackman_qlearning.launcher;


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


    private boolean hasBeenStarted = false;

    /**
     * Creates a new Launcher object.
     * @param environment The {@link JPacmanEnvironment} the will be used.
     */
    public Launcher(JPacmanEnvironment environment) {
        this.environment = environment;
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
        JPacmanEnvironmentUIBuilder builder = new JPacmanEnvironmentUIBuilder();
        
        return builder.build(environment);
    }


}
