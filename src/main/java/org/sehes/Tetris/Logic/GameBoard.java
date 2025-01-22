package org.sehes.Tetris.Logic;

import org.sehes.Tetris.GUI.TetrisCanvas;
import org.sehes.Tetris.GUI.TetrisDrawingHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private final int HEIGHT = TetrisCanvas.getInstance().getHeight();
    private final int WIDTH = TetrisCanvas.getInstance().getWidth();
    private final int MOVE = 30;//how much pix is the one rec move
    private final int GRIDUNIT = 30;// size of one grid block
    private static GameBoard instance;
    private TetrominoFactory currentTetromino;
    private final boolean[][] board;
    private final int delay;
    private final Timer gameLoopTimer;
    private final List<TetrominoFactory> listOfTetrominos;


    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    private GameBoard() {
        board = new boolean[][]{
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false, false},};
        delay = 800;
        gameLoopTimer = new Timer(delay, gameLoopListener);
        listOfTetrominos = new ArrayList<>();
    }

    public List<TetrominoFactory> getPlacedBlocks() {
        return listOfTetrominos;
    }

    /* these method will be in finally  in GameManager class*/
    public TetrominoFactory getCurrentTetromino() {
        return currentTetromino;
    }

    private void getNewTetromino() {
        currentTetromino = new TetrominoFactory(4 * GRIDUNIT, 0);
    }

    public void movePiece(int x, int y) {
        if (checkCollisions(x, y)) {
            currentTetromino.move(x, y);
            TetrisDrawingHandler.repaint();
        }
    }
    /* to here */


    /**
     * primitive colision check
     * @param x value how much should tetromino piece move to right (+) or left (-)
     * @param y
     * @return true if it could move, false if not able to move
     */
    private boolean checkCollisions(int x, int y) {
        int dx = (int) (currentTetromino.getX() + x);
        int dy = (int) (currentTetromino.getY() + y);
        return dx >= 0 && dx < WIDTH && dy >= 0 && dy < HEIGHT;
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
            } else if (currentTetromino.getY() == (HEIGHT - MOVE)) {
                listOfTetrominos.add(currentTetromino);
                getNewTetromino();
            }
        }
    }
}
