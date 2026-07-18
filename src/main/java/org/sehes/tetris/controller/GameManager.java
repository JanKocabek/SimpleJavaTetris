package org.sehes.tetris.controller;

import org.sehes.tetris.controller.input.InputAction;
import org.sehes.tetris.gui.GuiFactory;
import org.sehes.tetris.gui.ScorePanel;
import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.sehes.tetris.controller.GameState.*;


/**
 * The GameManager class is responsible for managing the overall game state,
 * handling user input, and coordinating the game loop. It initializes the game
 * components, starts the game loop, and provides methods for moving and
 * rotating pieces, as well as pausing and resuming the game.
 */


public class GameManager implements InputHandler {


    private static final int FPS = 60;
    private static final int FRAME_TIME_MS = 1000 / FPS;
    private static final int BASE_SPEED = 600;
    private final StateManager<GameState> stateManager;
    // is full redraw needed?
    private final AtomicBoolean isDirty = new AtomicBoolean(false);
    private final long movementSpeed = TimeUnit.MILLISECONDS.toNanos(BASE_SPEED);
    private final MainLoopListener gameLoop;
    private TetrisCanvas tetrisCanvas; // Reference to the canvas for repainting
    private GameBoard gameBoard; // reference to the game board for managing game logic
    private Timer gameLoopTimer; // Timer for the main game loop to control the game speed
    private ScorePanel scoreUI;// Reference to the score UI for updating the score display
    // Loop Time vars
    private long prevTime;
    private long gravityAccumulator;
    // FPS vars
    private int frameCount = 0;
    private long fpsTimer = 0;
    private Runnable gameExit = () -> System.exit(0);


    public GameManager(StateManager<GameState> stateManager) {
        this.stateManager = stateManager;
        this.gameLoop = new MainLoopListener();
    }

    /**
     * Starts the Tetris application by initializing the game state, creating
     * game loop timer, and setting up the game window. The game loop timer is
     * configured to trigger the main game loop at a fixed interval defined by
     * GameParameters.GAME_SPEED.
     */
    public void prepareGame(GuiFactory.WholeGui gui) {
        if (stateManager.getState() == INIT) {
            this.tetrisCanvas = gui.canvas();
            this.scoreUI = gui.scoreUI();
            gameLoopTimer = new Timer(FRAME_TIME_MS, gameLoop);
            gameExit = gui.exitAction();
            stateManager.setState(PREPARED);
        }

    }


    public Observable<Integer> fpsObservable() {
        return gameLoop;
    }

    public BoardView getBoardView() {
        return gameBoard.getBoardView();
    }

    public Tetromino getCurrentTetromino() {
        return gameBoard.getCurrentTetromino();
    }


    @Override
    public void handleInput(InputAction action) {
        switch (stateManager.getState()) {
            case PREPARED -> preparedInput(action);
            case PLAYING -> runningGameInput(action);
            case PAUSED -> pauseGameInput(action);
            case GAME_OVER -> gameOverInput(action);
            default -> {
                break;
            }

        }

    }

    private void gameOverInput(InputAction action) {
        switch (action) {
            case CONFIRM -> startGame();
            case CANCEL -> exitGame();
            default -> {
                break;
            }
        }
    }

    private void pauseGameInput(InputAction action) {
        switch (action) {
            case CANCEL -> exitGame();
            case CONFIRM -> resumeGame();
            default -> {
                break;
            }
        }
    }

    private void runningGameInput(InputAction action) {
        switch (action) {
            case CANCEL -> exitGame();
            case CONFIRM -> pauseGame();
            case MOVE_DOWN -> movePiece(DirectionFlag.DOWN);
            //  todo: will add in new branch case HARD_DROP -> movePiece(DirectionFlag.DOWN);
            case MOVE_LEFT -> movePiece(DirectionFlag.LEFT);
            case MOVE_RIGHT -> movePiece(DirectionFlag.RIGHT);
            case ROTATE_CW -> rotatePiece(RotationFlag.CLOCKWISE);
            case ROTATE_CCW -> rotatePiece(RotationFlag.COUNTER_CLOCKWISE);
            default -> {
                break;
            }
        }
    }

