package org.sehes.tetris.logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.sehes.tetris.controller.GameManager;

/*
    * The TetrisKeyInputHandler class is responsible for handling keyboard input during the Tetris game. It extends KeyAdapter and overrides the keyPressed method to respond to specific key events based on the current state of the game. Depending on whether the game is initializing, playing, paused, or over, it processes different key inputs to control the game flow, such as starting the game, moving or rotating the Tetromino pieces, pausing or resuming the game. This class interacts with the GameManager to update the game state accordingly based on user input.
 */
public class TetrisKeyInputHandler extends KeyAdapter {

    private final GameManager gameManager;

    public TetrisKeyInputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (gameManager.getGameState()) {
            case INITIALIZE, GAME_OVER -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameManager.startGame();
                }
            }
            case PLAYING ->
                handlePlayingStateInput(e);
            case PAUSED -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameManager.resumeGame();
                }
            }
        }
    }

    private void handlePlayingStateInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT ->
                gameManager.movePiece(DirectionFlag.RIGHT);
            case KeyEvent.VK_LEFT ->
                gameManager.movePiece(DirectionFlag.LEFT);
            case KeyEvent.VK_DOWN ->
                gameManager.movePiece(DirectionFlag.DOWN);
            case KeyEvent.VK_UP ->
                gameManager.rotatePiece(DirectionFlag.ROTATE_R);
            case KeyEvent.VK_A ->
                gameManager.rotatePiece(DirectionFlag.ROTATE_L);
            case KeyEvent.VK_ENTER ->
                gameManager.pauseGame();
            /* case KeyEvent.VK_ESCAPE ->
                    gameManager.exitGame(); */
            default -> {
                // Do nothing for other keys
            }
        }
    }
}
