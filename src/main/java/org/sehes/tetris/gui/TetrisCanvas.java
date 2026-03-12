package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import org.sehes.tetris.config.GameParameters;
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
    private final AtomicBoolean isBoardDirty = new AtomicBoolean(false);

    TetrisCanvas(TetrisDrawingHandler drawingHandler, GameManager gameManager) {
        Dimension prefSize = new Dimension(GameParameters.BLOCK_SIZE * GameParameters.COLUMNS,
                GameParameters.BLOCK_SIZE * GameParameters.VISIBLE_ROWS);
        this.setPreferredSize(prefSize);
        this.setMinimumSize(prefSize);
        this.setMaximumSize(prefSize);
        this.drawingHandler = drawingHandler;
        this.gameManager = gameManager;
        setBackground(Color.BLACK);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        drawingHandler.initialize(g2d);
        if (gameManager.getGameState() == GameManager.GameState.PREPARED) {
            return; // Not at the beginning: super.paintComponent() must run first to clear the
                    // canvas.
        }
        g2d.drawImage(drawingHandler.getGrid(), 0, 0, null);
        final boolean wasDirty = isBoardDirty.getAndSet(false);
        drawingHandler.paintGameBoard(g2d, gameManager.getBoardView(), wasDirty);
        drawingHandler.drawCurrentTetromino(g2d, gameManager.getCurrentTetromino());
    }

    public void repaintCanvas(boolean boardDirty) {
        if (boardDirty) {
            this.isBoardDirty.compareAndSet(false, true);
        }
        repaint();
    }
}
