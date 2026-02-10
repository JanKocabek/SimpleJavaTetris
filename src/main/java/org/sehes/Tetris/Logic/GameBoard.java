package org.sehes.tetris.logic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.gui.TetrisDrawingHandler;

public class GameBoard {

    public enum BlockContent {
        CYAN(Color.CYAN),
        YELLOW(Color.YELLOW),
        GREEN(Color.GREEN),
        BLUE(Color.BLUE),
        ORANGE(Color.ORANGE),
        RED(Color.RED),
        MAGENTA(Color.MAGENTA),
        EMPTY(null);

        private final Color color;

        BlockContent(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        private static final Map<Color, BlockContent> map = new HashMap<>();

        static {
            for (BlockContent type : BlockContent.values()) {
                map.put(type.color, type);
            }
        }

        public BlockContent setColor(Color color) {
            return map.get(color);
        }
    }

    private final int HEIGHT = TetrisCanvas.getInstance().getHeight();
    private final int WIDTH = TetrisCanvas.getInstance().getWidth();
    private final int MOVE = 30;//how much pix are the rectangles move
    private final int GRIDUNIT = 30;
    private final int ROWS = 21;
    private final int COLUMN = 10;// size of one grid block
    private static GameBoard instance;
    private Tetromino currentTetromino;
    private final BlockContent[][] board;
    private final int delay = 1600;
    private final Timer gameLoopTimer;

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    private GameBoard() {
        board = new BlockContent[ROWS][COLUMN];
        fillBoard();
        gameLoopTimer = new Timer(delay, gameLoopListener);
    }

    private void fillBoard() {
        for (BlockContent[] blockContents : board) {
            Arrays.fill(blockContents, BlockContent.EMPTY);
        }
    }

    public int getGRIDUNIT() {
        return GRIDUNIT;
    }


    /* these methods will be in finally in GameManager class probably*/
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    public BlockContent[][] getBoard() {
        return board;
    }

    private void getNewTetromino() {
        currentTetromino = Tetromino.tetrominoFactory();
    }

    public boolean movePiece(DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }
        if (checkCollisions(currentTetromino, flag)) {
            currentTetromino.move(flag);
            TetrisDrawingHandler.repaint();
            return true;
        }
        return false;
    }

    public void rotatePiece(DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return;
        }
        System.out.println("beforePosition: " + Arrays.toString(currentTetromino.getPosition()));
        boolean[][] nextGrid = currentTetromino.rotate(flag);

        // Check if the rotation would cause a collision
        if (checkCollisions(nextGrid, currentTetromino.getNextPos(), flag)) {
            currentTetromino.setGrid(nextGrid);
            currentTetromino.applyNewPosition();
        }
        System.out.println("afterPosition: " + Arrays.toString(currentTetromino.getPosition()));
    }

    /* to here */
    /**
     * compare if a new position of tetromino isn't occupied, both are
     * represented as 2D boolean array tetromino position [column][row]
     *
     * @param flag which direction are the tetromino supposed to move
     * @return true if collision not happened
     */
    private boolean checkCollisions(Tetromino tetromino, DirectionFlag flag) {
        int newX = tetromino.getPosition()[0] + flag.getX();
        int newY = tetromino.getPosition()[1] + flag.getY();
        if (!checkBoundaries(this.getCurrentTetromino(), flag)) {
            return false;
        }
        for (int row = 0; row < tetromino.getGrid().length; row++) {
            for (int column = 0; column < tetromino.getGrid()[row].length; column++) {
                if (tetromino.getGrid()[row][column]
                        && this.board[newY + row][newX + column] != BlockContent.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkCollisions(boolean[][] tetrominoGrid, int[] position, DirectionFlag flag) {
        int newX = position[0] + flag.getX();
        int newY = position[1] + flag.getY();

        if (!checkBoundaries(getCurrentTetromino(), flag)) {
            return false;
        }

        for (int row = 0; row < tetrominoGrid.length; row++) {
            for (int column = 0; column < tetrominoGrid[row].length; column++) {
                if (tetrominoGrid[row][column] && this.board[newY + row][newX + column] != BlockContent.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method check if the position after moving a piece is in the
     * gameBoardBoundaries
     *
     *
     * @param flag which direction are the tetromino supposed to move
     * @return true if the final position is in the gameBoard boundaries
     */
    private boolean checkBoundaries(Tetromino tetromino, DirectionFlag flag) {
        /* positions after move*/
 /* 0,0 -> 0+dx,0+dy*/
        int Y = tetromino.getPosition()[1]; // Fixed: Y should be position[1], not position[0]
        int newPositionX = tetromino.getPosition()[0] + flag.getX();
        int newPositionY = tetromino.getPosition()[1] + flag.getY();
        int newUpperRightX = newPositionX + tetromino.getGrid()[0].length - 1;
        int newBottomLeftY = newPositionY + tetromino.getGrid().length - 1;
        return newPositionX >= 0
                && newUpperRightX < this.board[Y].length
                && newBottomLeftY < this.board.length;
    }

    private void addBlockToBoard(Tetromino tetromino) {
        int X = tetromino.getPosition()[0];
        int Y = tetromino.getPosition()[1];
        for (int row = 0; row < tetromino.getGrid().length; row++) {
            for (int column = 0; column < tetromino.getGrid()[row].length; column++) {
                if (tetromino.getGrid()[row][column]) {
                    this.board[Y + row][X + column] = this.board[Y + row][X + column].setColor(tetromino.getColor());
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
