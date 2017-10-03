package nl.smit.jpackman_qlearning.environment;


import nl.smit.jpackman_qlearning.interfaces.FiniteStateEnviroment;
import nl.smit.jpackman_qlearning.interfaces.State;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateModel;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateModelFactory;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.util.Collection;

/**
 * A {@link FiniteStateEnviroment} implementation of the {@link JPacmanEnvironment}.
 * This class can for example be used for a Q Table.
 *
 * @author Jordi Smit, 18-7-2017.
 */
public class JPacmanStateEnvironment extends JPacmanEnvironment<State>
        implements FiniteStateEnviroment {

    private final MazeStateModelFactory mazeStateModelFactory;
    /**
     * The model that transform the the enviroment into {@link State} representations.
     */
    @MonotonicNonNull
    private MazeStateModel model;


    /**
     * Main constructor.
     *
     * @param gameEnvironmentFactory A factory that creates new GameEnvironment.
     * @param levelFilePath          The path to the file that will be used to parse the level.
     * @param mazeStateModelFactory  A factory that is able to create a state model
     *                               of this environment.
     */
    public JPacmanStateEnvironment(GameEnvironmentFactory gameEnvironmentFactory,
                                   String levelFilePath,
                                   MazeStateModelFactory mazeStateModelFactory) {
        super(gameEnvironmentFactory, levelFilePath);

        this.mazeStateModelFactory = mazeStateModelFactory;
    }

    private MazeStateModel getModel() {
        if (model == null) {
            this.model = mazeStateModelFactory.createMazeStateModel(this);
        }
        return model;
    }

    @Override
    public State observeState() {
        return getModel().getCurrentState();
    }


    @Override
    public Collection<State> getAllStates() {
        return getModel().getAllStates();
    }
}
