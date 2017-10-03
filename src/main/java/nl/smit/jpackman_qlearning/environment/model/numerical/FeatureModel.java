package nl.smit.jpackman_qlearning.environment.model.numerical;

import lombok.Getter;
import nl.smit.jpackman_qlearning.environment.percepts.Location;
import nl.tudelft.jpacman.board.Direction;
import org.checkerframework.checker.initialization.qual.UnderInitialization;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Implements a numerical feature model representation of the JPacmanEnvironment.
 *
 * @author Jordi Smit, 26-7-2017.
 */
public class FeatureModel extends AbstractNumericalModel {
    /**
     * Default values.
     */
    private static final int AMOUNT_OF_NEASREST_VALUES = 1;

    /**
     * Sub vectors of the state.
     */
    private final INDArray movablePlayerDirectionVector;
    private final INDArray nearestPelletDistancesVector;

    /**
     * The numerical represenation of the state.
     */
    @Getter
    private final INDArray currentState;

    /**
     * The default constructor of the FeatureModel.
     */
    public FeatureModel() {
        super();
        this.movablePlayerDirectionVector = createDefaultMovablePlayerDirectionVector();
        this.nearestPelletDistancesVector = createDefaultNearestPelletDistancesVector();

        this.currentState = createDefaultCurrentStateVector();
    }

    private INDArray createDefaultMovablePlayerDirectionVector(
            @UnknownInitialization(Object.class)FeatureModel this) {
        int amountOfDirections = Direction.values().length;
        return Nd4j.zeros(amountOfDirections);
    }

    private INDArray createDefaultNearestPelletDistancesVector(
            @UnknownInitialization(Object.class)FeatureModel this) {
        return Nd4j.zeros(AMOUNT_OF_NEASREST_VALUES);
    }

    @RequiresNonNull( {"movablePlayerDirectionVector", "nearestPelletDistancesVector"})
    private INDArray createDefaultCurrentStateVector(@UnderInitialization(Object.class)
                                                     FeatureModel this) {
        int amountOfFeatures = movablePlayerDirectionVector.columns()
                + nearestPelletDistancesVector.columns();

        return Nd4j.zeros(amountOfFeatures);
    }

    @Override
    public int getAmountOfFeatures() {
        return currentState.columns();
    }

    @Override
    protected void updateState() {
        updateMovablePlayerDirectionVector();
        updateNearestPelletDistancesVector();

        updateCurrentStateVector();
    }

    private void updateMovablePlayerDirectionVector() {
        INDArray newValues = createDefaultMovablePlayerDirectionVector();

        for (int i = 0; i < Direction.values().length; i++) {
            Direction direction = Direction.values()[i];

            if (playerCanMoveInDirection(direction)) {
                newValues.put(ROW_INDEX, i, NUMERICAL_TRUE);
            }
        }

        this.movablePlayerDirectionVector.assign(newValues);
    }

    private boolean playerCanMoveInDirection(Direction direction) {
        return getMovablePlayerDirection().contains(direction);
    }

    private void updateNearestPelletDistancesVector() {
        INDArray newValues = createDefaultNearestPelletDistancesVector();

        double nearestPelletDistances = calcNearestPelletDistances();
        newValues.put(ROW_INDEX, 0, nearestPelletDistances);


        this.nearestPelletDistancesVector.assign(newValues);
    }

    private double calcNearestPelletDistances() {
        if (getPelletLocations().isEmpty()) {
            return 0;
        }

        Location playerLocation = getPlayerLocation();
        double smallestDistance = Double.MAX_VALUE;

        for (Location pelletLocation : getPelletLocations()) {
            double distanceToPellet = playerLocation.distance(pelletLocation);

            if (distanceToPellet < smallestDistance) {
                smallestDistance = distanceToPellet;
            }
        }

        return smallestDistance;
    }


    private double calcNearestPelletDistancesX() {
        if (getPelletLocations().isEmpty()) {
            return 0;
        }

        Location playerLocation = getPlayerLocation();
        int smallestDistance = Integer.MAX_VALUE;

        for (Location pelletLocation : getPelletLocations()) {
            int distanceToPellet = Math.abs(pelletLocation.getX() - playerLocation.getX());

            if (distanceToPellet < smallestDistance) {
                smallestDistance = distanceToPellet;
            }
        }

        return smallestDistance;
    }

    private double calcNearestPelletDistancesY() {
        if (getPelletLocations().isEmpty()) {
            return 0;
        }

        Location playerLocation = getPlayerLocation();
        int smallestDistance = Integer.MAX_VALUE;

        for (Location pelletLocation : getPelletLocations()) {
            int distanceToPellet = Math.abs(pelletLocation.getY() - playerLocation.getY());

            if (distanceToPellet < smallestDistance) {
                smallestDistance = distanceToPellet;
            }
        }

        return smallestDistance;
    }


    private void updateCurrentStateVector() {
        INDArray combinedFeatures = Nd4j.hstack(movablePlayerDirectionVector,
                nearestPelletDistancesVector);

        assert combinedFeatures.columns() == currentState.columns()
                : "The amount of collum value is not allowed to change during runtime.";

        this.currentState.assign(combinedFeatures);
    }
}
