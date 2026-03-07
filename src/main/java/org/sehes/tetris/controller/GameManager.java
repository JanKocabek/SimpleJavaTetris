package org.sehes.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.sehes.tetris.gui.GameWindow;
import org.sehes.tetris.gui.GuiFactory;
import org.sehes.tetris.gui.InfoPanel;
import org.sehes.tetris.gui.ScorePanel;
import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.gui.TetrisDrawingHandler;
import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.GameBoard;
import org.sehes.tetris.model.IBoardView;
import org.sehes.tetris.model.Tetromino;

/**
 * The GameManager class is responsible for managing the overall game state,
 * handling user input, and coordinating the game loop. It initializes the game
 * components, starts the game loop, and provides methods for moving and
 * rotating pieces, as well as pausing and resuming the game.
 */
public class GameManager {

    // Define the possible game states
    public enum GameState {
        INIT, PREPARED, PLAYING, PAUSED, GAME_OVER
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

        @Override
        public void actionPerformed(final ActionEvent e) {
            var currentTime = System.nanoTime();
            if (prevTime == 0) {
                prevTime = currentTime;// Safety guard in case this is invoked before newGame() initializes timing
                                       // state
                return;
            }

            var elapsedTime = currentTime - prevTime;
            prevTime = currentTime;

            // --- FPS Logic Start ---
            frameCount++;
            fpsTimer += elapsedTime;

            if (fpsTimer >= TimeUnit.SECONDS.toNanos(1)) {
                currentFPS = frameCount; // This is your actual FPS for the last second
                frameCount = 0;
                fpsTimer = 0;
                infoP.updateFPS(currentFPS);
            }
            // --- FPS Logic End ---

            gravityAccumulator += elapsedTime;
            while (gravityAccumulator >= movementSpeed) {
                if (!gameBoard.tryMovePiece(DirectionFlag.DOWN)) {

                    gameBoard.addBlockToBoard();
                    isDirty = true;
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

            tetrisCanvas.repaintCanvas(isDirty);
            isDirty = false;
        }
    }

    private static final int FPS = 60;
    private static final int FRAME_TIME_MS = 1000 / FPS;
    private static final int BASE_SPEED = 600;
    private GameState gameState;// Current state of the game
    private TetrisCanvas tetrisCanvas; // Reference to the canvas for repainting
    private GameBoard gameBoard; // reference to the game board for managing game logic
    private Timer gameLoopTimer; // Timer for the main game loop to control the game speed
    private ScorePanel scoreUI;// Reference to the score UI for updating the score display
    private InfoPanel infoP;// Reference to the info panel for updating game state messages
    private TetrisDrawingHandler tetrisDrawingHandler; // only for telling the handler what to redraw and if it needs to
                                                       // redraw
    private boolean isDirty = false;
    // Loop Time vars
    private long prevTime;
    private long gravityAccumulator;
    private long movementSpeed = TimeUnit.MILLISECONDS.toNanos(BASE_SPEED);
    // FPS vars
    private int frameCount = 0;
    private long fpsTimer = 0;
    private int currentFPS = 0;

    /**
     * Starts the Tetris application by initializing the game state, creating
     * game loop timer, and setting up the game window. The game loop timer is
     * configured to trigger the main game loop at a fixed interval defined by
     * GameParameters.GAME_SPEED. The game window is created on the Event
     * Dispatch Thread (EDT) to ensure thread safety when interacting with Swing
     * components. The constructor initializes the game state to INIT and
     * sets up the necessary components for the game, including the canvas and
     * score UI, which will be used in the main game loop to update the display
     * and score as the game progresses.
     *
     */
    public GameManager() {
        this.gameState = GameState.INIT;
        SwingUtilities.invokeLater(() -> {
            final ActionListener gameLoopListener = new MainLoopListener();
            gameLoopTimer = new Timer(FRAME_TIME_MS, gameLoopListener);
            initializeGameWindow();
        });
    }

    public GameState getGameState() {
        return gameState;
    }

    public IBoardView getBoardView() {
        return gameBoard.getBoardView();
    }

    public Tetromino getCurrentTetromino() {
        return gameBoard.getCurrentTetromino();
    }
    

    /**
     * Try to move the current piece in the specified direction. If the move is
     * successful, repaint the canvas to reflect the new position of the piece.
     *
     * @param direction The direction to move the piece (e.g., LEFT, RIGHT,
     *                  DOWN).
     */
    public void movePiece(final DirectionFlag direction) {
        if (gameState != GameState.PLAYING) {
            return;
        }
        if (gameBoard.tryMovePiece(direction)) {
            tetrisCanvas.repaintCanvas(false);
        }
    }

    /**
     * Try to rotate the current piece in the specified direction. If the
     * rotation is successful, repaint the canvas to reflect the new orientation
     * of the piece.
     *
     * @param direction The direction to rotate the piece (CLOCKWISE, COUN
     */
    public void rotatePiece(final DirectionFlag direction) {
        if (gameState != GameState.PLAYING) {
            return;
        }
        if (gameBoard.tryRotatePiece(direction)) {
            tetrisCanvas.repaintCanvas(false);
        }
    }

    public void pauseGame() {
        if (gameState == GameState.PLAYING) {
            gameLoopTimer.stop();
            updateState(GameState.PAUSED);
        }
    }

    public void resumeGame() {
        if (gameState == GameState.PAUSED) {
            resetTime();
            gameLoopTimer.start();
            updateState(GameState.PLAYING);
        }
    }

    /**
     * Starts the game by resetting the game board and starting the game loop
     * timer. Sets the game state to PLAYING. This method can only be called if
     * the game is in the INITIALIZE or GAME_OVER state to prevent starting a
     * new game while one is already in progress.
     */
    void startGame() {
        switch (gameState) {
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
                break;
            }
        }
    }

