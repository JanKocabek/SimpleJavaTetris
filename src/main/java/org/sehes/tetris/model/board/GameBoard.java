package org.sehes.tetris.model.board;

import java.awt.Point;
import java.util.Arrays;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.Tetromino;

/**
 * The GameBoard class represents the game board in a Tetris game. It manages
 * the current tetromino, the state of the board, and provides methods for
 * moving, rotating, and adding tetrominoes to the board, as well as checking
 * for completed lines and clearing them. The class also provides a view of the
 * board through the IBoardView interface.
 * <p>
 * The GameBoard class is responsible for maintaining the game state and
 * ensuring that all operations on the board are valid according to the rules of
 * Tetris. It checks for collisions, boundaries, and line completions to provide
 * a seamless gaming experience.
 *
 * @author Sehes
 * @version 0.5
 * @see Tetromino
 * @see IBoardView
 */
public class GameBoard {

    private Tetromino currentTetromino;
    private final BlockContent[][] board;
    /*make the start posiiton dynamic based on tetromino type instead of one fixed position */
    private final Point startingPosition = new Point(GameParameters.SPAWN_POINT);//the position where new tetromino will spawn column 4 row 0
    private final IBoardView boardView;
    private int score;

    public GameBoard() {
        board = new BlockContent[GameParameters.ROWS][GameParameters.COLUMNS];
        score = 0;
        fillBoard();
        this.boardView = new IBoardView() {
            @Override
            public int getWidth() {
                return board[0].length;
            }

            @Override
            public int getHeight() {
                return board.length;
            }

            @Override
            public BlockContent getBlockContent(final int row, final int column) {
                if (row < 0 || row >= board.length || column < 0 || column >= board[row].length) {
                    throw new IndexOutOfBoundsException("Coordinates are out of bounds.");
                }
                return board[row][column];
            }
        };
    }

    public int getScore() {
        return score;
    }

    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * This method returns the IBoardView instance that provides a <b> read-only
     * view of the game board.</b> <br>
     * Dont use for the changes of the Board state or its components!!!<br>
     * The IBoardView interface allows other components of the game, such as the GUI, to access the state of the board without being able to modify it directly. This encapsulation ensures that all changes to the board state are controlled through the GameBoard class, maintaining the integrity of the game logic.
     * @return the IBoardView instance representing the current state of the game board.
     */
    public IBoardView getBoardView() {
        return boardView;
    }

    public boolean trySetNewTetromino() {
        final Tetromino newTetromino = Tetromino.tetrominoFactory(startingPosition);
        if (!checkBoundaries(newTetromino.getGrid(), startingPosition)) {
            return false;
        }
        if (isCollisionDetected(newTetromino.getGrid(), newTetromino.getPosition())) {
            return false;
        }
        this.currentTetromino = newTetromino;
        return true;
    }

    public boolean tryMovePiece(final DirectionFlag flag) {
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
    public boolean tryRotatePiece(final DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }
        final boolean[][] nextGrid = currentTetromino.rotate(flag);
        if (!canRotate(nextGrid)) {
            return false;
        }
        currentTetromino.setGrid(nextGrid);
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
        final Point position = currentTetromino.getPosition();
        for (int row = 0; row < currentTetromino.getGrid().length; row++) {
            for (int column = 0; column < currentTetromino.getGrid()[row].length; column++) {
                if (currentTetromino.getGrid()[row][column]) {
                    this.board[position.y + row][position.x + column] = BlockContent.fromColor(currentTetromino.getColor());
                }
            }
        }
    }

    /**
     * This method checks for completed lines on the game board and clears them
     * if found. It iterates through each row of the board and uses the
     * checkLine method to determine if a line is full (i.e., contains no EMPTY
     * blocks). If a full line is detected, it sets all blocks in that row to
     * EMPTY and shifts all rows above it down by one. The method returns true
     * if at least one line was cleared, allowing the game logic to update the
     * score.
     *
     * @return
     */
    public boolean checkAndClearLines() {
        boolean lineCleared = false;
        int linesClearedCount = 0;
        for (int row = 0; row < board.length; row++) {
            if (checkLine(board[row])) {
                lineCleared = true;
                shiftLinesDown(row);
                Arrays.fill(board[0], BlockContent.EMPTY);
                row--; // Check the same row again after shifting down
                linesClearedCount++;
            }
        }
        if (lineCleared) {
            updateScore(linesClearedCount);
        }
        return lineCleared;
    }

