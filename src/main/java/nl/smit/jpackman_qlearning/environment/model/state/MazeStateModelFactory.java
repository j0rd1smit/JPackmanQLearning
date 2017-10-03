package nl.smit.jpackman_qlearning.environment.model.state;


import nl.smit.jpackman_qlearning.interfaces.State;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;

/**
 * A factory method that can created different type of models.
 *
 * @author Jordi Smit, 19-7-2017.
 */
public class MazeStateModelFactory {
    /**
     * A factory that can created new {@link MazeState}s.
     */
    private final MazeStateFactory mazeStateFactory;

    /**
     * The default constructor.
     *
     * @param mazeStateFactory A factory that can created new {@link MazeState}s.
     */
    public MazeStateModelFactory(MazeStateFactory mazeStateFactory) {
        this.mazeStateFactory = mazeStateFactory;
    }

    /**
     * Creates a new model based on the enviroment.
     * Also sets up the observer relationship between the enviroment and the model.
     *
     * @param environment The environment for which the model will be created.
     * @return a new {@link MazeStateModel}.
     */
    public MazeStateModel createMazeStateModel(JPacmanEnvironment<State> environment) {
        JPacmanInitPercepts initPercepts = environment.getInitPercepts();
        MazeStateModel mazeStateModel = new MazeStateModel(mazeStateFactory, initPercepts);

        environment.addGameObserver(mazeStateModel);

        return mazeStateModel;
    }
}
