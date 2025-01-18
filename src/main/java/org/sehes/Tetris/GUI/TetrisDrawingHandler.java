package org.sehes.Tetris.GUI;

import org.sehes.Tetris.Logic.GameBoard;
import org.sehes.Tetris.Logic.TetrominoFactory;

import java.awt.*;

public class TetrisDrawingHandler {
    private final static int SIZET = 30;//size of one rectangle from tetromino
    private final static int CWIDTH = TetrisCanvas.getInstance().getWidth();
    private final static int CHEIGHT = TetrisCanvas.getInstance().getHeight();
    private final static int COL = 10;//number of cell in row (columns)
    private final static int ROWS = 20;//number of cell in column (rows)
    private final static GameBoard gameBoard = GameBoard.getInstance();
    private static TetrominoFactory tetromino;

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
        if (gameBoard.getCurrentTetromino() != null) {
            tetromino = gameBoard.getCurrentTetromino();
            g2d.setColor(tetromino.getColor());
            g2d.fill(tetromino.getRectangle());
        }
    }
}
