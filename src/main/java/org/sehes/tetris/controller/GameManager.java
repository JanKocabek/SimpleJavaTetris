package org.sehes.tetris.controller;

import org.sehes.tetris.gui.GuiFactory;
import org.sehes.tetris.gui.InfoPanel;
import org.sehes.tetris.gui.ScorePanel;
import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.GameBoard;
import org.sehes.tetris.model.RotationFlag;
import org.sehes.tetris.model.Tetromino;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.sehes.tetris.controller.GameState.GAME_OVER;
import static org.sehes.tetris.controller.GameState.INIT;
import static org.sehes.tetris.controller.GameState.PAUSED;
import static org.sehes.tetris.controller.GameState.PLAYING;
import static org.sehes.tetris.controller.GameState.PREPARED;

/**
 * The GameManager class is responsible for managing the overall game state,
 * handling user input, and coordinating the game loop. It initializes the game
 * components, starts the game loop, and provides methods for moving and
 * rotating pieces, as well as pausing and resuming the game.
 */

public class GameManager implements InputReceiver {

    //game constants
    private static final int FPS = 60;
    private static final int FRAME_TIME_MS = 1000 / FPS;
    private static final int BASE_SPEED_MS = 600;
    private static final int SOFT_LOCK_TIMER_MS = 500;

    private final State currentState = new State(INIT);// Current state of the game
    private final AtomicBoolean isDirty = new AtomicBoolean(false);
    private final long movementSpeed = TimeUnit.MILLISECONDS.toNanos(BASE_SPEED_MS);
    private final long softLockTimer = TimeUnit.MILLISECONDS.toNanos(SOFT_LOCK_TIMER_MS);

    private TetrisCanvas tetrisCanvas; // Reference to the canvas for repainting
    private GameBoard gameBoard; // reference to the game board for managing game logic
    private Timer gameLoopTimer; // Timer for the main game loop to control the game speed
    private ScorePanel scoreUI;// Reference to the score UI for updating the score display
    private InfoPanel infoP;// Reference to the info panel for updating game state messages
    // Loop Time vars
    private long prevTime;
    private long gravityAccumulator;
    // FPS vars
    private int frameCount = 0;
    private long fpsTimer = 0;

    private Runnable appExitMethod;
    private boolean isLockingMode = false;
    private int lockMoves = 0;
    private MainLoopListener gameLoopListener;

    public GameManager() {
        // todo: decide what to put here probably from prepareGame method or if it
        // worthy delete it.

    }

    /**
     * Starts the Tetris application by initializing the game state, creating
     * game loop timer, and setting up the game window. The game loop timer is
     * configured to trigger the main game loop at a fixed interval defined by
     * GameParameters.GAME_SPEED.
     */
    public void prepareGame(GuiFactory.WholeGui gui) {
        if (currentState.get() == INIT) {
            this.tetrisCanvas = gui.canvas();
            this.scoreUI = gui.scoreUI();
            this.infoP = gui.infoP();
            gameLoopListener = new MainLoopListener();
            gameLoopTimer = new Timer(FRAME_TIME_MS, gameLoopListener);
            currentState.set(PREPARED);
            appExitMethod = gui.exitAction();
        }
    }

    public GameState getGameState() {
        return currentState.get();
    }

    public BoardView getBoardView() {
        return gameBoard.getBoardView();
    }

    public Tetromino getCurrentTetromino() {
        return gameBoard.getCurrentTetromino();
    }

    @Override
    public void handleInput(InputAction action) {
        switch (getGameState()) {
            case PREPARED -> preparedInput(action);
            case PLAYING -> runningGameInput(action);
            case PAUSED -> pauseGameInput(action);
            case GAME_OVER -> gameOverInput(action);
            default -> {
                // do nothing
            }

        }

    }

    private void gameOverInput(InputAction action) {
        switch (action) {
            case CONFIRM -> startNewGame();
            case CANCEL -> exitGame();
            default -> {
                // do nothing
            }
        }
    }

