package nl.smit.jpackman_qlearning.environment.model.numerical;

import lombok.AccessLevel;
import lombok.Getter;
import nl.smit.jpackman_qlearning.interfaces.Model;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment.GameEnvironmentObserver;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Direction;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.List;

/**
 * Defines the expected singature of all the implementation of the different NumericalModels.
 *
 * @author Jordi Smit, 24-7-2017.
 */
public abstract class AbstractNumericalModel implements GameEnvironmentObserver, Model<INDArray> {
    protected static final int ROW_INDEX = 0;
    protected static final int NUMERICAL_TRUE = 1;

    /**
     * The default single player index for the precepts.
     */
    private static final int PLAYER_INDEX = 0;

    /**
     * The pellet location based on the last given percept.
     */
    @Getter(AccessLevel.PROTECTED)
    @MonotonicNonNull
    private List<Location> pelletLocations;

    /**
     * The locations of the player based on the last given percept.
     */
    @Getter(AccessLevel.PROTECTED)
    @MonotonicNonNull
    private List<Location> playerLocations;

    @Getter(AccessLevel.PROTECTED)
    @MonotonicNonNull
    private List<Direction> movablePlayerDirection;


    /**
     * @return The amount of features that will be used by the model.
     */
    public abstract int getAmountOfFeatures();


    @Override
    public void update(JPacmanPercepts percepts) {
        updatePelletLocations(percepts);
        updatePlayerLocations(percepts);
        updateMovablePlayerDirection(percepts);

        updateState();
    }


    private void updatePelletLocations(JPacmanPercepts percepts) {
        this.pelletLocations = percepts.getPelletLocations();
    }


    private void updatePlayerLocations(JPacmanPercepts percepts) {
        this.playerLocations = percepts.getPlayerLocations();
    }

    private void updateMovablePlayerDirection(JPacmanPercepts percepts) {
        this.movablePlayerDirection = percepts.getMovablePlayerDirection();
    }

    /**
     * Updates the current state based ont the percept information.
     */
    protected abstract void updateState();

    /**
     * @return The location of the single player.
     */
    protected Location getPlayerLocation() {
        return getPlayerLocations().get(PLAYER_INDEX);
    }
}
