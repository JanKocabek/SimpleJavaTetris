package org.sehes.tetris.model;

import static java.lang.System.Logger.Level.WARNING;

import java.util.Arrays;
import java.util.List;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.ShapeProvider.WallKicks;
import org.sehes.tetris.model.ShapeProvider.WallKicks.WallKickType;

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
 * @see BoardView
 */
public class GameBoard {

    private static final System.Logger myLogger = System.getLogger(GameBoard.class.getName());
    private final TetrominoFactory factory = new TetrominoFactory();
    private Tetromino currentTetromino;
    private final BlockContent[][] board;
    /*
     * make the start posiiton dynamic based on tetromino type instead of one fixed
     * position
     */
    private final BoardView boardView;
    private int score;

    public GameBoard() {
        board = new BlockContent[GameParameters.ROWS][GameParameters.COLUMNS];
        score = 0;
        fillBoard();
        this.boardView = new BoardView() {
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
                    throw new IndexOutOfBoundsException("Coordinates are out of bounds. Row: " + row + ", Column: "
                                                        + column + ". Board size: " + board.length + "x" + board[0].length);
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
    public BoardView getBoardView() {
        return boardView;
    }

    public boolean trySetNewTetromino() {
        final Coordinate startingPosition = GameParameters.SPAWN_POINT;
        final Tetromino newTetromino = factory.createNewRandomTetromino(startingPosition);
        if (isOutOfBoundaries(newTetromino.getStateCord(), startingPosition.x(), startingPosition.y())) {
            return false;
        }
        if (isCollisionDetected(newTetromino.getStateCord(), startingPosition.x(), startingPosition.y())) {
            return false;
        }
        this.currentTetromino = newTetromino;
        return true;
    }

    public boolean tryMovePiece(final DirectionFlag flag) {
        if (this.currentTetromino == null) {
            return false;
        }
        if (canMove(currentTetromino, flag)) {
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
     * @param rotation The direction in which to rotate the tetromino (
     *                 {@code CLOCKWISE} for right rotation,
     *                 {@code COUNTER_CLOCKWISE}
     *                 for left rotation)
     */
    public boolean tryRotatePiece(final RotationFlag rotation) {
        if (this.currentTetromino == null || rotation == null || currentTetromino.getType() == TetrominoType.O) {
            return false;
        }
        currentTetromino.setNextOrientation(rotation);
        final List<Coordinate> rotatedPosition = currentTetromino.getNextState(rotation);
        if (!canRotate(rotatedPosition) && !tryWallKick(rotatedPosition)) {
            return false;
        }
        currentTetromino.setState(rotatedPosition, rotation);
        return true;
    }

    /**
     * This method is responsible for adding the current tetromino to the game
     * board when it can no longer move down. It iterates through the grid of
     * the current tetromino and updates the corresponding positions on the game
     * board with the appropriate BlockContent based on the color of the
     * tetromino. This effectively "locks" the tetromino in place on the board,
     * allowing the elimination of completed lines and the spawning of a new
     * tetromino to occur in subsequent game logic.
     */
    public void lockTetrominoInPlace() {
        if (currentTetromino == null) {
            throw new IllegalStateException("No current tetromino to add to the board.");
        }

        for (Coordinate coordinate : currentTetromino.getStateCord()) {
            final int x = currentTetromino.getPositionX() + coordinate.x();
            final int y = currentTetromino.getPositionY() + coordinate.y();
            this.board[y][x] = BlockContent.fromColor(currentTetromino.getColor());
        }
        currentTetromino = null;
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
                row--;// todo: in future make the counting of line and clearing of lines separated to
                // prevent this
            }
        }
        if (lineCleared) {
            updateScore(linesClearedCount);
        }
        return lineCleared;
    }


    /**
     * This method is responsible for attempting to wall kick the current tetromino
     * if it cannot be rotated into its next orientation without collision. It uses
     * the wall kick table to find the possible wall kicks that can be applied to
     * the current tetromino, and then checks each wall kick to see if it results in
     * a valid position for the tetromino. If a valid wall kick is found, it updates
     * the tetromino's position accordingly and returns true. If no valid wall kick
     * is found, it returns false.
     *
     * @param rotatedPosition the grid configuration of the tetromino after
     *                        rotation
     * @return true if a valid wall kick is found, false otherwise
     */
    private boolean tryWallKick(List<Coordinate> rotatedPosition) {
        WallKickType wallKickType = currentTetromino.getType() == TetrominoType.I ? WallKickType.I_KICKS
                : WallKickType.NORMAL;
        List<Coordinate> wallKicks = WallKicks.getWallKicks(wallKickType,
                currentTetromino.getCurrentOrientation().getTransitionTo(currentTetromino.getNextOrientation()));
        for (Coordinate cord : wallKicks) {
            int testX = currentTetromino.getPositionX() + cord.x();
            int testY = currentTetromino.getPositionY() + cord.y();
            if (!isOutOfBoundaries(rotatedPosition, testX, testY)
                && !isCollisionDetected(rotatedPosition, testX, testY)) {
                currentTetromino.setPosition(testX, testY);
                return true;
            }
        }
        return false;
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
            default -> // shouldn't happen in current implementation
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
     * Checks if a tetromino can move to a new position without colliding with
     * existing pieces on the game board.
     *
     * @param tetromino The tetromino to be moved
     * @param direction The direction in which to move the tetromino
     * @return {@code true} if the move is valid, {@code false} otherwise
     */
    private boolean canMove(final Tetromino tetromino, final DirectionFlag direction) {
        final int futureX = tetromino.getPositionX() + direction.getX();
        final int futureY = tetromino.getPositionY() + direction.getY();
        return !isOutOfBoundaries(tetromino.getStateCord(), futureX, futureY)
               && !isCollisionDetected(tetromino.getStateCord(), futureX, futureY);
    }

    /**
     * Checks if the position after moving or rotating a piece would collide with
     * any existing pieces on the game board.
     *
     * @param stateCord   The list of coordinates representing the tetromino's new
     *                    state
     * @param newPosition The expected new position of the tetromino pivot on the
     *                    board
     * @return {@code true} if there is a collision, {@code false} otherwise
     */
    private boolean isCollisionDetected(final List<Coordinate> stateCord, final int newPositionX,
                                        final int newPositionY) {

        for (final var cord : stateCord) {
            if (this.board[newPositionY + cord.y()][newPositionX + cord.x()] != BlockContent.EMPTY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the position after rotating a piece is within the game board
     * boundaries and does not collide with any other pieces.
     *
     * @param rotatedStateCord The list of coordinates representing the tetromino's
     *                         state after the expected rotation
     * @return {@code true} if the original position is valid, {@code false}
     *         otherwise
     */
    private boolean canRotate(final List<Coordinate> rotatedStateCord) {
        if (rotatedStateCord == null || currentTetromino == null) {
            return false;
        }

        return !isOutOfBoundaries(rotatedStateCord, currentTetromino.getPositionX(), currentTetromino.getPositionY()) &&
               !isCollisionDetected(rotatedStateCord, currentTetromino.getPositionX(),
                       currentTetromino.getPositionY());
    }

    /**
     * Checks if the position of the tetromino after a move or rotation is out of
     * the game board boundaries. *
     *
     * @param newStateCord The coordinates of the tetromino after the move or
     *                     rotation.
     * @param position     The position of the tetromino pivot on the board after
     *                     the move or rotation.
     * @return {@code true} if the final position is out of the board
     *         boundaries, {@code false} otherwise.
     */
    private boolean isOutOfBoundaries(List<Coordinate> newStateCord, int positionX, int positionY) {
        for (Coordinate cord : newStateCord) {
            int newX = cord.x() + positionX;
            int newY = cord.y() + positionY;
            if (newX < 0 || newY < 0 || newX >= board[0].length || newY >= board.length) {
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

    /*below are JUNIT TEST ONLY methods
    after future decoupling logic from state they need to be transfer out of this class to don't pollute
    production code*/

    /**
     * JUNIT TEST ONLY<br>
     * this method set the current tetromino to the specific tetromino
     *
     * @param tetromino the tetromino you need to spawn
     */

    void spawnTetrominoForTestOnly(final Tetromino tetromino) {
        this.currentTetromino = tetromino;
    }

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
     * This method sets a specific {@link BlockContent} to a specific position on the game board.
     * This method is used for testing purposes to fill the game board with {@link BlockContent}s.
     *
     * @param row the row of the block to be set
     * @param column the column of the block to be set
     * @param blockContent the {@link BlockContent} to be set
     */
    void fillBlockForTestOnly(final int row, final int column, final BlockContent blockContent) {
        board[row][column] = blockContent;
    }


}