    void exitGame() {
        System.exit(0);
    }

    private void resetTime() {
        prevTime = System.nanoTime();
        gravityAccumulator = 0;
    }

    private void newGame() {
        gameBoard = new GameBoard();
        if (tetrisDrawingHandler.getGrid() == null) {
            tetrisDrawingHandler.drawGrid();
        }
        updateState(GameState.PLAYING);
        if (!gameBoard.trySetNewTetromino()) {
            setGameOver();
            return;
        }
        tetrisCanvas.repaintCanvas(false);
        resetTime();
    }

    private void setGameOver() {
        gameLoopTimer.stop();
        updateState(GameState.GAME_OVER);
    }

    /**
     * Initializes the game window by creating an instance of GameWindow using
     * the GuiFactory, setting up the TetrisCanvas and ScoreUI, and displaying
     * the GUI. The method also sets up a key input handler to manage user
     * interactions with the game. This method is called on the Event Dispatch
     * Thread (EDT) to ensure that all Swing components are created and
     * manipulated in a thread-safe manner. After initializing the game window,
     * it calls showGui() to make the window visible and request focus for the
     * canvas to receive key events.
     */
    private void initializeGameWindow() {
        final TetrisKeyInputHandler keyInputHandler = new TetrisKeyInputHandler(this);
        tetrisDrawingHandler = new TetrisDrawingHandler();

        final GuiFactory.GuiComponents gui = GuiFactory.createGUI(this, tetrisDrawingHandler, keyInputHandler);
        this.tetrisCanvas = gui.canvas();
        this.scoreUI = gui.scoreUI();
        this.infoP = gui.infoP();
        updateState(GameState.PREPARED);
        showGui(gui.window());
    }

    /**
     * Displays the game window by packing the components, setting the window to
     * be non-resizable, and making it visible. It also requests focus for the
     * TetrisCanvas to ensure that it can receive key events for user input.
     * This method is called after the game window has been initialized to show
     * the GUI to the player and allow them to interact with the game using the
     * keyboard.
     */
    private void showGui(final GameWindow gameWindow) {
        gameWindow.pack();
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        SwingUtilities.invokeLater(tetrisCanvas::requestFocusInWindow);// request focus for the canvas to receive key
                                                                       // events
    }

    /**
     * !!!except before UI is initialization CALL THIS METHOD TO UPDATE THE GAME
     * STATE!!! NOT the {@link GameState} field directly unless its necessary. then
     * document it please why was it necessary.
     *
     * @param newState new state game is set to. this method ensures that whenever
     *                 the game state is updated, the information panel is also
     *                 refreshed to reflect the new state.
     *                 Updates the game state and refreshes the information panel to
     *                 reflect the new state.
     *
     */
    private void updateState(final GameState newState) {
        this.gameState = newState;
        if (infoP == null) {
            return;
        }
        infoP.updateInfo(gameState);
    }

}
