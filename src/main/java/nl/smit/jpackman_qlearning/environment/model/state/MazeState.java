package nl.smit.jpackman_qlearning.environment.model.state;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import nl.smit.jpackman_qlearning.agent.environment.State;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Direction;

import java.util.Arrays;
import java.util.Collection;

/**
 * A {@link State} representation of a location in the JPacman maze.
 *
 * @author Jordi Smit, 9-7-2017.
 */
@EqualsAndHashCode
@ToString
public class MazeState implements State {
    /**
     * The location of the square.
     */
    @NonNull
    @Getter
    private final Location squareLocation;

    /**
     * List containing all the direction in wich a pellet lays on the next location.
     */
    @NonNull
    private final Collection<Direction> surroundingPellets;

    /**
     * Default constructor for the {@link MazeState}.
     *
     * @param squareLocation     The location of this maze state.
     * @param surroundingPellets A Collection containing all the direction,
     *                           in which a pellet lays on the next location.
     */
    public MazeState(Location squareLocation, Collection<Direction> surroundingPellets) {
        this.squareLocation = squareLocation;
        this.surroundingPellets = surroundingPellets;
    }

    /**
     * A Simpler constructor for the {@link MazeState}.
     *
     * @param squareLocation     The location of this maze state.
     * @param surroundingPellets A Array containing all the direction,
     *                           in which a pellet lays on the next location.
     */
    public MazeState(Location squareLocation, Direction... surroundingPellets) {
        this(squareLocation, Arrays.asList(surroundingPellets));
    }


    /**
     * @param direction The direction in wich a pellet lays on the next location.
     * @return True if the location has a pellet in one a specific direction.
     */
    public boolean hasPelletIn(Direction direction) {
        return surroundingPellets.contains(direction);
    }


}
