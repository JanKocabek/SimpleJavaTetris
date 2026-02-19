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

public class GameManager {

    public enum GameState {
        INITIALIZE, PLAYING, PAUSED, GAME_OVER

    }

    private GameState gameState;
    private GameWindow gameWindow;
    private TetrisCanvas tetrisCanvas;
    private GameBoard gameBoard;
    private Timer gameLoopTimer;
    private TetrisKeyInputHandler keyInputHandler;

    public void startApp() {
        this.gameState = GameState.INITIALIZE;
        SwingUtilities.invokeLater(() -> {
            this.gameWindow = new GameWindow(GameParameters.WINDOW_WIDTH, GameParameters.WINDOW_HEIGHT);
            this.tetrisCanvas = new TetrisCanvas(new TetrisDrawingHandler(), this);
            gameWindow.setCanvas(tetrisCanvas);
            // this.gameBoard = new GameBoard();//remove this later when we have a better way to initialize the game board
            this.keyInputHandler = new TetrisKeyInputHandler(this);
            tetrisCanvas.addKeyListener(keyInputHandler);
            ActionListener gameLoopListener = new MainLoopListener();
            gameLoopTimer = new Timer(GameParameters.GAME_SPEED, gameLoopListener);
        });
    }

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

    public void movePiece(DirectionFlag direction) {
        if (gameBoard.tryMovePiece(direction)) {
            tetrisCanvas.repaintCanvas();
        }
    }

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
            }
        }
    }
}