    private void pauseGameInput(InputAction action) {
        switch (action) {
            case CANCEL -> exitGame();
            case CONFIRM -> resumeGame();
            default -> {
                // do nothing
            }
        }
    }

    private void runningGameInput(InputAction action) {
        switch (action) {
            case CANCEL -> exitGame();
            case CONFIRM -> pauseGame();
            case MOVE_DOWN -> movePiece(DirectionFlag.DOWN);
            case HARD_DROP -> hardDrop();
            case MOVE_LEFT -> movePiece(DirectionFlag.LEFT);
            case MOVE_RIGHT -> movePiece(DirectionFlag.RIGHT);
            case ROTATE_CW -> rotatePiece(RotationFlag.CLOCKWISE);
            case ROTATE_CCW -> rotatePiece(RotationFlag.COUNTER_CLOCKWISE);
            default -> {
               //do nothing
            }
        }
    }

    private void preparedInput(InputAction action) {
        switch (action) {
            case CONFIRM -> startNewGame();
            case CANCEL -> exitGame();
            default -> {
             //do nothing
            }
        }
    }

    private void hardDrop() {
        if (currentState.get() != PLAYING) return;
        final var tetromino = getCurrentTetromino();
        final int hardDropDistance = gameBoard.calculateDropDistance();
        tetromino.setPosition(tetromino.getPositionX(), tetromino.getPositionY() + hardDropDistance);
        lock();
        tetrisCanvas.render(gameSnapshotFactory(currentState));
    }

    /**
     * Try to move the current piece in the specified direction. If the move is
     * successful, repaint the canvas to reflect the new position of the piece.
     *
     * @param direction The direction to move the piece (e.g., LEFT, RIGHT,
     *                  DOWN).
     */
    private void movePiece(final DirectionFlag direction) {
        if (currentState.get() == PLAYING && gameBoard.tryMovePiece(direction)) {
            tetrisCanvas.render(gameSnapshotFactory(currentState));
            if (isLockingMode) {
                gameLoopListener.updateDelay();

            }
        }
    }

    /**
     * Try to rotate the current piece in the specified direction. If the
     * rotation is successful, repaint the canvas to reflect the new orientation
     * of the piece.
     *
     * @param rotate The direction to rotate the piece (CLOCKWISE, COUN
     */
    private void rotatePiece(final RotationFlag rotate) {
        if (currentState.get() == PLAYING && gameBoard.tryRotatePiece(rotate)) {
            tetrisCanvas.render(gameSnapshotFactory(currentState));
            if (isLockingMode) {
                gameLoopListener.updateDelay();
            }
        }
    }


    private boolean tryPrepareNewPiece() {
        if (gameBoard.trySetNewTetromino()) {
            gameLoopListener.turnOffLockMode();
            return true;
        }
        return false;
    }

    private void pauseGame() {
        if (currentState.get() == PLAYING) {
            gameLoopTimer.stop();
            currentState.set(PAUSED);
        }
    }

    private void resumeGame() {
        if (currentState.get() == PAUSED) {
            resetTime();
            gameLoopTimer.start();
            currentState.set(PLAYING);
        }
    }

    /**
     * Starts the game by resetting the game board and starting the game loop
     * timer. Sets the game state to PLAYING. This method can only be called if
     * the game is in the INITIALIZE or GAME_OVER state to prevent starting a
     * new game while one is already in progress.
     */
    private void startNewGame() {
        switch (currentState.get()) {
            case PREPARED -> {
                newGame();
                gameLoopTimer.start();
            }
            case GAME_OVER -> {
                scoreUI.resetScore();
                newGame();
                gameLoopTimer.restart();
            }
            default -> {
                // Do nothing
            }
        }
    }

    private void exitGame() {
        appExitMethod.run();
    }

    private void resetTime() {
        prevTime = System.nanoTime();
        gravityAccumulator = 0;
        frameCount = 0;
        fpsTimer = 0;
    }

    private void newGame() {
        isDirty.set(true);
        gameBoard = new GameBoard();
        currentState.set(PLAYING);
        if (tryPrepareNewPiece()) {
            tetrisCanvas.render(gameSnapshotFactory(currentState));
            resetTime();
        } else {
            setGameOver();
        }
    }

