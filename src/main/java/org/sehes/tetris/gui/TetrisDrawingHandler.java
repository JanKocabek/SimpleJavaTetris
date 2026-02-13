package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.sehes.tetris.logic.GameBoard;
import org.sehes.tetris.logic.Tetromino;

public class TetrisDrawingHandler {

    private static final GameBoard board = GameBoard.getInstance();
    private static final int COL = 10;//number of cell in row (columns)
    private static final int ROWS = 20;//number of cell in column (rows)
    //size of one cell of GameBoard grid, also the size of one block of tetromino
    private static final int SIZETetromino = board.getGRIDUNIT();//latter uncuple this from GameBoard and make it a constant here, but for now it is fine

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
        for (int i = 1; i < COL; i++) {
            g2d.drawLine(SIZETetromino * i, 0, SIZETetromino * i, height);
        }
        for (int i = 1; i < ROWS; i++) {
            g2d.drawLine(0, SIZETetromino * i, width, SIZETetromino * i);
        }
    }

    public static void drawGame(Graphics2D g2d) {
        GameBoard.BlockContent[][] grid = TetrisDrawingHandler.board.getBoard();
        for (int row = grid.length - 1; row >= 0; row--) {
            for (int col = grid[row].length - 1; col >= 0; col--) {
                if (grid[row][col] != GameBoard.BlockContent.EMPTY) {
                    g2d.setColor(grid[row][col].getColor());
                    int x = (col) * SIZETetromino;
                    int y = (row - 1) * SIZETetromino;
                    g2d.fillRect(x, y, SIZETetromino, SIZETetromino);
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
        int size = t.getSIZEREC();
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column]) {
                    g2d.fillRect(x + column * size, y + row * size, size, size);
                }
            }
        }
    }

    public static void repaint() {
        TetrisCanvas.getInstance().repaint();
    }
}
