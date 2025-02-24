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

    /* these methods will be in finally in GameManager class probably*/
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
     * compare 2d array representation of tetromino with 2d representation
     * [00][01]
     * [10][11]-tetromino grid
     * tetromino position [column][row]
     * @param tetrominoGrid
     * @param tetrominoPosition
     * @return true if collision not happened
     */
    private boolean checkCollisions(boolean[][] tetrominoGrid, int[] tetrominoPosition, DirectionFlag flag) {
        int column = tetrominoPosition[0];
        int row = tetrominoPosition[1];
        if (!checkBoundaries(tetrominoPosition, new int[]{tetrominoGrid.length, tetrominoGrid[0].length}, flag))
            return false;
        for (int gridCol = 0; gridCol < tetrominoGrid.length; gridCol++) {
            for (int gridRow = 0; gridRow < tetrominoGrid.length; gridRow++) {
                int newPosCol = column + gridCol+ flag.getDCol();
                int newPosRow = row + gridRow + flag.getDRow();
                if( tetrominoGrid[gridRow][gridCol] && this.board[newPosCol][newPosRow] ){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method check if the position after moving a piece is in the gameBoardBoundaries
     *
     * @param tetrominoPosition its hold the current column [0] and row [1] position
     * @param tetrominoLenght  the lenght of tetromino grid [0]  coll and row [1]
     * @param flag which direction are the tetromino supposed to move
     * @return true if the final position is in the gameBoard boundaries
     */
    private boolean checkBoundaries(int[] tetrominoPosition, int[] tetrominoLenght, DirectionFlag flag) {
        /* positions after move*/
        /* 0,0 -> 0+dx,0+dy*/
        int[] leftUpPos = {
                tetrominoPosition[0] + flag.getDCol(),
                tetrominoPosition[1] + flag.getDRow()
        };
        /*[][]

         * [][]*/
        int[] rightUpPos = {
                leftUpPos[0] + tetrominoLenght[0] - 1,
                leftUpPos[1]
        };
        int[] leftDownPos = {
                leftUpPos[0],
                leftUpPos[1] + tetrominoLenght[1] - 1
        };
        int[] rightDownPos = {
                rightUpPos[0],
                rightUpPos[1] + tetrominoLenght[1] - 1
        };
        /*need to check only
         *  x
         *  ←↑ →y
         *  [][]
         *    []↓  */
        return leftUpPos[0] >= 0 && leftUpPos[1] >= 0 &&
                rightUpPos[0] <= this.board.length - 1 &&
                rightDownPos[0] <= this.board[0].length - 1;
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
                getNewTetromino();
            }
        }
    }
}
