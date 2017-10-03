package nl.smit.jpackman_qlearning.environment.percepts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.tudelft.jpacman.board.Direction;

/**
 * A container object with of 2D positions.
 *
 * @author Jordi Smit, 9-7-2017.
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class Location {
    /**
     * The x coordinates.
     */
    @Getter
    private final int x;
    /**
     * The y coordinates.
     */
    @Getter
    private final int y;


    /**
     * @param direction From this location.
     * @return The location in the given direction.
     */
    public Location locationTo(Direction direction) {
        return new Location(x + direction.getDeltaX(), y + direction.getDeltaY());
    }

    /**
     * @param other The other location.
     * @return the distance between to locations.
     */
    public double distance(Location other) {
        double dx = x - other.x;
        double dy = y - other.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
