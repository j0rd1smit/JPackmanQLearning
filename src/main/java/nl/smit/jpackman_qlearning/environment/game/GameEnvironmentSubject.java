package nl.smit.jpackman_qlearning.environment.game;


import nl.smit.jpackman_qlearning.environment.game.GameEnvironment.GameEnvironmentObserver;

/**
 * Specifies the required functionality for a {@link GameEnvironmentSubject}.
 *
 * @author Jordi Smit, 16-7-2017.
 */
public interface GameEnvironmentSubject {

    /**
     * Adds a new observer to the game.
     *
     * @param observer The object that wants to observer this object.
     */
    void addObserver(GameEnvironmentObserver observer);
}
