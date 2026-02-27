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
                case INIT ->
                    setText("game is loading wait please");

                case PREPARED ->
                    setText("press enter to start a new game");

                case PLAYING ->
                    setText("game is running");

                case PAUSED ->
                    setText("game is paused - press enter to resume");

                case GAME_OVER ->
                    setText("nobody can survive forever - press enter to start a new game");

                default -> {
                    setText("UNEXPECTED MOMENT send me bug report");
                }

            }
        }
    }
}
