package nl.smit.jpackman_qlearning;

import nl.smit.jpackman_qlearning.environment.JPacmanStateEnvironment;
import nl.smit.jpackman_qlearning.environment.builder.JPacmanStateEnvironmentBuilder;
import nl.smit.jpackman_qlearning.launcher.Launcher;

/**
 * @author Jordi Smit, 3-10-2017.
 */
class Main {

    private static final String SMALL_MAZE = "/smallMaze.txt";
    private static final String HARD_MAZE = "/hardMaze.txt";

    private Main() {
    }

    /**
     * Launches the applications.
     * @param args takes no args.
     */
    public static void main(String[] args) {
        JPacmanStateEnvironment environmentBuilder = new JPacmanStateEnvironmentBuilder()
                .useLevelFile(HARD_MAZE)
                .build();

        new Launcher(environmentBuilder).launch();
    }
}
