package org.sehes.tetris.logic;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The GameBoard class represents the game board in a Tetris game. It manages
 * the state of the board, including the current Tetromino piece and the grid of
 * blocks. The class provides methods for moving and rotating the current
 * Tetromino, checking for collisions, and adding the Tetromino to the board
 * when it can no longer move. It also defines an inner enum BlockContent to
 * represent the different types of blocks on the board, each associated with a
 * specific color. The GameBoard interacts with other components of the game,
 * such as the GameManager and TetrisCanvas, to update the game state and render
 * the visual representation of the board.
 *
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
         * the color is not found in the map, it will return EMPTY.
         * @param color The Color object for which to find the corresponding
         * BlockContent enum value.
         * @return The BlockContent enum value corresponding to the provided
         * Color, or EMPTY value if the color is not found in the map. EMPTY means that the
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
    /*make the start posiiton dynamic based on tetromino type instead of one fixed position */
    private final Point startingPosition = new Point(GameParameters.SPAWN_POINT);//the position where new tetromino will spawn column 4 row 0

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
        boolean[][] nextGrid = currentTetromino.rotate(flag);
        if (!canRotate(nextGrid)) {
            return false;
        }
        currentTetromino.setGrid(nextGrid);
        return true;
    }

    /**
     * compare if a new position of tetromino grid isn't occupied, both are
     * represented as 2D boolean array tetromino position [column][row]
     *
     * @param tetrominoGrid the grid of the tetromino after moving
     * @param position the position of the tetromino before moving
     * @param flag which direction are the tetromino supposed to move determines
     * the new position of the tetromino after moving
     * @return true if collision not happened and the position is in the
     * gameBoard boundaries
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
     * @param tetrominoGrid the final grid of the tetromino after moving/rotating
     * @param position the destined position of the tetromino after moving /
     * rotating(doesn't change it)
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

    /**
     * this method is responsible for adding the current tetromino to the game
     * board when it can no longer move down. It iterates through the grid of
     * the current tetromino and updates the corresponding positions on the game
     * board with the appropriate BlockContent based on the color of the
     * tetromino. This effectively "locks" the tetromino in place on the board,
     * allowing the elimination of completed lines and the spawning of a new
     * tetromino to occur in subsequent game logic. It also checks if there is a
     * current tetromino to add before attempting to update the board, throwing
     * an IllegalStateException if there isn't one.
     */
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
