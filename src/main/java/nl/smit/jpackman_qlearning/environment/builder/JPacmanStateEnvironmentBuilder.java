package nl.smit.jpackman_qlearning.environment.builder;


import nl.smit.jpackman_qlearning.environment.JPacmanEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.JPacmanStateEnvironment;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateFactory;
import nl.smit.jpackman_qlearning.environment.model.state.MazeStateModelFactory;
import org.checkerframework.checker.initialization.qual.UnderInitialization;

/**
 * A builder class for {@link JPacmanStateEnvironment}.
 *
 * @author Jordi Smit, 19-7-2017.
 */
public class JPacmanStateEnvironmentBuilder
        extends AbstractJPacmanEnvironmentBuilder<JPacmanStateEnvironmentBuilder> {

    /**
     * The building variables and their default values.
     */
    private MazeStateModelFactory modelFactory = createDefaultMazeStateModelFactory();


    private MazeStateModelFactory createDefaultMazeStateModelFactory(
            @UnderInitialization(Object.class)JPacmanStateEnvironmentBuilder this) {

        MazeStateFactory mazeStateFactory = getMazeStateFactory();
        return new MazeStateModelFactory(mazeStateFactory);
    }

    private MazeStateFactory getMazeStateFactory(@UnderInitialization(Object.class)
                                                 JPacmanStateEnvironmentBuilder this) {
        return new MazeStateFactory();
    }

    /**
     * Tells the builder to use the provided model during the construction.
     *
     * @param modelFactory A factory that can create MazeStateModels.
     * @return The updated builder.
     */
    public JPacmanStateEnvironmentBuilder useModelFactory(MazeStateModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        return this;
    }


    /**
     * Build the JPacmanEnvironment.
     *
     * @return The newly created JPacmanEnvironment.
     */
    public JPacmanStateEnvironment build() {
        String levelFilePath = getLevelFilePath();
        JPacmanEnvironmentFactory jPacmanEnvironmentFactory = getJPacmanEnvironmentFactory();

        return jPacmanEnvironmentFactory.createJPacmanStateEnvironment(levelFilePath);
    }

    private JPacmanEnvironmentFactory getJPacmanEnvironmentFactory() {
        return new JPacmanEnvironmentFactory(getGameEnvironmentFactory(), modelFactory);
    }


}
