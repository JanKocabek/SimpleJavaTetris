package org.sehes.tetris.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {

    private final TetrisCanvas canvas;
    private final ScorePanel scoreUI;

    //private final FlowLayout layout = new FlowLayout();/if we added manager later
    /**
     * Constructs a new GameWindow with the specified width and height. The
     * window is set up with a white background, a preferred size based on the
     * provided dimensions, and a null layout. The canvas for drawing the game
     * will be added later using the setCanvas method.
     *
     * @param width The width of the game window.
     * @param height The height of the game window.
     *
     */
    GameWindow(JPanel contentPane,TetrisCanvas canvas, ScorePanel scoreUI) {
        super("Tetris");
        setContentPane(contentPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.canvas = canvas;
        this.scoreUI = scoreUI;
    }

    public TetrisCanvas getCanvas() {
        return canvas;
    }

    public ScorePanel getScoreUI() {
        return scoreUI;
    }
}
