package nl.smit.jpackman_qlearning.environment;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import nl.smit.jpackman_qlearning.agent.environment.Action;
import nl.smit.jpackman_qlearning.agent.environment.Environment;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironment.GameEnvironmentObserver;
import nl.smit.jpackman_qlearning.environment.game.GameEnvironmentFactory;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;
import nl.tudelft.jpacman.board.Direction;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The JPacman implementation of {@link Environment}.
 *
 * @param <StateClass> The class that is used to represent the states.
 * @author Jordi Smit, 11-7-2017.
 */
public abstract class JPacmanEnvironment<StateClass> implements Environment<StateClass> {
    private static final double REWARD_VALUE = 10;
    /**
     * The variables that will be used to create and recreate the {@link GameEnvironment}.
     */
    private final GameEnvironmentFactory gameEnvironmentFactory;
    private final String levelFilePath;
    /**
     * The available actions of this {@link Environment}.
     */
    private final List<Action> availableActions;
    /**
     * Observers of the enviroments game.
     */
    private List<GameEnvironmentObserver> gameObservers = new ArrayList<>();
    /**
     * The environment that can be influanced by the agent.
     */
    @Getter
    private GameEnvironment game;
    /**
     * The last known score of the player.
     */
    private double previousScore;


    /**
     * Creates a new {@link JPacmanEnvironment}.
     *
     * @param gameEnvironmentFactory A factory that creates new {@link GameEnvironment}s.
     * @param levelFilePath          The path to the file that will be used to parse the level.
     */
    public JPacmanEnvironment(GameEnvironmentFactory gameEnvironmentFactory,
                              String levelFilePath) {

        this.gameEnvironmentFactory = gameEnvironmentFactory;
        this.levelFilePath = levelFilePath;
        this.availableActions = createAvailableActions();

        setGameAndAddObserver();
    }

    @EnsuresNonNull("game")
    @RequiresNonNull( {"levelFilePath", "gameEnvironmentFactory"})
    private void setGameAndAddObserver(@UnknownInitialization(Object.class)
                                       JPacmanEnvironment<StateClass>this) {
        assert gameObservers != null;

        this.game = gameEnvironmentFactory.createGameEnvironment(levelFilePath);
        this.previousScore = game.getScore();

        for (GameEnvironmentObserver observer : gameObservers) {
            this.game.addObserver(observer);
        }
    }


    private List<Action> createAvailableActions(@UnknownInitialization(Object.class)
                                                JPacmanEnvironment<StateClass>this) {
        return Arrays.asList(
                this::moveNorth,
                this::moveEast,
                this::moveSouth,
                this::moveWest
        );
    }

    /*
    game will only be null untill setGameAndAddObserver has been called to the first time
    afterwards it will always be non null.
    these function are only referenced as lambda expresion in the constructor which but not called.
    Therefor are the dereference.of.nullable warning a false alarm.
    */
    @SuppressWarnings("dereference.of.nullable")
    private void moveNorth(@UnknownInitialization(Object.class)
                           JPacmanEnvironment<StateClass>this) {
        this.game.move(Direction.NORTH);
    }

    @SuppressWarnings("dereference.of.nullable")
    private void moveEast(@UnknownInitialization(Object.class)
                          JPacmanEnvironment<StateClass>this) {
        this.game.move(Direction.EAST);
    }

    @SuppressWarnings("dereference.of.nullable")
    private void moveSouth(@UnknownInitialization(Object.class)
                           JPacmanEnvironment<StateClass>this) {
        this.game.move(Direction.SOUTH);
    }

    @SuppressWarnings("dereference.of.nullable")
    private void moveWest(@UnknownInitialization(Object.class)
                          JPacmanEnvironment<StateClass>this) {
        this.game.move(Direction.WEST);
    }


    @Override
    public double observeReward() {
        return calcPeletEatBasedScore();
    }

    private double calcPeletEatBasedScore() {
        if (!getGame().isMadeValidMove()) {
            this.previousScore = game.getScore();
            return -REWARD_VALUE;
        }

        if (hasTheScoreIncreased()) {
            this.previousScore = game.getScore();
            return REWARD_VALUE;
        }

        return 0;
    }


    private boolean hasTheScoreIncreased() {
        return game.getScore() > previousScore;
    }

    @Override
    public boolean isActive() {
        return game.isInProgress();
    }

    @Override
    public void start() {
        game.start();
    }

    @Override
    public void stop() {
        game.stop();
    }

    @Override
    public void reset() {
        setGameAndAddObserver();
    }

    @Override
    public Collection<Action> getAllActions() {
        return ImmutableList.copyOf(availableActions);
    }


    /**
     * Add a new GameEnvironmentObserve.
     *
     * @param observer A GameEnvironmentObserver that wants to observer the enviroments game.
     */
    public void addGameObserver(GameEnvironmentObserver observer) {
        if (!isAlreadyAGameEnvironmentObserver(observer)) {
            this.gameObservers.add(observer);
            game.addObserver(observer);
        }
    }

    private boolean isAlreadyAGameEnvironmentObserver(GameEnvironmentObserver observer) {
        return gameObservers.contains(observer);
    }

    /**
     * @return The init percepts of the {@link Environment}.
     */
    public JPacmanInitPercepts getInitPercepts() {
        return getGame().getInitPercepts();
    }


    /**
     * A interface for the lambda expresion,
     * that the enviroment uses to create its {@link GameEnvironment}s.
     */
    @FunctionalInterface
    public interface GameMaker {
        /**
         * @return Creates a new {@link GameEnvironment}.
         */
        GameEnvironment create();
    }
}
