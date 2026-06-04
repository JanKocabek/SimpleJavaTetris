package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.GameManager.GameState;

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

    private final transient GameManager gameManager;
    private final transient TetrisDrawingHandler drawingHandler;
    private final AtomicBoolean isBoardDirty = new AtomicBoolean(false);
    private static final Color backgroundColor = Color.BLACK;

    TetrisCanvas(TetrisDrawingHandler drawingHandler, GameManager gameManager) {
        Dimension prefSize = new Dimension(GameParameters.BLOCK_SIZE * GameParameters.COLUMNS,
                GameParameters.BLOCK_SIZE * GameParameters.VISIBLE_ROWS);
        this.setPreferredSize(prefSize);
        this.setMinimumSize(prefSize);
        this.setMaximumSize(prefSize);
        this.drawingHandler = drawingHandler;
        this.gameManager = gameManager;
        setBackground(backgroundColor);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        drawingHandler.initialize(g2d);
        switch (gameManager.getGameState()) {
            case GameState.PREPARED -> {
              //do nothing
            }
            case GameState.PLAYING -> {
                final boolean wasDirty = isBoardDirty.getAndSet(false);
                drawingHandler.paintGameBoard(g2d, gameManager.getBoardView(), wasDirty);
                if (gameManager.getGameState() == GameState.PLAYING) {
                    drawingHandler.drawCurrentTetromino(g2d, gameManager.getCurrentTetromino());
                }
            }
            default -> {
                final boolean wasDirty = isBoardDirty.getAndSet(false);
                drawingHandler.paintGameBoard(g2d, gameManager.getBoardView(), wasDirty);
            }
        }
    }

    /**
     * Repaints the canvas with precise control over the dirty flag state.
     * Unlike the default {@link #repaint()} method, this method also manages
     * the internal dirty flag, which is used to optimize rendering by indicating
     * whether the game board has changed and requires redrawing.
     *
     * @param boardDirty true if the board has been modified and requires redrawing,
     *                   false otherwise
     */
    public void repaintCanvas(boolean boardDirty) {
        if (boardDirty) {
            this.isBoardDirty.compareAndSet(false, true);
        }
        repaint();
    }
}
