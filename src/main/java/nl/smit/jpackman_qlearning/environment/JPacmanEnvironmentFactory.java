package nl.smit.jpackman_qlearning.environment;


import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateModelFactory;

/**
 * A factory class for the different implementations of the {@link JPacmanEnvironment}.
 *
 * @author Jordi Smit, 19-7-2017.
 */
public class JPacmanEnvironmentFactory {
    /**
     * A factory the creates the GameEnvironments.
     */
    private final GameEnvironmentFactory gameEnvironmentFactory;

    /**
     * A factory the creates the MazeStateModel.
     */
    private final MazeStateModelFactory modelFactory;

    /**
     * Creates a new GameEnvironmentFactory object.
     *
     * @param gameEnvironmentFactory A factory the creates the GameEnvironments.
     * @param modelFactory           A MazeStateModelFactory.
     */
    public JPacmanEnvironmentFactory(GameEnvironmentFactory gameEnvironmentFactory,
                                     MazeStateModelFactory modelFactory) {
        this.gameEnvironmentFactory = gameEnvironmentFactory;
        this.modelFactory = modelFactory;
    }

    /**
     * @param levelFilePath The path to the file that will be used to parse the level.
     * @return A new JPacmanStateEnvironment object.
     */
    public JPacmanStateEnvironment createJPacmanStateEnvironment(String levelFilePath) {

        return new JPacmanStateEnvironment(gameEnvironmentFactory, levelFilePath, modelFactory);
    }
}
