package nl.smit.jpackman_qlearning.environment.model.numerical;

import lombok.Getter;
import nl.smit.jpackman_qlearning.environment.percepts.JPacmanInitPercepts;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * A numerical model of the JPacmanStateEnvironment.
 *
 * @author Jordi Smit, 24-7-2017.
 */
public class NumericalModel extends AbstractNumericalModel {
    /**
     * Default values.
     */
    private static final int ROWS = 1;


    /**
     * Unchanging map information.
     */
    private final int mapHeight;
    private final int mapWidth;

    /**
     * Sub vectors of the state.
     */
    private final INDArray pelletVector;
    private final INDArray playerVector;


    @Getter
    private final INDArray currentState;

    /**
     * The default constructor.
     *
     * @param initPercepts The initpercepts that will be used to constructor the model.
     */
    public NumericalModel(JPacmanInitPercepts initPercepts) {
        this.mapHeight = initPercepts.getBoardHeight();
        this.mapWidth = initPercepts.getBoardWidth();

        this.playerVector = createDefaultPlayerVector();
        this.pelletVector = createDefaultPelletVector();

        this.currentState = createDefaultCurrentState();
    }


    private INDArray createDefaultPelletVector(
            @UnknownInitialization(Object.class)NumericalModel this) {
        int amountOfTiles = mapHeight * mapWidth;
        return Nd4j.zeros(ROWS, amountOfTiles);
    }


    private INDArray createDefaultPlayerVector(
            @UnknownInitialization(Object.class)NumericalModel this
    ) {
        int amountOfTiles = mapHeight * mapWidth;
        return Nd4j.zeros(ROWS, amountOfTiles);
    }

    @RequiresNonNull( {"pelletVector", "playerVector"})
    private INDArray createDefaultCurrentState(
            @UnknownInitialization(Object.class)NumericalModel this
    ) {
        int amountOfFeatures = pelletVector.columns() + playerVector.columns();
        return Nd4j.zeros(amountOfFeatures);
    }


    @Override
    protected void updateState() {
        updatePelletVector();
        updatePlayerVector();

        updateCurrentStateVector();
    }


    private void updateCurrentStateVector() {
        INDArray combinedFeatures = Nd4j.hstack(pelletVector, playerVector);

        assert combinedFeatures.columns() == currentState.columns()
                : "The amount of collum value is not allowed to change during runtime.";

        this.currentState.assign(combinedFeatures);
    }


    private void updatePelletVector() {
        INDArray newPelletVector = createDefaultPelletVector();

        for (Location location : getPelletLocations()) {
            int index = calcLocationIndex(location);
            newPelletVector.put(ROW_INDEX, index, NUMERICAL_TRUE);
        }

        this.pelletVector.assign(newPelletVector);
    }


    private int calcLocationIndex(Location location) {
        return location.getY() * mapWidth + location.getX();
    }

    private void updatePlayerVector() {
        INDArray newPlayerVector = createDefaultPlayerVector();

        Location playerLocation = getPlayerLocation();
        int playerLocationIndex = calcLocationIndex(playerLocation);
        newPlayerVector.put(ROW_INDEX, playerLocationIndex, NUMERICAL_TRUE);


        this.playerVector.assign(newPlayerVector);
    }


    @Override
    public int getAmountOfFeatures() {
        return this.currentState.columns();
    }
}
