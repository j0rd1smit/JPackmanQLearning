package nl.smit.jpackman_qlearning.environment.helper;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;

import java.io.IOException;

/**
 * A factory that creates JPacmam objects.
 *
 * @author Jordi Smit, 11-7-2017.
 */
public class JPacmanFactory {

    private static final PacManSprites SPRITE_STORE = new PacManSprites();


    /**
     * Creates a new {@link Level} from file.
     *
     * @param path The path to level text file.
     * @return A new level object.
     */
    public Level makeLevelFormFile(String path) {
        try {
            return getMapParser().parseMap(path);
        } catch (IOException e) {
            throw new PacmanConfigurationException(
                    "Unable to create level, name = " + path, e);
        }
    }

    /**
     * Create a new {@link Player}.
     *
     * @return a new {@link Player}.
     */
    public Player createPlayer() {
        return getPlayerFactory().createPacMan();
    }


    /**
     * @return A new map parser object using the factories from
     * {@link #getLevelFactory()} and {@link #getBoardFactory()}.
     */
    private MapParser getMapParser() {
        return new MapParser(getLevelFactory(), getBoardFactory());
    }

    /**
     * @return A new board factory using the sprite store from
     * {@link #getSpriteStore()}.
     */
    private BoardFactory getBoardFactory() {
        return new BoardFactory(getSpriteStore());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}
     * and the ghosts from {@link #getGhostFactory()}.
     */
    private LevelFactory getLevelFactory() {
        return new LevelFactory(getSpriteStore(), getGhostFactory());
    }

    /**
     * @return The default {@link PacManSprites}.
     */
    private PacManSprites getSpriteStore() {
        return SPRITE_STORE;
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    private GhostFactory getGhostFactory() {
        return new GhostFactory(getSpriteStore());
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    private PlayerFactory getPlayerFactory() {
        return new PlayerFactory(getSpriteStore());
    }


}
