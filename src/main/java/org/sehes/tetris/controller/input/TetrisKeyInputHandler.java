package org.sehes.tetris.controller.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;

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

    private final InputReceiver receiver;

    /**
     * Constructor for the TetrisKeyInputHandler class.
     *
     * @param receiver The class responsible for managing the pressedKey
     */
    public TetrisKeyInputHandler(InputReceiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Method to translate the input to the unified InputAction enum based on the pressed key.
     *
     * @param e The KeyEvent object containing information about the key that was
     *          pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_DOWN -> receiver.handleInput(InputAction.MOVE_DOWN);
            case VK_LEFT -> receiver.handleInput(InputAction.MOVE_LEFT);
            case VK_RIGHT -> receiver.handleInput(InputAction.MOVE_RIGHT);
            default -> {
               break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case VK_ESCAPE -> receiver.handleInput(InputAction.CANCEL);
            case VK_ENTER -> receiver.handleInput(InputAction.CONFIRM);
            case VK_UP -> receiver.handleInput(InputAction.ROTATE_CW);
            case VK_A -> receiver.handleInput(InputAction.ROTATE_CCW);
            case VK_SPACE -> receiver.handleInput(InputAction.HARD_DROP);
            default -> {
              //do nothing
            }
        }
    }
}
