package org.sehes.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.gui.GameWindow;
import org.sehes.tetris.gui.GuiFactory;
import org.sehes.tetris.gui.ScorePanel;
import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.gui.TetrisDrawingHandler;
import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.board.GameBoard;

/**
 * The GameManager class is responsible for managing the overall game state,
 * handling user input, and coordinating the game loop. It initializes the game
 * components, starts the game loop, and provides methods for moving and
 * rotating pieces, as well as pausing and resuming the game.
 */
public class GameManager {

    // Define the possible game states
    public enum GameState {
        INITIALIZE, PLAYING, PAUSED, GAME_OVER
    }

    private GameState gameState;
    private GameWindow gameWindow;
    private TetrisCanvas tetrisCanvas;
    private GameBoard gameBoard;
    private Timer gameLoopTimer;
    private ScorePanel scoreUI;


    
    /**
     * Starts the Tetris application by initializing the game state, creating game loop timer, and setting up the game window. The game loop timer is configured to trigger the main game loop at a fixed interval defined by GameParameters.GAME_SPEED. The game window is created on the Event Dispatch Thread (EDT) to ensure thread safety when interacting with Swing components. The constructor initializes the game state to INITIALIZE and sets up the necessary components for the game, including the canvas and score UI, which will be used in the main game loop to update the display and score as the game progresses.
     *
     */
    public GameManager() {
        this.gameState = GameState.INITIALIZE;
        SwingUtilities.invokeLater(() -> {
            ActionListener gameLoopListener = new MainLoopListener();
            gameLoopTimer = new Timer(GameParameters.GAME_SPEED, gameLoopListener);
            initializeGameWindow();
        });
    }

    public GameState getGameState() {
        return gameState;
    }

    public TetrisCanvas getCanvas() {
        return tetrisCanvas;
    }

    public GameBoard getBoard() {
        return gameBoard;
    }

    /**
     * Initializes the game window by creating an instance of GameWindow using the GuiFactory, setting up the TetrisCanvas and ScoreUI, and displaying the GUI. The method also sets up a key input handler to manage user interactions with the game. This method is called on the Event Dispatch Thread (EDT) to ensure that all Swing components are created and manipulated in a thread-safe manner. After initializing the game window, it calls showGui() to make the window visible and request focus for the canvas to receive key events.
     */
    private void initializeGameWindow() {
        TetrisKeyInputHandler keyInputHandler;
        keyInputHandler = new TetrisKeyInputHandler(this);
        this.gameWindow = GuiFactory.createGUI(this, new TetrisDrawingHandler(), keyInputHandler);
        this.tetrisCanvas = gameWindow.getCanvas();
        this.scoreUI = gameWindow.getScoreUI();
        showGui();
    }

    /**
     * Displays the game window by packing the components, setting the window to be non-resizable, and making it visible. It also requests focus for the TetrisCanvas to ensure that it can receive key events for user input. This method is called after the game window has been initialized to show the GUI to the player and allow them to interact with the game using the keyboard.
     */
    private void showGui() {
        gameWindow.pack();
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        SwingUtilities.invokeLater(tetrisCanvas::requestFocusInWindow);//request focus for the canvas to receive key events
    }

    /**
     * Starts the game by resetting the game board and starting the game loop
     * timer. Sets the game state to PLAYING. This method can only be called if
     * the game is in the INITIALIZE or GAME_OVER state to prevent starting a
     * new game while one is already in progress.
     */
    public void startGame() {
        if (gameState != GameState.INITIALIZE && gameState != GameState.GAME_OVER) {
            return; // Prevent starting a new game if one is already in progress
        }
        gameBoard = new GameBoard(); // Reset the game board for a new game
        GameState previous = gameState;
        if (gameBoard.trySetNewTetromino()) {
            gameState = GameState.PLAYING;
            scoreUI.resetScore();
        } else {
            gameState = GameState.GAME_OVER; // If we can't set a new piece, the game is over
            return;
        }
        if (previous == GameState.INITIALIZE) {
            gameLoopTimer.start();
        } else {
            gameLoopTimer.restart();
        }
        tetrisCanvas.repaintCanvas();
    }

    /**
     * Try to move the current piece in the specified direction. If the move is
     * successful, repaint the canvas to reflect the new position of the piece.
     *
     * @param direction The direction to move the piece (e.g., LEFT, RIGHT,
     * DOWN).
     */
    public void movePiece(DirectionFlag direction) {
        if (gameState != GameState.PLAYING) {
            return;
        }
        if (gameBoard.tryMovePiece(direction)) {
            tetrisCanvas.repaintCanvas();
        }
    }

    /**
     * Try to rotate the current piece in the specified direction. If the
     * rotation is successful, repaint the canvas to reflect the new orientation
     * of the piece.
     *
     * @param direction The direction to rotate the piece (CLOCKWISE, COUN
     */
    public void rotatePiece(DirectionFlag direction) {
        if (gameState != GameState.PLAYING) {
            return;
        }
        if (gameBoard.tryRotatePiece(direction)) {
            tetrisCanvas.repaintCanvas();
        }
    }

    public void pauseGame() {
        if (gameState == GameState.PLAYING) {
            gameLoopTimer.stop();
            gameState = GameState.PAUSED;
        }
    }

    public void resumeGame() {
        if (gameState == GameState.PAUSED) {
            gameLoopTimer.start();
            gameState = GameState.PLAYING;
        }
    }

    /**
     * The Main game loop listener that is triggered by the game loop timer. It attempts to move the current piece down. If the piece cannot move down, it adds the piece to the board, checks for and clears any completed lines, updates the score, and tries to set a new piece. If a new piece cannot be set, it means the game is over, so it updates the game state and stops the game loop timer. After processing the game logic, it repaints the canvas to reflect any changes in the game state.
     */
    private class MainLoopListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameBoard.tryMovePiece(DirectionFlag.DOWN)) {
                gameBoard.addBlockToBoard();
                gameBoard.checkAndClearLines();
                scoreUI.updateScore(gameBoard.getScore());
                if (!gameBoard.trySetNewTetromino()) {
                    gameState = GameState.GAME_OVER;
                    gameLoopTimer.stop();
                }
            }
            tetrisCanvas.repaintCanvas();
        }
    }
}
