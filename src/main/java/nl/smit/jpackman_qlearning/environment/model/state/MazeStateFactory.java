package nl.smit.jpackman_qlearning.environment.model.state;


import nl.smit.jpackman_qlearning.interfaces.State;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Direction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static nl.tudelft.jpacman.board.Direction.EAST;
import static nl.tudelft.jpacman.board.Direction.NORTH;
import static nl.tudelft.jpacman.board.Direction.SOUTH;
import static nl.tudelft.jpacman.board.Direction.WEST;

/**
 * A factory to create {@link MazeState}s objects.
 *
 * @author Jordi Smit, 9-7-2017.
 */
public class MazeStateFactory {

    /**
     * Creates {@link Collection} of {@link State}s that represents a board,
     * with the given height and width.
     *
     * @param width  The width of the board.
     * @param height The height of the board.
     * @return The {@link Collection} of states.
     */
    public Collection<State> createBoardRepresentation(final int width, final int height) {
        HashSet<State> states = new HashSet<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Location location = createLocation(x, y);
                addAllPossibleLocationsStates(location, states);
            }
        }

        return states;
    }

    private void addAllPossibleLocationsStates(final Location location, Set<State> stateSet) {
        stateSet.add(new MazeState(location));
        stateSet.add(new MazeState(location, NORTH, EAST, SOUTH, WEST));

        allCominationsWith3pellets(location, stateSet);
        allCombinationsWith2Pellets(location, stateSet);
        allCombinationsWith1Pellet(location, stateSet);
    }

    private void allCominationsWith3pellets(final Location location, Set<State> stateSet) {
        stateSet.add(new MazeState(location, EAST, SOUTH, WEST));
        stateSet.add(new MazeState(location, NORTH, SOUTH, WEST));
        stateSet.add(new MazeState(location, NORTH, EAST, WEST));
        stateSet.add(new MazeState(location, NORTH, EAST, SOUTH));
    }

    private void allCombinationsWith2Pellets(final Location location, Set<State> stateSet) {
        stateSet.add(new MazeState(location, NORTH, WEST));
        stateSet.add(new MazeState(location, NORTH, EAST));
        stateSet.add(new MazeState(location, EAST, SOUTH));
        stateSet.add(new MazeState(location, SOUTH, WEST));
        stateSet.add(new MazeState(location, EAST, WEST));
        stateSet.add(new MazeState(location, NORTH, SOUTH));
    }

    private void allCombinationsWith1Pellet(final Location location, Set<State> stateSet) {
        stateSet.add(new MazeState(location, NORTH));
        stateSet.add(new MazeState(location, EAST));
        stateSet.add(new MazeState(location, SOUTH));
        stateSet.add(new MazeState(location, WEST));
    }

    /**
     * Creates a new {@link MazeState}.
     *
     * @param location          The location in the maze.
     * @param surroundingPellet A collection that contains the directions,
     *                          in which the locations is serounded by pellets.
     * @return a new {@link MazeState}.
     */
    public MazeState createState(final Location location, Collection<Direction> surroundingPellet) {
        return new MazeState(location, surroundingPellet);
    }


    private Location createLocation(int x, int y) {
        return new Location(x, y);
    }
}
