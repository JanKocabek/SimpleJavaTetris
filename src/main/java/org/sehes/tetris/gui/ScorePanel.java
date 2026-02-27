package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ScorePanel extends JPanel {

    private final ScoreLabel scoreUI;
    private static final int THICKNESS = 2;

    private final Font scoreFont = new Font(Font.MONOSPACED, Font.BOLD, 20);

    public ScorePanel() {
        super();

        setOpaque(true);
        this.scoreUI = new ScoreLabel();
        setBackground(Color.WHITE);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, THICKNESS);
        Border lineBorderWithTitle = BorderFactory.createTitledBorder(lineBorder, "Score", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, scoreFont, Color.BLACK);
        setBorder(lineBorderWithTitle);
        setLayout(new BorderLayout());
        add(scoreUI, BorderLayout.NORTH);
    }

    public void updateScore(int score) {
        scoreUI.updateScore(score);
    }

    public void resetScore() {
        scoreUI.resetScore();
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

            setText(String.format(SCORE_FORMAT, 0));
        }

        void updateScore(int score) {
            setText(String.format(SCORE_FORMAT, score));
        }

        void resetScore() {
            setText(String.format(SCORE_FORMAT, 0));
        }
    }
}
