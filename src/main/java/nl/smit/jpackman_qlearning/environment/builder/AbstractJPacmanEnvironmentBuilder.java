package nl.smit.jpackman_qlearning.environment.builder;

import lombok.AccessLevel;
import lombok.Getter;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;


/**
 * The contains shared functionality between the different enviroment builders.
 *
 * @param <E> The type of the sub class.
 * @author Jordi Smit, 24-7-2017.
 */
public class AbstractJPacmanEnvironmentBuilder<E extends AbstractJPacmanEnvironmentBuilder> {
    /**
     * The default level file.
     */
    private static final String LEVEL_FILE = "/smallMaze.txt";

    /**
     * The building variables and their default values.
     */
    @Getter(AccessLevel.PROTECTED)
    private String levelFilePath = LEVEL_FILE;
    @Getter(AccessLevel.PROTECTED)
    private GameEnvironmentFactory gameEnvironmentFactory = new GameEnvironmentFactory();

    /**
     * Tells the builder to use the provided level File during the construction.
     *
     * @param levelFilePath The location of the level File.
     * @return the updated builder.
     */
    public E useLevelFile(String levelFilePath) {
        this.levelFilePath = levelFilePath;
        return (E) this;
    }

    /**
     * Tells the builder to use the provided factory during the construction.
     *
     * @param factory The factory to be used.
     * @return The updated builder.
     */
    public E useGameEnvironmentFactory(GameEnvironmentFactory factory) {
        this.gameEnvironmentFactory = factory;
        return (E) this;
    }


}
