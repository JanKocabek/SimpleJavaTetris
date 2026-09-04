package org.sehes.tetris.controller;

import org.sehes.tetris.config.GhostType;
import org.sehes.tetris.controller.input.InputAction;
import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.GameBoard;
import org.sehes.tetris.model.PieceGenerator;
import org.sehes.tetris.model.RotationFlag;
import org.sehes.tetris.model.Tetromino;
import org.sehes.tetris.model.TetrominoType;
import org.sehes.tetris.model.score.HardDropEvent;
import org.sehes.tetris.model.score.LockPieceEvent;
import org.sehes.tetris.model.score.SoftDropEvent;
import org.sehes.tetris.model.score.TSpin;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.sehes.tetris.controller.GameState.GAME_OVER;
import static org.sehes.tetris.controller.GameState.INIT;
import static org.sehes.tetris.controller.GameState.NEW_GAME;
import static org.sehes.tetris.controller.GameState.PAUSED;
import static org.sehes.tetris.controller.GameState.PLAYING;
import static org.sehes.tetris.controller.GameState.PREPARED;


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
    private final ObservableImpl<TetrominoType> spawnObservable = new ObservableImpl<>();
    private final ObservableImpl<TetrominoType> holdObservable = new ObservableImpl<>();

    private final StateManager<GameState> stateManager;
    private final PieceGenerator generator;
    private final AtomicBoolean isDirty = new AtomicBoolean(false); // is full redraw needed?
    private final long movementSpeed = TimeUnit.MILLISECONDS.toNanos(BASE_SPEED);
    private final MainLoopListener gameLoop = new MainLoopListener();
    private final ScoreMessenger scoreMessenger;
    private Rendering tetrisCanvas; // Reference to the canvas for repainting
    private GameBoard gameBoard; // reference to the game board for managing game logic
    private Timer gameLoopTimer; // Timer for the main game loop to control the game speed
    // Loop Time vars
    private long prevTime;
    private long gravityAccumulator;
    // FPS vars
    private int frameCount = 0;
    private long fpsTimer = 0;
    private Runnable gameExit = () -> System.exit(0);
    private GhostType ghostType;
    private TetrominoType holdTetromino;
    private boolean isHoldLock = false;

    public GameManager(StateManager<GameState> stateManager, ScoreMessenger scoreMessenger, PieceGenerator generator) {
        this.generator = generator;
        this.ghostType = GhostType.FULL;
        this.stateManager = stateManager;
        this.scoreMessenger = scoreMessenger;
        this.holdTetromino = null;
    }

    /**
     * Starts the Tetris application by initializing the game state, creating
     * game loop timer, and setting up the game window. The game loop timer is
     * configured to trigger the main game loop at a fixed interval defined by
     * GameParameters.GAME_SPEED.
     */
    public void prepareGame(Rendering canvas, Runnable exitAction) {
        if (stateManager.getState() == INIT) {
            this.tetrisCanvas = canvas;
            gameLoopTimer = new Timer(FRAME_TIME_MS, gameLoop);
            gameExit = exitAction;
            stateManager.setState(PREPARED);
        }
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

    public Observable<TetrominoType> spawnObservable() {
        return spawnObservable;
    }

    public Observable<TetrominoType> holdObservable() {
        return holdObservable;
    }

    public Observable<Integer> fpsObservable() {
        return gameLoop;
    }

    private BoardView getBoardView() {
        return gameBoard.getBoardView();
    }

    private Tetromino getCurrentTetromino() {
        return gameBoard.getCurrentTetromino();
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
            case MOVE_DOWN -> softDrop();
            case HARD_DROP -> hardDrop();
            case MOVE_LEFT -> movePiece(DirectionFlag.LEFT);
            case MOVE_RIGHT -> movePiece(DirectionFlag.RIGHT);
            case ROTATE_CW -> rotatePiece(RotationFlag.CLOCKWISE);
            case ROTATE_CCW -> rotatePiece(RotationFlag.COUNTER_CLOCKWISE);
            case TOGGLE_GHOST -> toggleGhostPiece();
            case HOLD -> holdOrSwap();
            default -> {
                break;
            }
        }
    }

    private void holdOrSwap() {
        if (isHoldLock) return;

        TetrominoType currentType = getCurrentTetromino().getType();
        TetrominoType previousHold = holdTetromino;
        setHoldAndNotify(currentType);
        isHoldLock = true;

        boolean spawnSuccessful = (previousHold == null)
                ? trySpawnNewTetromino()
                : trySpawnMino(previousHold);

        if (!spawnSuccessful) {
            setGameOver();
        }
        render();
    }

    private boolean trySpawnMino(TetrominoType previousHold) {
        return gameBoard.trySetNewTetromino(previousHold);
    }


    private void setHoldAndNotify(TetrominoType currentType) {
        holdTetromino = currentType;
        holdObservable().notifyObservers(holdTetromino);
    }

    /**
     * call new repainting on mainCanvas
     */
    private void render() {
        tetrisCanvas.render(createGameSnapshot());
    }

    private void toggleGhostPiece() {
        ghostType = ghostType.next();
        render();
    }

    private void hardDrop() {
        final var distance = gameBoard.tryHardDrop();
        if (distance > 0) {
            scoreMessenger.notifyObservers(new HardDropEvent(distance));
            render();
        }
        lockClearAndScorePiece();
        isDirty.set(true);
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
        if (gameBoard.tryMovePiece(direction)) {
            render();
        }
    }

    private void softDrop() {
        if (gameBoard.trySoftDrop()) {
            scoreMessenger.notifyObservers(new SoftDropEvent(1));
            render();
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
        if (gameBoard.tryRotatePiece(rotate)) {
            render();
        }
    }

    private void pauseGame() {
        gameLoopTimer.stop();
        stateManager.setState(PAUSED);
    }

    private void resumeGame() {
        resetTime();
        gameLoopTimer.start();
        stateManager.setState(PLAYING);
    }

    /**
     * Starts the game by resetting the game board and starting the game loop
     * timer. Sets the game state to PLAYING. This method can only be called if
     * the game is in the INITIALIZE or GAME_OVER state to prevent starting a
     * new game while one is already in progress.
     */
    //ToDo: This method could maybe be split into two methods and remove unnecessary switch
    private void startGame() {
        switch (stateManager.getState()) {
            case PREPARED -> {
                newGame();
                gameLoopTimer.start();
            }
            case GAME_OVER -> {
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
        setHoldAndNotify(null);
        isHoldLock = false;
        stateManager.setState(NEW_GAME);
        isDirty.set(true);
        gameBoard = new GameBoard();
        spawnObservable.notifyObservers(generator.peekNext());
        if (spawnMinoOrGameOver()) {
            render();
            resetTime();
            stateManager.setState(PLAYING);
        }
    }

    private void setGameOver() {
        gameLoopTimer.stop();
        stateManager.setState(GAME_OVER);
    }

    private GameSnapshot createGameSnapshot() {
        final var wasDirty = isDirty.getAndSet(false);
        Tetromino current = getCurrentTetromino();
        return new GameSnapshot(getBoardView(), Optional.ofNullable(current), wasDirty, current == null ? 0 : gameBoard.calculateDropDistance(), current == null ? GhostType.NONE : ghostType);
    }

    private boolean trySpawnNewTetromino() {
        final var piece = generator.getNextPiece();
        if (trySpawnMino(piece)) {
            spawnObservable.notifyObservers(generator.peekNext());
            return true;
        }
        return false;
    }

    private void lockClearAndScorePiece() {
        isHoldLock = false;
        gameBoard.lockTetrominoInPlace();
        gameBoard.clearLines();
        final var lastAction = gameBoard.getLastAction();
        final LockPieceEvent lockEvent = createLockEvent(lastAction.tSpin(), lastAction.linesCleared());
        scoreMessenger.notifyObservers(lockEvent);
        spawnMinoOrGameOver();
        gravityAccumulator = 0;
    }

    private boolean spawnMinoOrGameOver() {
        if (trySpawnNewTetromino()) return true;
        setGameOver();
        return false;
    }

    private LockPieceEvent createLockEvent(final TSpin tSpin, int clearedLines) {
        return new LockPieceEvent(clearedLines, tSpin);
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
                if (!gameBoard.tryGravityMove()) {
                    //todo: this will in future update here replaced by delayLock mechanism (soft drop) after GameLoop is made own class and Gui/swing-agnostic!
                    lockClearAndScorePiece();
                    isDirty.set(true);
                    break;//break the while loop
                }
                gravityAccumulator -= movementSpeed;
            }

            render();
        }

        private void fpsCalculation(long elapsedTime) {
            int currentFPS;
            frameCount++;
            fpsTimer += elapsedTime;

            if (fpsTimer >= TimeUnit.SECONDS.toNanos(1)) {
                currentFPS = frameCount; // This is your actual FPS for the last second
                frameCount = 0;
                fpsTimer = 0;
                notifyObservers(currentFPS);
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
        public void notifyObservers(Integer event) {
            observers.forEach(o -> o.update(event));
        }
    }


}