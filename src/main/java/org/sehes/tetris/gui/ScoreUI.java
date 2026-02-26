package org.sehes.tetris.gui;

public class ScoreUI extends javax.swing.JLabel {
    
    public ScoreUI() {
        setOpaque(true);
        setText("Score: 0");
    }

    public void updateScore(int score) {
        setText("Score: " + score);
    }

    public void resetScore() {
        setText("Score: 0");
    }



}
