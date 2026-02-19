package org.sehes.tetris.logic;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
         * This method takes a Color object from tetromino and returns the
         * corresponding BlockContent enum value. It uses a static map to
         * efficiently look up the BlockContent based on the provided Color. If
         * the color is not found in the map, it will return null, which can be
         * handled appropriately in the calling code.
         *
         * @param color The Color object for which to find the corresponding
         * BlockContent enum value.
         * @return The BlockContent enum value corresponding to the provided
         * Color, or null if the color is not found in the map.means that the
         * block is empty and doesn't have a color.
         */
        public static BlockContent fromColor(Color color) {
            if (color == null) {
                return EMPTY;
            }
            return map.get(color);
        }
    }
    private Tetromino currentTetromino;
    private final BlockContent[][] board;
    private static final int STARTING_ROW = 0;//the row where new tetromino will spawn
    private static final int STARTING_COL = 4;//the column where new tetromino

    public GameBoard() {
        board = new BlockContent[GameParameters.ROWS][GameParameters.COLUMNS];
        fillBoard();
    }

    private void fillBoard() {
        for (BlockContent[] blockContents : board) {
            Arrays.fill(blockContents, BlockContent.EMPTY);
        }
    }

    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    public BlockContent[][] getGrid() {
        return board;
    }

    public void setNewTetromino() {
        Point startingPosition = new Point(STARTING_COL, STARTING_ROW);
        currentTetromino = Tetromino.tetrominoFactory(startingPosition);
    }

    public boolean tryMovePiece(DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }
        if (canMove(currentTetromino.getGrid(), currentTetromino.getPosition(), flag)) {
            currentTetromino.move(flag);
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
    public boolean tryRotatePiece(DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }

        System.out.println("beforePosition: " + currentTetromino.getPosition().toString());// Debugging output to show the position before rotation
        boolean[][] nextGrid = currentTetromino.rotate(flag);
        if (!canRotate(nextGrid)) {
            return false;
        }
        currentTetromino.setGrid(nextGrid);
        System.out.println("afterPosition: " + currentTetromino.getPosition().toString());// Debugging output to show the position after rotation
        return true;
    }

    /**
     * compare if a new position of tetromino isn't occupied, both are
     * represented as 2D boolean array tetromino position [column][row]
     *
     * @param flag which direction are the tetromino supposed to move
     * @return true if collision not happened
     */
    private boolean canMove(boolean[][] tetrominoGrid, Point position, DirectionFlag flag) {
        if (tetrominoGrid == null || flag == null) {
            return false;
        }
        Point newPosition = new Point(position.x + flag.getX(), position.y + flag.getY());
        if (!checkBoundaries(tetrominoGrid, newPosition)) {
            return false;
        }
        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (this.board[newPosition.y + gridR][newPosition.x + gridC] != BlockContent.EMPTY)) {
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
     * @return true if collision not happened and the position is in the
     * gameBoard boundaries
     */
    private boolean canRotate(boolean[][] tetrominoGrid) {
        if (tetrominoGrid == null || currentTetromino == null) {
            return false;
        }
        Point position = currentTetromino.getPosition();
        if (!checkBoundaries(tetrominoGrid, position)) {
            return false;
        }
        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (this.board[position.y + gridR][position.x + gridC] != BlockContent.EMPTY)) {
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
     * @param position the position of the tetromino after moving /
     * rotating(doesn't change)
     * @param flag which direction are the tetromino supposed to move/rotate
     * @return true if the final position is in the gameBoard boundaries
     */
    private boolean checkBoundaries(boolean[][] tetrominoGrid, Point position) {

        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (position.x + gridC < 0
                        || position.x + gridC >= board[0].length
                        || position.y + gridR >= board.length
                        || position.y + gridR < 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addBlockToBoard() {
        if (currentTetromino == null) {
            throw new IllegalStateException("No current tetromino to add to the board.");
        }
        int posCol = currentTetromino.getPosition().x;
        int posRow = currentTetromino.getPosition().y;
        for (int row = 0; row < currentTetromino.getGrid().length; row++) {
            for (int column = 0; column < currentTetromino.getGrid()[row].length; column++) {
                if (currentTetromino.getGrid()[row][column]) {
                    this.board[posRow + row][posCol + column] = BlockContent.fromColor(currentTetromino.getColor());
                }
            }
        }
    }
}
