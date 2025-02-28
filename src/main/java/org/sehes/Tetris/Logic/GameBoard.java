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
    private final int MOVE = 30;//how much pix are the rectangles move
    private final int GRIDUNIT = 30;// size of one grid block
    private static GameBoard instance;
    private Tetromino currentTetromino;
    private final boolean[][] board;
    private final int delay;
    private final Timer gameLoopTimer;
    private final List<Tetromino> listOfTetrominos;


    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    private GameBoard() {
        board = new boolean[21][10];

        delay = 800;
        gameLoopTimer = new Timer(delay, gameLoopListener);
        listOfTetrominos = new ArrayList<>();
    }

    public List<Tetromino> getPlacedBlocks() {
        return listOfTetrominos;
    }

    /* these methods will be in finally in GameManager class probably*/
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    private void getNewTetromino() {
        currentTetromino = Tetromino.tetrominoFactory();
        listOfTetrominos.add(currentTetromino);
    }

    public boolean movePiece(DirectionFlag flag) {
        if (checkCollisions(currentTetromino, flag)) {
            currentTetromino.move(flag);
            TetrisDrawingHandler.repaint();
            return true;
        }
        return false;
    }
    /* to here */


    /**
     * compare if a new position of tetromino isn't occupied,
     * both are represented as 2D boolean array
     * tetromino position [column][row]
     * @param tetrominoGrid 2D boolean array of tetromino
     * @param tetrominoPosition coordinates of a upper left position of tetromino
     * @param flag which direction are the tetromino supposed to move
     * @return true if collision not happened
     */
    private boolean checkCollisions(Tetromino tetromino, DirectionFlag flag) {
        int newX = tetromino.getPosition()[0] + flag.getX();
        int newY = tetromino.getPosition()[1] + flag.getY();
        if (!checkBoundaries(this.getCurrentTetromino(), flag)) return false;
        for (int row = 0; row < tetromino.getGrid().length; row++) {
            for (int column = 0; column < tetromino.getGrid()[row].length; column++) {
                if (tetromino.getGrid()[row][column] && this.board[newY + row][newX + column]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method check if the position after moving a piece is in the gameBoardBoundaries
     *
     * @param tetrominoPosition its hold the new column [0] and row [1] position
     * @param tetrominoLenght  the lenght of tetromino grid [0]  coll and row [1]
     * @param flag which direction are the tetromino supposed to move
     * @return true if the final position is in the gameBoard boundaries
     */
    private boolean checkBoundaries(Tetromino tetromino, DirectionFlag flag) {
        /* positions after move*/
        /* 0,0 -> 0+dx,0+dy*/
        int Y = tetromino.getPosition()[0];
        int newPositionX = tetromino.getPosition()[0] + flag.getX();
        int newPositionY = tetromino.getPosition()[1] + flag.getY();

        int newUpperRightX = newPositionX + tetromino.getGrid()[0].length - 1;
        int newBottomLeftY = newPositionY + tetromino.getGrid().length - 1;
        return newPositionX >= 0 &&
               newUpperRightX < this.board[Y].length &&
               newBottomLeftY < this.board.length;

    }

    private void addBlockToBoard(Tetromino tetromino) {
        int X = tetromino.getPosition()[0];
        int Y = tetromino.getPosition()[1];
        for (int row = 0; row < tetromino.getGrid().length; row++) {
            for (int column = 0; column < tetromino.getGrid()[row].length; column++) {
                if (tetromino.getGrid()[row][column]) {
                    this.board[Y+row][X+column] = true;
                }
            }
        }
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
            if (!movePiece(DirectionFlag.DOWN)) {
                addBlockToBoard(currentTetromino);
                getNewTetromino();
            }
        }
    }
}
