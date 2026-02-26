package org.sehes.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.gui.GameWindow;
import org.sehes.tetris.gui.GuiFactory;
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
     * Starts the Tetris application by initializing the game state, creating
     * the game window and canvas, and setting up the key input handler and game
     * loop timer.
     */
    public void startApp() {
        this.gameState = GameState.INITIALIZE;
        SwingUtilities.invokeLater(() -> {
            ActionListener gameLoopListener = new MainLoopListener();
            gameLoopTimer = new Timer(GameParameters.GAME_SPEED, gameLoopListener);
            initializeGameWindow();
        });
    }

    private void initializeGameWindow() {
        TetrisKeyInputHandler keyInputHandler;
        keyInputHandler = new TetrisKeyInputHandler(this);
        this.gameWindow = GuiFactory.createGUI(this, new TetrisDrawingHandler(), keyInputHandler);
        this.tetrisCanvas = gameWindow.getCanvas();
        showGui();
    }

    private void showGui() {
        gameWindow.pack();
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        SwingUtilities.invokeLater(tetrisCanvas::requestFocusInWindow);//request focus for the canvas to receive key events
    }


    /* 
        * Starts the game by resetting the game board and starting the game loop timer.
        * Sets the game state to PLAYING.
        * This method can only be called if the game is in the INITIALIZE or GAME_OVER state to prevent starting a new game while one is already in progress.
     */
    public void startGame() {
        if (gameState != GameState.INITIALIZE && gameState != GameState.GAME_OVER) {
            return; // Prevent starting a new game if one is already in progress
        }
        gameBoard = new GameBoard(); // Reset the game board for a new game
        GameState previous = gameState;
        if(gameBoard.trySetNewTetromino()) {
            gameState = GameState.PLAYING;
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

    // Inner class to handle the main game loop, which is triggered by the Timer.
    private class MainLoopListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameBoard.tryMovePiece(DirectionFlag.DOWN)) {
                gameBoard.addBlockToBoard();
                gameBoard.checkAndClearLines();
                if (!gameBoard.trySetNewTetromino()) {
                    gameState = GameState.GAME_OVER;
                    gameLoopTimer.stop();
                }
            }
            tetrisCanvas.repaintCanvas();
        }
    }
}
