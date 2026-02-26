package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class GameWindow extends JFrame {

    private TetrisCanvas canvas;
    private ScoreUI scoreUI;

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
    GameWindow(int width, int height) {
        super("Tetris");
        //basic setting for a window
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setPreferredSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
    }

    public TetrisCanvas getCanvas() {
        return canvas;
    }

    public ScoreUI getScoreUI() {
        return scoreUI;
    }

    void setCanvas(TetrisCanvas canvas) {
        this.canvas = canvas;
        this.add(canvas);
    }

    void setScoreUI(ScoreUI scoreUI) {
        this.scoreUI = scoreUI;
        this.add(scoreUI);
    }
}
