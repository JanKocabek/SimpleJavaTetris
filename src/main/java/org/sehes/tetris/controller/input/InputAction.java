package org.sehes.tetris.controller.input;

public enum InputAction {
    /**
     * Exit application
     */
    CANCEL(true),
    /**
     * Start new game,pause and unpause game
     */
    CONFIRM(true),
    /**
     * Hard drop
     */
    HARD_DROP(false),
    /**
     * Soft drop - move mino down one row
     */
    MOVE_DOWN(true),
    /**
     * Move mino left
     */
    MOVE_LEFT(true),
    /**
     * Move mino right
     */
    MOVE_RIGHT(true),
    /**
     * Rotate mino counterclockwise
     */
    ROTATE_CCW(false),
    /**
     * Rotate mino clockwise
     */
    ROTATE_CW(false);

    private final boolean isPressed;

    InputAction(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean pressedEdge() {
        return isPressed;
    }
}
