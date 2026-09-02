package org.sehes.tetris.gui;

import org.jspecify.annotations.NonNull;
import org.sehes.tetris.controller.Observer;
import org.sehes.tetris.model.score.ScoreInfoDTO;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ScorePanel extends JPanel implements Observer<ScoreInfoDTO> {

    private static final int SCORE_BORDER_THICK = 2;
    private static final String EMPTY = " ";
    private final ScoreLabel scoreUI;
    private final Font scoreFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private final JLabel clearingInfo = new JLabel(EMPTY, SwingConstants.CENTER);
    private final JLabel comboInfo = new JLabel(EMPTY, SwingConstants.CENTER);
    private final JLabel b2bInfo = new JLabel(EMPTY, SwingConstants.CENTER);

    public ScorePanel() {
        super();
        setOpaque(true);
        setBackground(Color.WHITE);
        //GridBagConstraints for stacking vertically
        setLayout(new GridBagLayout());
        this.scoreUI = new ScoreLabel();
        final GridBagConstraints verStackConstr = createVerStackConstr();
        setupCompGridPos(verStackConstr, 0, new Insets(0, 0, 0, 0));
        add(scoreUI, verStackConstr);
        setupCompGridPos(verStackConstr, 1, new Insets(0, 0, 2, 0));
        add(b2bInfo, verStackConstr);
        setupCompGridPos(verStackConstr, 2, new Insets(0, 0, 4, 0));
//        clearingInfo.setOpaque(true);
//        clearingInfo.setBackground(Color.BLACK);
        add(clearingInfo, verStackConstr);
        setupCompGridPos(verStackConstr, 3, new Insets(0, 0, 4, 0));
//        comboInfo.setOpaque(true);
//        comboInfo.setBackground(Color.BLUE);
        add(comboInfo, verStackConstr);
        //uncomment if needed move up
//        GridBagConstraints fillerConstraints = new GridBagConstraints();
//        fillerConstraints.gridy = 4;
//        fillerConstraints.weighty = 1.0; // Pushes everything above it to the top
//        add(javax.swing.Box.createVerticalGlue(), fillerConstraints);
    }

    private void setupCompGridPos(GridBagConstraints constraints, int row, Insets insets) {
        constraints.gridy = row;
        constraints.insets = insets;
    }

    private @NonNull GridBagConstraints createVerStackConstr() {
        final GridBagConstraints verStackConstr = new GridBagConstraints();
        verStackConstr.gridx = 0;
        verStackConstr.fill = GridBagConstraints.HORIZONTAL;
        verStackConstr.weightx = 1.0;
        return verStackConstr;
    }


    @Override
    public void update(ScoreInfoDTO info) {
        scoreUI.updateScore(info.score());
        final var b2b = info.B2BBonus() ? "B2B" : EMPTY;
        b2bInfo.setText(b2b);
        if (!info.clearType().name().equals("NONE"))
            clearingInfo.setText("%s".formatted(info.clearType().toString().replace("_", EMPTY)));
        else clearingInfo.setText(EMPTY);
        if (info.combo() > 0) {
            comboInfo.setText("Combo x" + info.combo());
        } else {
            comboInfo.setText(EMPTY);
        }
    }

    /**
     * Inner class representing the score label within the ScorePanel. It
     * extends JLabel and is responsible for displaying the current score in a
     * formatted manner. The label is styled with a monospaced font and has a
     * specific background and foreground color to enhance visibility. The
     * ScoreLabel class provides methods to update the displayed score and reset
     * it to zero when needed, ensuring that the score display is always
     * accurate and visually consistent with the overall design of the
     * ScorePanel.
     */
    private class ScoreLabel extends JLabel {

        private static final String SCORE_FORMAT = "%07d";

        ScoreLabel() {
            setOpaque(true);
            setFont(scoreFont);
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
            Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, SCORE_BORDER_THICK);
            Border lineBorderWithTitle = BorderFactory.createTitledBorder(lineBorder, "Score", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, scoreFont, Color.BLACK);
            setBorder(lineBorderWithTitle);
            setText(String.format(SCORE_FORMAT, 0));
        }

        private void updateScore(int score) {
            setText(String.format(SCORE_FORMAT, score));
        }
    }
}
