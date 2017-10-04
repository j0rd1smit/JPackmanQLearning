package nl.smit.jpackman_qlearning.ui;


import nl.smit.jpackman_qlearning.agent.ReinforcementAgent;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.tudelft.jpacman.ui.Action;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Builder for {@link JPacmanEnvironmentUI}.
 *
 * @author Jordi Smit, 14-7-2017.
 */
public class JPacmanEnvironmentUIBuilder {

    /**
     * Map of buttons and their actions.
     */
    private final Map<String, Action> buttons;

    /**
     * Creates a new instance of the this builder.
     */
    public JPacmanEnvironmentUIBuilder() {
        buttons = new LinkedHashMap<>();
    }


    /**
     * @param environment The {@link JPacmanEnvironment} that will be used by the GUI.
     * @return A new instance of the {@link JPacmanEnvironmentUI}.
     */
    public JPacmanEnvironmentUI build(final JPacmanEnvironment environment, ReinforcementAgent agent) {
        assert environment != null;

        return new JPacmanEnvironmentUI(environment, buttons, agent);
    }

    /**
     * Adds a button to the UI.
     *
     * @param caption The caption of the button.
     * @param action  The action to execute when the button is clicked.
     * @return The builder.
     */
    public JPacmanEnvironmentUIBuilder addButton(String caption, Action action) {
        assert caption != null;
        assert !caption.isEmpty();
        assert action != null;

        buttons.put(caption, action);

        return this;
    }
}
