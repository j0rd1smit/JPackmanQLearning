package nl.smit.jpackman_qlearning.environment.game;


import com.google.common.collect.ImmutableList;
import lombok.Getter;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.SinglePlayerGame;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;

import java.util.ArrayList;
import java.util.List;


/**
 * A extension of {@link Game} that keeps track of changing attributes of the map.
 * That can be send as precepts.
 *
 * @author Jordi Smit, 8-7-2017.
 */
public class GameEnvironment extends SinglePlayerGame implements GameEnvironmentSubject {

    /**
     * The pellet location coordinates on the board.
     */
    private final List<Location> pelletLocations = new ArrayList<>();

    /**
     * The pellet location coordinates on the board.
     */
    private final List<Location> playerLocations = new ArrayList<>();

    /**
     * The ghost location coordinates on the board.
     */
    private final List<Location> ghostLocations = new ArrayList<>();

    /**
     * List with all the observers of this game.
     */
    @Getter
    private final List<GameEnvironmentObserver> observers = new ArrayList<>();

    @Getter
    private boolean madeValidMove = true;


    /**
     * Creates a new GameEnvironment object.
     *
     * @param player The player object that can be controlled.
     * @param level  The level that will be played.
     */
    public GameEnvironment(Player player, Level level) {
        super(player, level);

        addUnitLocations(level.getBoard());
    }

    /**
     * Loops through all the square,
     * all the encounterd units locations to their corrrect lists.
     *
     * @param board The board containing all the squares.
     */
    private void addUnitLocations(@UnknownInitialization(SinglePlayerGame.class)
                                  GameEnvironment this,
                                  Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Square square = board.squareAt(x, y);
                for (Unit unit : square.getOccupants()) {
                    Location location = new Location(x, y);
                    addUnitLocationToCorrectList(unit, location);
                }
            }
        }
        assert !playerLocations.isEmpty() : "There should alway be one player location";
    }

    /**
     * Adds the unit to the correct location list.
     *
     * @param unit     The unit to be added and sorted.
     * @param location The location of this unit.
     */
    private void addUnitLocationToCorrectList(
            @UnknownInitialization(SinglePlayerGame.class)GameEnvironment this,
            Unit unit, Location location) {
        if (unit instanceof Player) {
            playerLocations.add(location);
            return;
        }
        if (unit instanceof Pellet) {
            pelletLocations.add(location);
            return;
        }
        if (unit instanceof Ghost) {
            ghostLocations.add(location);
            return;
        }

        throw new RuntimeException("Unknown Unit type: " + unit);
    }

    /**
     * Moves the player and updates the map information.
     *
     * @param player    The player that will be moved.
     * @param direction The direction in which the player will be moved.
     */
    @Override
    public void move(Player player, Direction direction) {
        if (isInProgress()) {
            this.madeValidMove = player.getSquare().getSquareAt(direction).isAccessibleTo(player);
            getLevel().move(player, direction);
            updateUnitLocations();
            updateObservers();
            movePostCondition(player);
        }
    }

    /**
     * Clears old unit locations and updates them to the current ones.
     */
    private void updateUnitLocations() {
        clearUnitLocations();
        Board board = getBoard();
        addUnitLocations(board);
    }

    /**
     * @return The board attribute of the level.
     */
    private Board getBoard() {
        return getLevel().getBoard();
    }

    /**
     * Clears the locations attributes to empty list so they can be updated.
     */
    private void clearUnitLocations() {
        playerLocations.clear();
        pelletLocations.clear();
        ghostLocations.clear();
    }

    private void updateObservers() {
        for (GameEnvironmentObserver observer : observers) {
            observer.update(getPercepts());
        }
    }

    private void movePostCondition(Player player) {
        Square playerSquare = player.getSquare();
        boolean isCorrectSquare = false;
        for (Location location : playerLocations) {
            isCorrectSquare = getBoard().squareAt(location.getX(), location.getY())
                    .equals(playerSquare);
        }

        assert isCorrectSquare : "The player locations should contain the correct square location.";
    }

    /**
     * Moves the default player.
     *
     * @param direction The direction in which the player will be moved.
     */
    public void move(Direction direction) {
        move(getPlayer(), direction);
    }

    private Player getPlayer() {
        return this.getPlayers().get(0);
    }

    @Override
    public void addObserver(GameEnvironmentObserver observer) {
        if (!isAlreadyAObserver(observer)) {
            this.observers.add(observer);
            observer.update(getPercepts());
        }
    }

    private boolean isAlreadyAObserver(GameEnvironmentObserver observer) {
        return observers.contains(observer);
    }

    /**
     * @return The precepts of the current state of the {@link GameEnvironment}.
     */
    public JPacmanPercepts getPercepts() {
        List<Location> pellets = ImmutableList.copyOf(pelletLocations);
        List<Location> players = ImmutableList.copyOf(playerLocations);
        List<Location> ghosts = ImmutableList.copyOf(ghostLocations);
        List<Direction> movablePlayerDirection = findMovablePlayerDirection();


        return new JPacmanPercepts(pellets, players, ghosts, movablePlayerDirection);
    }


    private List<Direction> findMovablePlayerDirection() {
        List<Direction> movablePlayerDirection = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (playerCanMoveInDirection(direction)) {
                movablePlayerDirection.add(direction);
            }
        }

        return movablePlayerDirection;
    }

    private boolean playerCanMoveInDirection(Direction direction) {
        Location playerLocation = playerLocations.get(0);
        Square playerSquare = getBoard().squareAt(playerLocation.getX(), playerLocation.getY());

        return playerSquare.getSquareAt(direction).isAccessibleTo(getPlayer());
    }


    /**
     * @return The score of the player.
     */
    public double getScore() {
        return getPlayers().get(0).getScore();
    }

    /**
     * @return The init percepts
     */
    public JPacmanInitPercepts getInitPercepts() {
        Location playerLocation = playerLocations.get(0);
        int boardWidth = getBoard().getWidth();
        int boardHeight = getBoard().getHeight();

        return new JPacmanInitPercepts(boardWidth, boardHeight, playerLocation);
    }


    /**
     * The required functionality to observe the {@link GameEnvironment}.
     */
    public interface GameEnvironmentObserver {
        /**
         * Updates the observer with the new percepts.
         *
         * @param percepts The new percepts.s
         */
        void update(JPacmanPercepts percepts);
    }

}