    private void setGameOver() {
        gameLoopTimer.stop();
        currentState.set(GAME_OVER);
    }

    private GameSnapshot gameSnapshotFactory(State state) {
        final var wasDirty = this.isDirty.getAndSet(false);
        return (state.get()) == GameState.PLAYING ? new GameSnapshot(getBoardView(), Optional.of(getCurrentTetromino()), wasDirty) : new GameSnapshot(getBoardView(), Optional.empty(), wasDirty);
    }

    private void lock() {
        gameBoard.lockTetrominoInPlace();
        isDirty.set(true);
        gameBoard.checkAndClearLines();
        scoreUI.updateScore(gameBoard.getScore());
        if (!tryPrepareNewPiece()) {
            setGameOver();
        }
        gravityAccumulator = 0;
        lockMoves = 0;
    }

    /**
     * The Main game loop listener that is triggered by the game loop timer. It
     * attempts to move the current piece down. If the piece cannot move down,
     * it adds the piece to the board, checks for and clears any completed
     * lines, updates the score, and tries to set a new piece. If a new piece
     * cannot be set, it means the game is over, so it updates the game state
     * and stops the game loop timer. After processing the game logic, it
     * repaints the canvas to reflect any changes in the game state.
     */
    private class MainLoopListener implements ActionListener {
        private static final int MAX_LOCK_MOVES = 15;
        private long delayLockAccumulator = 0;


        @Override
        public void actionPerformed(final ActionEvent e) {
            long currentTime = System.nanoTime();
            if (prevTime == 0) {
                prevTime = currentTime;// Safety guard in case this is invoked before newGame() initializes timing
                // state. is it ever needed?
                return;
            }

            long elapsedTime = currentTime - prevTime;
            prevTime = currentTime;

            fpsCalculation(elapsedTime);

            if (!isLockingMode) {
                handlePieceMovement(elapsedTime);
            } else {
                handleLockDelay(elapsedTime);
            }
            tetrisCanvas.render(gameSnapshotFactory(currentState));
        }

        private void handlePieceMovement(long elapsedTime) {
            gravityAccumulator += elapsedTime;
            while (gravityAccumulator >= movementSpeed) {
                if (gameBoard.tryMovePiece(DirectionFlag.DOWN)) {
                    gravityAccumulator -= movementSpeed;
                } else {
                    gravityAccumulator = 0;
                    isLockingMode = true;
                    break;
                }
            }

        }


        private void handleLockDelay(long elapsedTime) {
            delayLockAccumulator += elapsedTime;
            if (delayLockAccumulator >= softLockTimer || lockMoves == MAX_LOCK_MOVES) {
                System.out.println("actual moves: " + lockMoves);
                turnOffLockMode();
                lock();
            } else if (gameBoard.canMove(gameBoard.getCurrentTetromino(), DirectionFlag.DOWN)) {
                isLockingMode = false;
            }
        }

        public void updateDelay() {
            if (lockMoves == MAX_LOCK_MOVES) return;
            delayLockAccumulator = 0;
            lockMoves++;
            System.out.println("Lock moves: " + lockMoves);
        }

        public void turnOffLockMode() {
            isLockingMode = false;
            delayLockAccumulator = 0;
            lockMoves = 0;
        }

        private void fpsCalculation(long elapsedTime) {
            frameCount++;
            fpsTimer += elapsedTime;

            if (fpsTimer >= TimeUnit.SECONDS.toNanos(1)) {
                int currentFPS = frameCount; // This is your actual FPS for the last second
                frameCount = 0;
                fpsTimer = 0;
                infoP.updateFPS(currentFPS);
            }
        }
    }

    private final class State {
        private GameState gameState;

        private State(final GameState gameState) {
            this.gameState = gameState;
        }

        public GameState get() {
            return gameState;
        }

        private void set(final GameState gameState) {
            this.gameState = gameState;
            updateInfo();
        }

        private void updateInfo() {
            if (infoP != null) infoP.updateLabelText(this.gameState);
        }
    }
}

