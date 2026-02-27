package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.sehes.tetris.controller.GameManager;

/**
 * The info panel is the bottom panel of the tetris game. It is used to display
 * the current state of the game, such as new game, paused, game over, etc.
 */
public class InfoPanel extends JPanel {

    private final InfoLabel infoLabel;

    InfoPanel() {
        super();
        setOpaque(true);
        setBackground(Color.green);
        setLayout(new BorderLayout());
        //setBorder(BorderFactory.createLineBorder(Color.black, 1));
        infoLabel = new InfoLabel();
        add(infoLabel, BorderLayout.CENTER);
    }

    public void updateInfo(GameManager.GameState gameState) {
        infoLabel.updateInfo(gameState);

    }

    private class InfoLabel extends JLabel {

        private final Font infoFont = new Font(Font.SERIF, Font.BOLD, 18);

        InfoLabel() {
            setText("");
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.TOP);
            setFont(infoFont);
            setForeground(Color.black);
        }

        void updateInfo(GameManager.GameState gameState) {
            switch (gameState) {
                case INITIALIZE -> {
                    infoLabel.setText("press enter to start a new game");
                }
                case PLAYING -> {
                    infoLabel.setText("game is running");
                }
                case PAUSED -> {
                    infoLabel.setText("game is paused - press enter to resume");
                }
                case GAME_OVER -> {
                    infoLabel.setText("nobody can survive forever - press enter to start a new game");
                }
                default ->
                    throw new AssertionError();
            }
        }
    }
}
