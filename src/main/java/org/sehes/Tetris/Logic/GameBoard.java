package org.sehes.Tetris.Logic;

import org.sehes.Tetris.GUI.TetrisCanvas;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameBoard {
    private static final int MOVE = 30;
    private final int GRIDUNIT = 30;

    private static GameBoard instance;
    private TetrisCanvas canvas;
    private double width;
    private double height;
    private TetrominoFactory currentTetromino;
    private final boolean[][] board;
    private final int delay;
    private final Timer gameLoopTimer;


    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public void init(TetrisCanvas canvas) {
        if (this.canvas == null) {
            this.canvas = TetrisCanvas.getInstance();
            width = canvas.getSize().width;
            height = canvas.getSize().height;
            currentTetromino = null;
            //tetromino = new TetrominoFactory(0, 0, 30, 30);
        }
    }

    private GameBoard() {
        board = new boolean[20][10];
        delay = 800;
        gameLoopTimer = new Timer(delay, gameLoopListener);
    }


    public TetrominoFactory getCurrentTetromino() {
        return currentTetromino;
    }

    public void movePiece(int x, int y) {
        if (checkCollisions(x, y)) {
            currentTetromino.move(x, y);
            canvas.repaint();
        }
    }

    /**
     *
     * @param x value how much should tetromino piece move to right (+) or left (-)
     * @param y
     * @return true if it could move, false if not able to move
     */
    private boolean checkCollisions(int x, int y) {
        int dx = (int) (currentTetromino.getX() + x);
        int dy = (int) (currentTetromino.getY() + y);
        return dx >= 0 && dx < width && dy >= 0 && dy < height;
    }


    ActionListener gameLoopListener = new MainLoopListener();

    public void startGame() {
        gameLoopTimer.start();

    }

    private class MainLoopListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (currentTetromino == null) {
                getNewTetromino();
            }
            if (checkCollisions(0, MOVE)) {

                movePiece(0, MOVE);
            } else gameLoopTimer.stop();
        }
    }

    private void getNewTetromino() {
        currentTetromino = new TetrominoFactory(4 * GRIDUNIT, 0);
    }
}
