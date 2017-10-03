package nl.smit.jpackman_qlearning.ui;

import nl.tudelft.jpacman.ui.Action;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Map;

/**
 * The button panel of the GUI.
 * Based on the Jpacman framework https://github.com/SERG-Delft/jpacman-framework.
 *
 * @author Jordi Smit, 14-7-2017.
 */

class ButtonPanel extends JPanel {
    /**
     * Default serialisation ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new button panel with a button for every action.
     *
     * @param buttons The map of caption - action for each button.
     * @param parent  The parent frame, used to return window focus.
     */
    ButtonPanel(final Map<String, Action> buttons, final JFrame parent) {
        super();
        assert buttons != null;
        assert parent != null;

        for (final String caption : buttons.keySet()) {
            JButton button = new JButton(caption);
            button.addActionListener(e -> {
                buttons.get(caption).doAction();
                parent.requestFocusInWindow();
            });
            add(button);
        }
    }
}
