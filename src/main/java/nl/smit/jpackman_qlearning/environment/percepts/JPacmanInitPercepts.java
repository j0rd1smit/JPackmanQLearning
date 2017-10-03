package nl.smit.jpackman_qlearning.environment.percepts;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;


/**
 * A data container of the init information of the {@link JPacmanEnvironment}.
 *
 * @author Jordi Smit, 9-7-2017.
 */
@Data
@AllArgsConstructor
public class JPacmanInitPercepts {
    /**
     * The width of the board.
     */
    private final int boardWidth;
    /**
     * The height of the board.
     */
    private final int boardHeight;

    /**
     * The starting location of the player.
     */
    private final Location startingLocation;
}
