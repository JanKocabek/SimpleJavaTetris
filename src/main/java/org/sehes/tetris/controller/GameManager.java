package org.sehes.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.sehes.tetris.gui.GameWindow;
import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.gui.TetrisDrawingHandler;
import org.sehes.tetris.logic.DirectionFlag;
import org.sehes.tetris.logic.GameBoard;
import org.sehes.tetris.logic.GameParameters;
import org.sehes.tetris.logic.TetrisKeyInputHandler;

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
    private TetrisKeyInputHandler keyInputHandler;

    /**
     * Starts the Tetris application by initializing the game state, creating
     * the game window and canvas, and setting up the key input handler and game
     * loop timer.
     */
    public void startApp() {
        this.gameState = GameState.INITIALIZE;
        SwingUtilities.invokeLater(() -> {
            this.gameWindow = new GameWindow(GameParameters.WINDOW_WIDTH, GameParameters.WINDOW_HEIGHT);
            this.tetrisCanvas = new TetrisCanvas(new TetrisDrawingHandler(), this);
            gameWindow.setCanvas(tetrisCanvas);
            this.gameBoard = new GameBoard();//remove this later when we have a better way to initialize the game board
            this.keyInputHandler = new TetrisKeyInputHandler(this);
            tetrisCanvas.addKeyListener(keyInputHandler);
            ActionListener gameLoopListener = new MainLoopListener();
            gameLoopTimer = new Timer(GameParameters.GAME_SPEED, gameLoopListener);
        });
    }

    /* 
        * Starts the game by resetting the game board and starting the game loop timer.
        * Sets the game state to PLAYING.
        * This method can only be called if the game is in the INITIALIZE or GAME_OVER state to prevent starting a new game while one is already in progress.
     */
    public void startGame() {
        if (gameState == GameState.INITIALIZE || gameState == GameState.GAME_OVER) {
            gameBoard = new GameBoard(); // Reset the game board for a new game
            gameLoopTimer.start();
            gameState = GameState.PLAYING;
        }
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
    * Try to move the current piece in the specified direction.
     If the move is successful, repaint the canvas to reflect the new position of the piece.
    * @param direction The direction to move the piece (e.g., LEFT, RIGHT, DOWN).
     */

    public void movePiece(DirectionFlag direction) {
        if (gameBoard.tryMovePiece(direction)) {
            tetrisCanvas.repaintCanvas();
        }
    }

    /**
     * Try to rotate the current piece in the specified direction.
     * If the rotation is successful, repaint the canvas to reflect the new orientation of the piece.
     * @param direction The direction to rotate the piece (CLOCKWISE, COUN
 */
    public void rotatePiece(DirectionFlag direction) {
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

    // Inner class to handle the main game loop, which is triggered by the Timer.
    
    private class MainLoopListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameBoard.getCurrentTetromino() == null) {
                gameBoard.setNewTetromino();
            }
            if (gameBoard.tryMovePiece(DirectionFlag.DOWN)) {
                tetrisCanvas.repaintCanvas();
            } else {
                gameBoard.addBlockToBoard();
                gameBoard.setNewTetromino();
                tetrisCanvas.repaintCanvas();
            }
        }
    }
}
