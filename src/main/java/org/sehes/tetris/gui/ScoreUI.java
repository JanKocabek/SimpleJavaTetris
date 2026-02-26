package org.sehes.tetris.gui;

import java.awt.Font;

public class ScoreUI extends javax.swing.JLabel {

    private static final String SCORE_FORMAT = "%07d";

    public ScoreUI() {
        setOpaque(true);
        setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        setText("Score: 0000000");
    }

    public void updateScore(int score) {
        setText("Score: " + String.format(SCORE_FORMAT, score));
    }

    public void resetScore() {
        setText("Score: 0000000");
    }
}
