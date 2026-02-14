package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.sehes.tetris.logic.GameBoard;
import org.sehes.tetris.logic.GameParameters;
import org.sehes.tetris.logic.Tetromino;

public class TetrisDrawingHandler {

    private static final GameBoard board = GameBoard.getInstance();

    private TetrisDrawingHandler() {

    }

    public static void initialize(Graphics2D g2d) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);
    }

    public static void drawGrid(Graphics2D g2d) {
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

    public static void drawGame(Graphics2D g2d) {
        GameBoard.BlockContent[][] grid = TetrisDrawingHandler.board.getBoard();
        for (int row = grid.length - 1; row >= 0; row--) {
            for (int col = grid[row].length - 1; col >= 0; col--) {
                if (grid[row][col] != GameBoard.BlockContent.EMPTY) {
                    g2d.setColor(grid[row][col].getColor());
                    int x = (col) * GameParameters.BLOCK_SIZE;
                    int y = (row - GameParameters.ROW_OFFSET) * GameParameters.BLOCK_SIZE;
                    g2d.fillRect(x, y, GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE);
                }
            }
        }
        if (board.getCurrentTetromino() != null) {
            drawCurrentTetromino(g2d);
        }
    }

    private static void drawCurrentTetromino(Graphics2D g2d) {
        Tetromino t = board.getCurrentTetromino();
        g2d.setColor(t.getColor());
        boolean[][] grid = t.getGrid();
        int x = t.getXCoord();
        int y = t.getYCoord();
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column]) {
                    g2d.fillRect(x + column * GameParameters.BLOCK_SIZE, y + row * GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE);
                }
            }
        }
    }

    public static void repaint() {
        TetrisCanvas.getInstance().repaint();
    }
}
