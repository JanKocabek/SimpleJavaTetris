package org.sehes.tetris.logic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import org.sehes.tetris.gui.TetrisDrawingHandler;

/*
DISCLAIMER:
RATHER USE posCol AND posRow FOR POSITION VARIABLES!!!!
In the game board and tetromino representation, the x is  left to right (columns), y is top to bottom (rows)
but 2D arrays are represented as [y][x] that is [row][column] so be careful when programming!!!!!
This class currently handles both the game board state management and the game loop timing.
In future iterations, it is advisable to separate these concerns into distinct classes:
- GameBoard: Responsible solely for managing the state of the game board, including piece placement, collision detection, and line clearing.
- GameManager: Oversees the game loop, timing, and overall game state transitions (e.g., starting, pausing, ending the game).
 */
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

        /**
         * This method takes a Color object from tetromino and returns the corresponding BlockContent enum value. It uses a static map to efficiently look up the BlockContent based on the provided Color. If the color is not found in the map, it will return null, which can be handled appropriately in the calling code.
         * @param color The Color object for which to find the corresponding BlockContent enum value.
         * @return The BlockContent enum value corresponding to the provided Color, or null if the color is not found in the map.means that the block is empty and doesn't have a color.
         */
        public static BlockContent fromColor(Color color) {
            return map.get(color);
        }
    }   

    //private final int MOVE = 30;//how much pix are the rectangles move
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
        if (canMove(currentTetromino.getGrid(), currentTetromino.getPosition(), flag)) {
            currentTetromino.move(flag);
            TetrisDrawingHandler.repaint();
            return true;
        }
        return false;
    }

    /**
     * This method is responsible for rotating the current tetromino in the
     * specified direction (right or left). It first checks if there is a
     * current tetromino to rotate. If there isn't, it simply returns without
     * doing anything. Then, it calculates the next grid configuration of the
     * tetromino after rotation using the rotate method of the Tetromino class.
     * Before applying the rotation, it checks for potential collisions that
     * might occur due to the new grid configuration using the checkCollisions
     * method. If there are no collisions, it updates the tetromino's grid to
     * the new rotated configuration. Finally, it prints the position of the
     * tetromino before and after rotation for debugging purposes
     *
     * @param flag The direction in which to rotate the tetromino (e.g.,
     * ROTATE_R for right rotation, ROTATE_L for left rotation)
     */
    public void rotatePiece(DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return;
        }

        System.out.println("beforePosition: " + Arrays.toString(currentTetromino.getPosition()));// Debugging output to show the position before rotation
        boolean[][] nextGrid = currentTetromino.rotate(flag);
        if (canRotate(nextGrid, flag)) {
            currentTetromino.setGrid(nextGrid);
        }

        System.out.println("afterPosition: " + Arrays.toString(currentTetromino.getPosition()));// Debugging output to show the position after rotation
    }

    /* to here */
    /**
     * compare if a new position of tetromino isn't occupied, both are
     * represented as 2D boolean array tetromino position [column][row]
     *
     * @param flag which direction are the tetromino supposed to move
     * @return true if collision not happened
     */
    private boolean canMove(boolean[][] tetrominoGrid, int[] position, DirectionFlag flag) {
        if (tetrominoGrid == null || flag == null) {
            return false;
        }
        int columns = 0; //but in tetromino is flag.getX()
        int rows = 1; //but in tetromino is flag.getY()
        int[] newPosition = {position[columns] + flag.getX(), position[rows] + flag.getY()};
        if (!checkBoundaries(tetrominoGrid, newPosition)) {
            return false;
        }
        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (this.board[newPosition[rows] + gridR][newPosition[columns] + gridC] != BlockContent.EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method check if the position after rotating a piece is in the
     * gameBoardBoundaries and not occupied by another piece
     *
     * @param tetrominoGrid the grid of the tetromino after rotation
     * @param flag which direction are the tetromino supposed to rotate
     * @return true if collision not happened and the position is in the
     * gameBoard boundaries
     */
    private boolean canRotate(boolean[][] tetrominoGrid, DirectionFlag flag) {
        if (tetrominoGrid == null || flag == null) {
            return false;
        }
        int[] position = currentTetromino.getPosition();
        if (!checkBoundaries(tetrominoGrid, position)) {
            return false;
        }
        int x = position[0];
        int y = position[1];

        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (this.board[y + gridR][x + gridC] != BlockContent.EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * this method check if the position after moving a piece is in the
     * gameBoardBoundaries and not occupied by another piece
     *
     * @param tetrominoGrid the grid of the tetromino after moving/rotating
     * @param newPosition the position of the tetromino after moving /
     * rotating(doesn't change)
     * @param flag which direction are the tetromino supposed to move/rotate
     * @return true if the final position is in the gameBoard boundaries
     */
    private boolean checkBoundaries(boolean[][] tetrominoGrid, int[] newPosition) {
        int colPos = newPosition[0];//current column
        int rowPos = newPosition[1];//current row
        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (colPos + gridC < 0
                        || colPos + gridC >= board[0].length
                        || rowPos + gridR >= board.length
                        || rowPos + gridR < 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addBlockToBoard(Tetromino tetromino) {
        int posCol = tetromino.getPosition()[0];
        int posRow = tetromino.getPosition()[1];
        for (int row = 0; row < tetromino.getGrid().length; row++) {
            for (int column = 0; column < tetromino.getGrid()[row].length; column++) {
                if (tetromino.getGrid()[row][column]) {
                    this.board[posRow + row][posCol + column] = BlockContent.fromColor(tetromino.getColor());
                }
            }
        }
    }

    ActionListener gameLoopListener = new MainLoopListener();

    public void startGame() {
        gameLoopTimer.start();
    }

    private class MainLoopListener implements ActionListener {

        @Override
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