    private void updateScore(int linesCleared) {
        // Example scoring system: 100 points for 1 line, 300 for 2 lines, etc.
        switch (linesCleared) {
            case 1 ->
                score += 100;
            case 2 ->
                score += 300;
            case 3 ->
                score += 500;
            case 4 ->
                score += 800;
            default ->{
                System.err.println("Invalid number of lines cleared: " + linesCleared);
            }
        }
    }

    private boolean checkLine(final BlockContent[] boardRow) {
        boolean isLineFull = true;
        for (final BlockContent cell : boardRow) {
            if (cell == BlockContent.EMPTY) {
                isLineFull = false;
                break;
            }
        }
        return isLineFull;
    }

    private void fillBoard() {
        for (final BlockContent[] blockContents : board) {
            Arrays.fill(blockContents, BlockContent.EMPTY);
        }
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
    private boolean canMove(final boolean[][] tetrominoGrid, final Point position, final DirectionFlag flag) {
        if (tetrominoGrid == null || flag == null) {
            return false;
        }
        final Point newPosition = new Point(position.x + flag.getX(), position.y + flag.getY());
        if (!checkBoundaries(tetrominoGrid, newPosition)) {
            return false;
        }
        return !isCollisionDetected(tetrominoGrid, newPosition);
    }

    /**
     * this method checks if the new position of the tetromino after moving or
     * rotating is occupied by another piece on the game board. It iterates
     * through the grid of the tetromino and checks if any of the blocks in the
     * grid are true (indicating that there is a block in that position) and if
     * the corresponding position on the game board is not EMPTY. If such a
     * condition is found, it means that there is a collision, and the method
     * returns true. If no collisions are detected after checking all blocks in
     * the tetromino grid, the method returns false, indicating that the move or
     * rotation is valid and can be performed without overlapping another piece
     * on the board
     *
     * @param tetrominoGrid the grid of the tetromino after moving or rotating,
     * represented as a 2D boolean array where true indicates the presence of a
     * block in that position
     * @param newPosition the new position of the tetromino after moving or
     * rotating, represented as a Point object with x and y coordinates
     * corresponding to the column and row on the game board, respectively
     * @return
     */
    private boolean isCollisionDetected(final boolean[][] tetrominoGrid, final Point newPosition) {
        for (int gridR = 0; gridR < tetrominoGrid.length; gridR++) {
            for (int gridC = 0; gridC < tetrominoGrid[gridR].length; gridC++) {
                if ((tetrominoGrid[gridR][gridC])
                        && (this.board[newPosition.y + gridR][newPosition.x + gridC] != BlockContent.EMPTY)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * this method check if the position after rotating a piece is in the
     * gameBoardBoundaries and not occupied by another piece
     *
     * @param tetrominoGrid the grid of the tetromino after rotation
     * @return true if collision not happened and the position is in the
     * gameBoard boundaries
     */
    private boolean canRotate(final boolean[][] tetrominoGrid) {
        if (tetrominoGrid == null || currentTetromino == null) {
            return false;
        }
        final Point position = currentTetromino.getPosition();
        if (!checkBoundaries(tetrominoGrid, position)) {
            return false;
        }
        return !isCollisionDetected(tetrominoGrid, position);
    }

    /**
     * this method check if the position after moving a piece is in the
     * gameBoardBoundaries.
     *
     * @param tetrominoGrid the final grid of the tetromino after
     * moving/rotating
     * @param position the destined position of the tetromino after moving /
     * rotating(doesn't change it)
     * @return true if the final position is in the gameBoard boundaries
     */
    private boolean checkBoundaries(final boolean[][] tetrominoGrid, final Point position) {

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

    private void shiftLinesDown(final int currentRow) {
        for (int r = currentRow; r > 0; r--) {
            System.arraycopy(board[r - 1], 0, board[r], 0, board[r].length);
        }
    }

}
