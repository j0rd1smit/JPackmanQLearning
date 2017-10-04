package nl.smit.jpackman_qlearning.ui;



import nl.smit.jpackman_qlearning.agent.ReinforcementAgent;
import nl.smit.jpackman_qlearning.environment.JPacmanEnvironment;
import nl.tudelft.jpacman.ui.Action;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The swing version of the {@link JPacmanEnvironment} GUI.
 * Based on the Jpacman framework https://github.com/SERG-Delft/jpacman-framework.
 *
 * @author Jordi Smit, 14-7-2017.
 */
public class JPacmanEnvironmentUI extends JFrame {

    /**
     * Default serialisation UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The desired frame rate interval for the graphics in milliseconds, 40
     * being 25 fps.
     */
    private static final int FRAME_INTERVAL = 40;

    /**
     * The panel displaying the game.
     */
    private final EnvironmentPanel environmentPanel;

    private final InfoPanel infoPanel;

    /**
     * Creates a new {@link JPacmanEnvironmentUI}.
     *
     * @param environment The {@link JPacmanEnvironment} to be displayed.
     * @param buttons     A map containing the name and their corresponding actions.
     */
    @SuppressWarnings("initialization")
    // requestFocusInWindow called before initialization ends
    JPacmanEnvironmentUI(final JPacmanEnvironment environment, Map<String, Action> buttons
            , ReinforcementAgent agent) {

        super("JPac-man Environment");
        assert environment != null;
        assert buttons != null;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel buttonPanel = new ButtonPanel(buttons, this);

        environmentPanel = new EnvironmentPanel(environment);
        infoPanel = new InfoPanel(agent);

        Container contentPanel = getContentPane();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPanel.add(environmentPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.NORTH);

        pack();
    }

    /**
     * Starts the "engine", the thread that redraws the interface at set
     * intervals.
     */
    public void start() {
        setVisible(true);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this::nextFrame, 0, FRAME_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Draws the next frame, i.e. refreshes the scores and game.
     */
    private void nextFrame() {
        environmentPanel.repaint();
        infoPanel.refresh();
    }

}
