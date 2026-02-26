package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.sehes.tetris.controller.GameManager;

/**
 * The TetrisCanvas class is responsible for rendering the game state onto the
 * screen. It extends JPanel and overrides the paintComponent method to draw the
 * game grid and the current Tetromino piece. The canvas interacts with the
 * GameManager to retrieve the current game state and update the display
 * accordingly. It also provides a method to repaint the canvas when the game
 * state changes, ensuring that the visual representation of the game is always
 * up to date.
 */
public class TetrisCanvas extends JPanel {

    private final GameManager gameManager;
    private final TetrisDrawingHandler drawingHandler;

    TetrisCanvas(TetrisDrawingHandler drawingHandler, GameManager gameManager) {
        this.drawingHandler = drawingHandler;
        this.gameManager = gameManager;
        setBackground(Color.BLACK);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawingHandler.initialize(g2d);
        if (gameManager.getGameState() == GameManager.GameState.INITIALIZE) {
            return;
        }
        drawingHandler.drawGrid(g2d);
        drawingHandler.drawBoardState(g2d, gameManager.getBoard().getBoardView());
        drawingHandler.drawCurrentTetromino(g2d, gameManager.getBoard().getCurrentTetromino());
    }

    public void repaintCanvas() {
        repaint();
    }
}
