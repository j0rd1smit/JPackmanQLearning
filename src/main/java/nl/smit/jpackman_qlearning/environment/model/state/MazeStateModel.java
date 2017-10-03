package nl.smit.jpackman_qlearning.environment.model.state;

import lombok.Getter;

import nl.smit.jpackman_qlearning.agent.environment.Model;
import nl.smit.jpackman_qlearning.agent.environment.State;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment.GameEnvironmentObserver;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.tudelft.jpacman.board.Direction;
import org.checkerframework.checker.initialization.qual.UnderInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Implements a Model that find the state of the {@link GameEnvironment} it is observing.
 *
 * @author Jordi Smit, 9-7-2017.
 */
public class MazeStateModel implements GameEnvironmentObserver, Model<State> {
    /**
     * The default single player index for the precepts.
     */
    private static final int PLAYER_INDEX = 0;

    /**
     * Factory used to create new states and locations.
     */
    private final MazeStateFactory mazeStateFactory;

    /**
     * The pellet location based on the last given percept.
     */
    @MonotonicNonNull
    private List<Location> pelletLocations;

    /**
     * The locations of the player based on the last given percept.
     */
    @MonotonicNonNull
    private List<Location> playerLocations;


    @Getter
    @MonotonicNonNull
    private State currentState;

    private Collection<State> allPossibleStates;


    /**
     * Creates a new {@link MazeStateModel} based on the init percepts.
     *
     * @param mazeStateFactory A factory that can created new states.
     * @param initPercepts     The initpercept that will be used to created the model.
     */
    public MazeStateModel(MazeStateFactory mazeStateFactory, JPacmanInitPercepts initPercepts) {
        assert mazeStateFactory != null;
        this.mazeStateFactory = mazeStateFactory;

        this.allPossibleStates = this.createAllPossibleStatesCollection(initPercepts);
    }

    /**
     * Init the model based on the init percepts.
     *
     * @param initPercepts The init percepts of the environment.
     */

    private Collection<State> createAllPossibleStatesCollection(
            @UnderInitialization(Object.class)MazeStateModel this,
            JPacmanInitPercepts initPercepts) {

        assert mazeStateFactory != null;

        int boardWidth = initPercepts.getBoardWidth();
        int boardHeight = initPercepts.getBoardHeight();

        return mazeStateFactory.createBoardRepresentation(boardWidth, boardHeight);
    }

    /**
     * @return All possible states of the model.
     */
    public Collection<State> getAllStates() {
        return allPossibleStates;
    }

    /**
     * Updates the {@link MazeStateModel#getCurrentState()}.
     *
     * @param precepts The precepts about the current state of the {@link JPacmanEnvironment}.
     */
    public void update(JPacmanPercepts precepts) {
        updatePreceptInstanceVariables(precepts);

        updateState();
    }

    @EnsuresNonNull( {"playerLocations", "pelletLocations"})
    private void updatePreceptInstanceVariables(JPacmanPercepts precepts) {
        this.playerLocations = precepts.getPlayerLocations();
        this.pelletLocations = precepts.getPelletLocations();
    }

    @EnsuresNonNull("currentState")
    @RequiresNonNull( {"pelletLocations", "playerLocations"})
    private void updateState(MazeStateModel this) {
        Collection<Direction> surroundingPellets = findSurroundingPelletsPlayer();
        Location playerLocation = getPlayerLocation();

        this.currentState = mazeStateFactory.createState(playerLocation, surroundingPellets);
    }

    @RequiresNonNull( {"pelletLocations", "playerLocations"})
    private Collection<Direction> findSurroundingPelletsPlayer() {
        List<Direction> surroundingPellets = new LinkedList<>();

        for (Direction direction : Direction.values()) {
            if (playerSeesPelletIn(direction)) {
                surroundingPellets.add(direction);
            }
        }

        return surroundingPellets;
    }

    @RequiresNonNull( {"pelletLocations", "playerLocations"})
    private boolean playerSeesPelletIn(Direction direction) {
        Location playerLocation = getPlayerLocation();
        return pelletLocations.contains(playerLocation.locationTo(direction));
    }

    @RequiresNonNull("playerLocations")
    private Location getPlayerLocation() {
        return playerLocations.get(PLAYER_INDEX);
    }


}
