package org.sehes.tetris.controller;

public enum InputAction {
    /**
     * Exit application, unpause game
     */
    CANCEL,
    /**
     * Start new game
     */
    CONFIRM,
    /**
     * Hard drop
     */
    HARD_DROP,
    /**
     * Soft drop - move mino down one row
     */
    MOVE_DOWN,
    /**
     * Move mino left
     */
    MOVE_LEFT,
    /**
     * Move mino right
     */
    MOVE_RIGHT,
    /**
     * Rotate mino counterclockwise
     */
    ROTATE_CCW,
    /**
     * Rotate mino clockwise
     */
    ROTATE_CW,
}
