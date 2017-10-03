package nl.smit.jpackman_qlearning.agent;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import nl.smit.jpackman_qlearning.interfaces.Action;

/**
 * A data object class that hold all the data form a observed Q transition.
 * @param <StateClass> The class that is used to represent the states.
 *
 * @author Jordi Smit, 17-7-2017.
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class QTransition<StateClass> {

    /**
     * The statring state of the transition.
     */
    @Getter
    @NonNull
    private final StateClass startingState;

    /**
     * The end state of the transition.
     */
    @Getter
    @NonNull
    private final StateClass endState;

    /**
     * The action that was preformed during the transition.
     */
    @Getter
    @NonNull
    private final Action preformedAction;

    /**
     * The observed reward in the end state.
     */
    @Getter
    private final double observedReward;

}
