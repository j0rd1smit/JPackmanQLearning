package nl.smit.jpackman_qlearning.environment.game;


import nl.smit.jpackman_qlearning.environment.helper.JPacmanFactory;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Creates {@link GameEnvironment} objects.
 *
 * @author Jordi Smit, 12-7-2017.
 */
public class GameEnvironmentFactory {

    /**
     * Creates a new {@link GameEnvironment} based on the given input file.
     *
     * @param filePath The location of the level representation file.
     * @return A new {@link GameEnvironment} object.
     */
    public @NonNull GameEnvironment createGameEnvironment(String filePath) {
        JPacmanFactory jPacmanFactory = new JPacmanFactory();
        Level level = jPacmanFactory.makeLevelFormFile(filePath);
        Player player = jPacmanFactory.createPlayer();

        return new GameEnvironment(player, level);
    }
}
