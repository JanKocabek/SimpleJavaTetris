package org.sehes.tetris.model;

import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import org.sehes.tetris.config.GameParameters;

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

    private static System.Logger myLogger = System.getLogger(GameBoard.class.getName());

    private Tetromino currentTetromino;
    private final BlockContent[][] board;
    /*
     * make the start posiiton dynamic based on tetromino type instead of one fixed
     * position
     */
    private final Point startingPosition = new Point(GameParameters.SPAWN_POINT);// the position where new tetromino
    // will spawn column 4 row 0
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
     * The IBoardView interface allows other components of the game, such as the
     * GUI, to access the state of the board without being able to modify it
     * directly. This encapsulation ensures that all changes to the board state
     * are controlled through the GameBoard class, maintaining the integrity of
     * the game logic.
     *
     * @return the IBoardView instance representing the current state of the
     *         game board.
     */
    public IBoardView getBoardView() {
        return boardView;
    }

    public boolean trySetNewTetromino() {
        final Tetromino newTetromino = Tetromino.tetrominoFactory(startingPosition);
        if (isOutOfBoundaries(newTetromino.getStateCord(), startingPosition)) {
            return false;
        }
        if (isCollisionDetected(newTetromino.getStateCord(), newTetromino.getPosition())) {
            return false;
        }
        this.currentTetromino = newTetromino;
        return true;
    }

    public boolean tryMovePiece(final DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }
        if (canMove(currentTetromino.getStateCord(), currentTetromino.getPosition(), flag)) {
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
     *             ROTATE_R for right rotation, ROTATE_L for left rotation)
     */
    public boolean tryRotatePiece(final DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }
        final List<Point> rotatedPosition = currentTetromino.rotate(flag);
        if (!canRotate(rotatedPosition)) {
            return false;
        }
        currentTetromino.setState(rotatedPosition, flag);
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
        final List<Point> tetromino = currentTetromino.getStateCord();
        for (Point block : tetromino) {
            myLogger.log(INFO, () -> "Adding block X: " + block.x + ", Y: " + block.y + " to pos X: " + position.x
                                     + ",Y: " + position.y + " which would be: " + position.x + block.x + ", " + position.y + block.y);
            this.board[position.y + block.y][position.x + block.x] = BlockContent
                    .fromColor(currentTetromino.getColor());
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
     * @return true if there was at least one line cleared
     */
    public boolean checkAndClearLines() {
        boolean lineCleared = false;
        int linesClearedCount = 0;
        for (int row = 0; row < board.length; row++) {
            if (isLineFull(board[row])) {
                shiftLinesDown(row);
                linesClearedCount++;
                lineCleared = true;
                row--;//todo: in future make the counting of line and clearing of lines separated to prevent this
            }
        }
        if (lineCleared) {
            updateScore(linesClearedCount);
        }
        return lineCleared;
    }

    /**
     * Updates the score based on the number of lines cleared. The scoring
     * system is as follows: - 1 line cleared: 100 points - 2 lines cleared: 300
     * points - 3 lines cleared: 500 points - 4 lines cleared: 800 points
     */
    private void updateScore(int linesCleared) {
        switch (linesCleared) {
            case 1 -> score += 100;
            case 2 -> score += 300;
            case 3 -> score += 500;
            case 4 -> score += 800;
            default ->//shouldn't happen in current implementation
                    myLogger.log(WARNING, () -> "Invalid number of lines cleared: " + linesCleared);
        }
    }

    private boolean isLineFull(final BlockContent[] boardRow) {
        for (final BlockContent cell : boardRow) {
            if (cell == BlockContent.EMPTY) {
                return false;
            }
        }
        return true;
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
     * @param points   List of cordinates represented current tetromino
     * @param position the position of the tetromino before moving
     * @param flag     which direction are the tetromino supposed to move determines
     *                 the new position of the tetromino after moving
     * @return true if collision not happened and the position is in the
     *         gameBoard boundaries
     */
    private boolean canMove(final List<Point> points, final Point position, final DirectionFlag flag) {
        if (points == null || flag == null) {
            return false;
        }
        final Point newPosition = new Point(position.x + flag.getX(), position.y + flag.getY());
        if (isOutOfBoundaries(points, newPosition)) {
            return false;
        }
        return !isCollisionDetected(points, newPosition);
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
     * @param points      the tetromino represented as List of Points{x,y}
     * @param newPosition the new position of the tetromino after moving or
     *                    rotating, represented as a Point object with x and y
     *                    coordinates
     *                    corresponding to the column and row on the game board,
     *                    respectively
     * @return
     */
    private boolean isCollisionDetected(final List<Point> points, final Point newPosition) {

        for (final Point point : points) {
            if (this.board[newPosition.y + point.y][newPosition.x + point.x] != BlockContent.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method check if the position after rotating a piece is in the
     * gameBoardBoundaries and if sp then return true if dont colide with other
     * piece;
     *
     * @param points the list of cordination represent the tetromino state after
     *               expected rotation
     * @return true if original position is ok
     */
    private boolean canRotate(final List<Point> points) {
        if (points == null || currentTetromino == null) {
            return false;
        }
        final Point position = currentTetromino.getPosition();
        if (isOutOfBoundaries(points, position)) {
            return false;
        }
        return !isCollisionDetected(points, position);
    }

    /**
     * this method check if the position after moving a piece is out of board or
     * not
     *
     * @param points   the points of tetromino after move/rotation
     * @param position the destined position of the tetromino after moving /
     *                 rotating(doesn't change it) on the board
     * @return true if the final position is out of boundaries
     */
    private boolean isOutOfBoundaries(final List<Point> points, final Point position) {
        myLogger.log(INFO, () -> "Tetromino Points: " + points + ", Position: " + position);
        for (final Point point : points) {
            if (point.x + position.x < 0
                || point.y + position.y < 0
                || point.x + position.x >= board[0].length
                || point.y + position.y >= board.length) {
                myLogger.log(INFO, () -> "tetromino would be on position: " + (point.x + position.x) + ", "
                                         + (point.y + position.y));
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    private void shiftLinesDown(final int currentRow) {
        if (currentRow == 0) {
            return;
        }
        for (int cr = currentRow; cr > 0; cr--) {
            System.arraycopy(board[cr - 1], 0, board[cr], 0, board[cr].length);
        }
        Arrays.fill(board[0], BlockContent.EMPTY);
    }



    /*
     * JUNIT TEST ONLY methods
     * here is section of testing method for assuring the code correctness
     * use only protected modifier
     */

    /**
     * JUNIT TEST ONLY<br>
     * this method fill last line with blocks
     */
    void fillLineForTestOnly() {
        int lastLine = board.length - 1;
        Arrays.fill(board[lastLine], BlockContent.CYAN);
    }

    /**
     * JUNIT TEST ONLY<br>
     * this method set the current tetromino to the specific tetromino
     * @param tetromino the tetromino you need to spawn
     */
    void spawnTetrominoForTestOnly(final Tetromino tetromino) {
        this.currentTetromino = tetromino;
    }

}
