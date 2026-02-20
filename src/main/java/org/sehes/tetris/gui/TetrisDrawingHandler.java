package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.sehes.tetris.logic.GameBoard;
import org.sehes.tetris.logic.GameParameters;
import org.sehes.tetris.logic.Tetromino;

/**
 * The TetrisDrawingHandler class is responsible for rendering the game state onto the screen.
 * It provides methods to initialize the graphics context, draw the game grid, and render the current Tetromino piece based on the game board's state.
 *  The drawing handler interacts with the GameManager to retrieve necessary information about the game state and ensures that the visual representation of the game is accurate and up to date.
 */
public class TetrisDrawingHandler {

    public void initialize(Graphics2D g2d) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);
    }

    public void drawGrid(Graphics2D g2d) {
        final Rectangle drawingArea = g2d.getClipBounds();
        final int width = drawingArea.width;
        final int height = drawingArea.height;

        g2d.setColor(Color.YELLOW);
        for (int i = 1; i < GameParameters.COLUMNS; i++) {
            g2d.drawLine(GameParameters.BLOCK_SIZE * i, 0, GameParameters.BLOCK_SIZE * i, height);
        }
        for (int i = 1; i < GameParameters.VISIBLE_ROWS; i++) {
            g2d.drawLine(0, GameParameters.BLOCK_SIZE * i, width, GameParameters.BLOCK_SIZE * i);
        }
    }

    public void drawBoardState(Graphics2D g2d, GameBoard.BlockContent[][] board) {
        for (int row = board.length - 1; row >= 0; row--) {
            for (int col = board[row].length - 1; col >= 0; col--) {
                if (board[row][col] != GameBoard.BlockContent.EMPTY) {
                    g2d.setColor(board[row][col].getColor());
                    int x = (col) * GameParameters.BLOCK_SIZE;
                    int y = (row - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
                    g2d.fillRect(x, y, GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE);
                }
            }
        }
    }

    public void drawCurrentTetromino(Graphics2D g2d, Tetromino t) {
        if (t == null) {
            return;
        }
        g2d.setColor(t.getColor());
        boolean[][] grid = t.getGrid();
        Point position = calculateTetrominoPosition(t);
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column]) {
                    g2d.fillRect(position.x + column * GameParameters.BLOCK_SIZE, position.y + row * GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE);
                }
            }
        }
    }

    private Point calculateTetrominoPosition(Tetromino t) {
        int x = t.getPosition().x * GameParameters.BLOCK_SIZE;
        int y = (t.getPosition().y - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
        return new Point(x, y);
    }
}
