package nl.smit.jpackman_qlearning.environment.game;

import nl.smit.jpackman_qlearning.environment.helper.JPacmanFactory;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Test the precept creation by {@link GameEnvironment}.
 *
 * @author Jordi Smit, 11-7-2017.
 */
class GameEnvironmentTest {

    /**
     * The location of smaller testing environment.
     */
    private static final String TEST_FILE_NAME = "/environmentLauncherTest.txt";
    /**
     * The assumed starting locations of on the map.
     */
    private static final Location PLAYER = new Location(2, 2);
    private static final Location PELLET_LOCATION_1 = new Location(1, 2);
    private static final Location PELLET_LOCATION_2 = new Location(2, 3);
    private static final Location GHOST_LOCATION = new Location(21, 1);
    private static final List<Location> PELLET_LOCATIONS =
            Arrays.asList(PELLET_LOCATION_1, PELLET_LOCATION_2);

    /**
     * The board properties.
     */
    private static final int HEIGHT = 5;
    private static final int WIDTH = 23;

    /**
     * The index of the active player.
     */
    private static final int SINGLE_PLAYER_INDEX = 0;

    /**
     * Depandancies that are to complex to mock.
     * They will be created using the {@link JPacmanFactory}.
     */
    private Player player;


    /**
     * The object under testing.
     */
    private GameEnvironment gameEnvironment;

    /**
     * Sets up the testing variables.
     */
    @BeforeEach
    void setUpGameEnvironmentTest() {
        JPacmanFactory jPacmanFactory = new JPacmanFactory();
        Level level = jPacmanFactory.makeLevelFormFile(TEST_FILE_NAME);
        player = jPacmanFactory.createPlayer();
        gameEnvironment = new GameEnvironment(player, level);
        gameEnvironment.start();
    }

    /**
     * Test if the correct player location precept has been send,
     * after move into a specific direction.
     * @param direction The direction in which has moved.
     */
    @ParameterizedTest(name = "get precept after moving in direction={0}")
    @CsvSource({
            "NORTH",
            "EAST",
            "SOUTH",
            "WEST"
    })
    void playerPreceptLocation(Direction direction) {
        Location startLocation = gameEnvironment.getPercepts()
                .getPlayerLocations().get(SINGLE_PLAYER_INDEX);
        Location expected = startLocation.locationTo(direction);


        gameEnvironment.move(player, direction);
        Location result = gameEnvironment.getPercepts()
                .getPlayerLocations().get(SINGLE_PLAYER_INDEX);

        assertThat(result).isEqualTo(expected);
    }


    /**
     * Test if the precepts return the correct pellet locations.
     */
    @Test
    void pelletLocationPercept() {
        List<Location> result = gameEnvironment.getPercepts().getPelletLocations();

        assertThat(result).containsExactly(PELLET_LOCATION_1, PELLET_LOCATION_2);
    }


    /**
     * Test if the precepts return the correct ghost location.
     */
    @Test
    void ghostLocationPercept() {
        preventGhostFromMovingAway();

        List<Location> result = gameEnvironment.getPercepts().getGhostLocations();

        assertThat(result).containsExactly(GHOST_LOCATION);
    }

    private void preventGhostFromMovingAway() {
        gameEnvironment.stop();
    }

    /**
     * Test if the correct pellet locations will be removed after eating them.
     * @param direction The direction in which the player has to move.
     * @param locationIndex The index of the pellet that will be eaten.
     */
    @ParameterizedTest(name = "eat pellet in direction={0}")
    @CsvSource({
            "SOUTH, 0",
            "WEST, 1"
    })
    void pelletLocationPreceptAfterRemove(Direction direction, int locationIndex) {
        Location expected = PELLET_LOCATIONS.get(locationIndex);

        gameEnvironment.move(player, direction);
        List<Location> result = gameEnvironment.getPercepts().getPelletLocations();

        assertThat(result).containsExactly(expected);
    }


    /**
     * Test if the getMovablePlayerDirection return the correct value based on the current state.
     */
    @Test
    void movablePlayerDirectionPercepts() {

        List<Direction> result = gameEnvironment.getPercepts().getMovablePlayerDirection();

        assertThat(result).containsExactly(Direction.values());
    }

    /**
     * When a player is next to a wall then the direction toward,
     * that wall should not be in the movable directions.
     */
    @Test
    void movablePlayerDirectionPerceptsAfterMovingToTheWestWall() {
        gameEnvironment.move(Direction.WEST);

        List<Direction> result = gameEnvironment.getPercepts().getMovablePlayerDirection();

        assertThat(result).containsExactly(Direction.NORTH, Direction.SOUTH, Direction.EAST);
    }

    /**
     * Test the init percepts for the player starting location.
     */
    @Test
    void initPerceptsPlayerLocation() {
        Location result = gameEnvironment.getInitPercepts().getStartingLocation();

        assertThat(result).isEqualTo(PLAYER);
    }

    /**
     * Test the init percepts for the player board height.
     */
    @Test
    void initPerceptsPlayerHeight() {
        int result = gameEnvironment.getInitPercepts().getBoardHeight();

        assertThat(result).isEqualTo(HEIGHT);
    }

    /**
     * Test the init percepts for the player board width.
     */
    @Test
    void initPerceptsPlayerWidth() {
        int result = gameEnvironment.getInitPercepts().getBoardWidth();

        assertThat(result).isEqualTo(WIDTH);
    }
}
