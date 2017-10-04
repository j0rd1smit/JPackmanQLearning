package nl.smit.jpackman_qlearning.ui;

import nl.smit.jpackman_qlearning.agent.ReinforcementAgent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * [Class explanation]
 *
 * @author Jordi Smit, 4-10-2017.
 */
public class InfoPanel extends JPanel {
    /**
     * The label text formats
     */
    private static final int AMOUNT_OF_LABELS = 4;
    private static final String TRAINING_MODE_CAPTION = "Mode: ";
    private static final String EPS_CAPTION = "Eps value: ";
    private static final String TOTAL_MOVES_CAPTION = "Total moves: ";
    private static final String RESET_MOVES_CAPTION = "Moves since reset: ";

    /**
     * The labels.
     */
    private final JLabel trainigModeLabel;
    private final JLabel epsLabel;
    private final JLabel movesMadeLabel;
    private final JLabel movesMadeSinceReset;

    /**
     * The agent.
     */
    private final ReinforcementAgent agent;


    /**
     * The default constructor.
     * @param agent The agents how info will be displayed.
     */
    public InfoPanel(ReinforcementAgent agent) {
        this.agent = agent;

        setLayout(new GridLayout(AMOUNT_OF_LABELS, 1));
        trainigModeLabel = new JLabel(TRAINING_MODE_CAPTION, JLabel.CENTER);
        epsLabel = new JLabel(EPS_CAPTION, JLabel.CENTER);
        movesMadeLabel = new JLabel(TOTAL_MOVES_CAPTION, JLabel.CENTER);
        movesMadeSinceReset = new JLabel(RESET_MOVES_CAPTION, JLabel.CENTER);

        add(trainigModeLabel);
        add(epsLabel);
        add(movesMadeLabel);
        add(movesMadeSinceReset);

    }

    /**
     * Refreshes the displayed info.
     */
    void refresh() {
        trainigModeLabel.setText(formatTrainingsModeCaption());
        epsLabel.setText(formatEpsCaption());
        movesMadeLabel.setText(formatTotalMovesCaption());
        movesMadeSinceReset.setText(formatSinceResetMovesCaption());
    }

    private String formatTrainingsModeCaption() {
        if (agent.isLearning()) {
            return TRAINING_MODE_CAPTION + "training";
        }
        return TRAINING_MODE_CAPTION + "best known move";
    }

    private String formatEpsCaption() {
       return EPS_CAPTION + Double.toString(agent.getRandomActionChange());
    }

    private String formatTotalMovesCaption() {
        return TOTAL_MOVES_CAPTION + agent.getTotalMovesMade();
    }

    private String formatSinceResetMovesCaption() {
        return RESET_MOVES_CAPTION + agent.getMovesMadeSinceReset();
    }



}
