package org.sehes.tetris.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.RotationFlag;

/**
 * The TetrisKeyInputHandler class is responsible for handling keyboard input
 * for the Tetris game.
 * It handles the input based on the current state of the game. If the game is
 * not running
 * (i.e. the game state is PREPARED or GAME_OVER), it handles the input to start
 * a new game
 * or exit the application. If the game is running, it handles the input to move
 * the current
 * piece, rotate it, pause the game, or exit the application. The class extends
 * the KeyAdapter class and overrides the keyPressed method to handle the input.
 *
 * @author Sehes
 * @version 0.5
 */
public class TetrisKeyInputHandler extends KeyAdapter {

    private final GameManager gameManager;

    /**
     * Constructor for the TetrisKeyInputHandler class.
     * 
     * @param gameManager The GameManager responsible for managing the game state
     *                    and logic.
     */
    public TetrisKeyInputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Method to handle the input based on the current state of the game.
     * 
     * @param e The KeyEvent object containing information about the key that was
     *          pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {

        switch (gameManager.getGameState()) {
            case PREPARED, GAME_OVER -> handleNotRunningGame(e);
            case PLAYING ->
                handlePlayingStateInput(e);
            case PAUSED -> handlePausedStateInput(e);
            default -> {
                // Do nothing for other states
            }
        }
    }

    /**
     * Method to handle the input when the game is running.
     * 
     * @param e The KeyEvent object containing information about the key that was
     *          pressed.
     */
    private void handlePausedStateInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gameManager.resumeGame();
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameManager.exitGame();
        }
    }

    /**
     * Method to handle the input when the game is not running.
     * 
     * @param e The KeyEvent object containing information about the key that was
     *          pressed.
     */
    private void handleNotRunningGame(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER ->
                gameManager.startGame();
            case KeyEvent.VK_ESCAPE ->
                gameManager.exitGame();
            default -> {
                // Do nothing for other keys
            }
        }
    }

    /**
     * Method to handle the input when the game is running.
     * 
     * @param e The KeyEvent object containing information about the key that was
     *          pressed.
     */
    private void handlePlayingStateInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT ->
                gameManager.movePiece(DirectionFlag.RIGHT);
            case KeyEvent.VK_LEFT ->
                gameManager.movePiece(DirectionFlag.LEFT);
            case KeyEvent.VK_DOWN ->
                gameManager.movePiece(DirectionFlag.DOWN);
            case KeyEvent.VK_UP ->
                gameManager.rotatePiece(RotationFlag.CLOCKWISE);
            case KeyEvent.VK_A ->
                gameManager.rotatePiece(RotationFlag.COUNTER_CLOCKWISE);
            case KeyEvent.VK_ENTER ->
                gameManager.pauseGame();
            case KeyEvent.VK_ESCAPE ->
                gameManager.exitGame();
            default -> {
                // Do nothing for other keys
            }
        }
    }
}
