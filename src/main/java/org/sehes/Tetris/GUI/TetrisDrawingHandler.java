package org.sehes.Tetris.GUI;

import org.sehes.Tetris.Logic.GameBoard;
import org.sehes.Tetris.Logic.Tetromino;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TetrisDrawingHandler {
    private final static int SIZET = 30;//size of one cell of GameBoard grid
    private final static int CWIDTH = TetrisCanvas.getInstance().getWidth();
    private final static int CHEIGHT = TetrisCanvas.getInstance().getHeight();
    private final static int COL = 10;//number of cell in row (columns)
    private final static int ROWS = 20;//number of cell in column (rows)
    private final static GameBoard gameBoard = GameBoard.getInstance();

    private TetrisDrawingHandler() {

    }

    public static void initialize(Graphics2D g2d) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);
    }

    public static void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        for (int i = 1; i < COL; i++) {
            g2d.drawLine(SIZET * i, 0, SIZET * i, CHEIGHT);
        }
        for (int i = 1; i < ROWS; i++) {
            g2d.drawLine(0, SIZET * i, CWIDTH, SIZET * i);
        }

    }

    public static void drawGame(Graphics2D g2d) {
        for (Tetromino block : gameBoard.getPlacedBlocks()) {
            g2d.setColor(block.getColor());
            boolean[][] grid = block.getGrid();
            int x = block.getxCoord();
            int y = block.getyCoord();
            int size =block.getSIZEREC();
            for (int row = 0; row < grid.length; row++) {
                for (int column = 0; column < grid[row].length; column++) {
                    if (grid[row][column]) {
                        g2d.fill(new Rectangle2D.Double(x+column*size,y+row*size, size,size));
                    }
                }
            }
        }
    }



    public static void repaint() {
        TetrisCanvas.getInstance().repaint();
    }
}
