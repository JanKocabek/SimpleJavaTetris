package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ScorePanel extends JPanel {

    private final ScoreLabel scoreUI;
    private final Font scoreFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private static final int THICKNESS = 2;
    public ScorePanel() {
        super();

        setOpaque(true);
        this.scoreUI = new ScoreLabel();
        setBackground(Color.WHITE);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, THICKNESS);
        Border lineBorderWithTitle = BorderFactory.createTitledBorder(lineBorder, "Score", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, scoreFont, Color.BLACK);
        setBorder(lineBorderWithTitle);
        add(scoreUI);
    }

    public void updateScore(int score) {
        scoreUI.updateScore(score);
    }

    public void resetScore() {
        scoreUI.resetScore();
    }

    private class ScoreLabel extends JLabel {

        private static final String SCORE_FORMAT = "%07d";

        ScoreLabel() {
            setOpaque(true);
            setFont(scoreFont);
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
            setText(String.format(SCORE_FORMAT, 0));
        }

        public void updateScore(int score) {
            setText(String.format(SCORE_FORMAT, score));
        }

        public void resetScore() {
            setText(String.format(SCORE_FORMAT, 0));
        }
    }
}
