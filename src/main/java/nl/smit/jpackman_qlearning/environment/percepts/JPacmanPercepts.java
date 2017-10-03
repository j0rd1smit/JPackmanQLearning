package nl.smit.jpackman_qlearning.environment.percepts;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.smit.jpackman_qlearning.agent.environment.Percepts;
import nl.tudelft.jpacman.board.Direction;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;

import java.util.List;

/**
 * A data container of the changing information of the {@link JPacmanEnvironment}.
 *
 * @author Jordi Smit, 9-7-2017.
 */
@Data
@RequiredArgsConstructor
public class JPacmanPercepts implements Percepts {
    /**
     * The pellet location coordinates on the board.
     */
    @NonNull
    private final List<Location> pelletLocations;

    /**
     * The pellet location coordinates on the board.
     */
    @NonNull
    private final List<Location> playerLocations;

    /**
     * The ghost location coordinates on the board.
     */
    @NonNull
    private final List<Location> ghostLocations;

    @NonNull
    private final List<Direction> movablePlayerDirection;

}