    private void preparedInput(InputAction action) {
        switch (action) {
            case CONFIRM -> startGame();
            case CANCEL -> exitGame();
            default -> {
                break;
            }
        }
    }

    /**
     * Try to move the current piece in the specified direction. If the move is
     * successful, repaint the canvas to reflect the new position of the piece.
     *
     * @param direction The direction to move the piece (e.g., LEFT, RIGHT,
     *                  DOWN).
     */
    private void movePiece(final DirectionFlag direction) {
        if (stateManager.getState() != PLAYING) {
            return;
        }
        if (gameBoard.tryMovePiece(direction)) {
            tetrisCanvas.render(gameSnapshotFactory(stateManager.getState()));
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
        if (stateManager.getState() != PLAYING) {
            return;
        }
        if (gameBoard.tryRotatePiece(rotate)) {
            tetrisCanvas.render(gameSnapshotFactory(stateManager.getState()));
        }
    }

    private void pauseGame() {
        if (stateManager.getState() == PLAYING) {
            gameLoopTimer.stop();
            stateManager.setState(PAUSED);
        }
    }

    private void resumeGame() {
        if (stateManager.getState() == PAUSED) {
            resetTime();
            gameLoopTimer.start();
            stateManager.setState(PLAYING);
        }
    }

    /**
     * Starts the game by resetting the game board and starting the game loop
     * timer. Sets the game state to PLAYING. This method can only be called if
     * the game is in the INITIALIZE or GAME_OVER state to prevent starting a
     * new game while one is already in progress.
     */
    private void startGame() {
        switch (stateManager.getState()) {
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
        gameExit.run();
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
        stateManager.setState(PLAYING);
        if (!gameBoard.trySetNewTetromino()) {
            setGameOver();
            return;
        }
        tetrisCanvas.render(gameSnapshotFactory(stateManager.getState()));
        resetTime();
    }

    private void setGameOver() {
        gameLoopTimer.stop();
        stateManager.setState(GAME_OVER);
    }

    private GameSnapshot gameSnapshotFactory(GameState state) {
        final var wasDirty = this.isDirty.getAndSet(false);
        return (state) == GameState.PLAYING ? new GameSnapshot(getBoardView(), Optional.of(getCurrentTetromino()), wasDirty) : new GameSnapshot(getBoardView(), Optional.empty(), wasDirty);
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
    private class MainLoopListener implements ActionListener, Observable<Integer> {
        private final List<Observer<Integer>> observers = new CopyOnWriteArrayList<>();
        private int currentFPS=0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            long currentTime = System.nanoTime();
            if (prevTime == 0) {
                prevTime = currentTime;// Safety guard in case this is invoked before newGame() initializes timing state. is it ever needed?
                return;
            }

            var elapsedTime = currentTime - prevTime;
            prevTime = currentTime;

            fpsCalculation(elapsedTime);

            gravityAccumulator += elapsedTime;
            while (gravityAccumulator >= movementSpeed) {
                if (!gameBoard.tryMovePiece(DirectionFlag.DOWN)) {

                    gameBoard.lockTetrominoInPlace();
                    isDirty.set(true);
                    gameBoard.checkAndClearLines();
                    scoreUI.updateScore(gameBoard.getScore());
                    if (!gameBoard.trySetNewTetromino()) {
                        setGameOver();
                    }
                    gravityAccumulator = 0;
                    break;
                }
                gravityAccumulator -= movementSpeed;
            }

            tetrisCanvas.render(gameSnapshotFactory(stateManager.getState()));
        }

        private void fpsCalculation(long elapsedTime) {
            frameCount++;
            fpsTimer += elapsedTime;

            if (fpsTimer >= TimeUnit.SECONDS.toNanos(1)) {
                currentFPS = frameCount; // This is your actual FPS for the last second
                frameCount = 0;
                fpsTimer = 0;
                notifyObservers();
            }
        }

        @Override
        public void addObserver(final Observer<Integer> observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(final Observer<Integer> observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyObservers() {
            observers.forEach(o -> o.update(currentFPS));
        }
    }
}