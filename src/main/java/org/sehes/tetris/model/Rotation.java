package org.sehes.tetris.model;

/**
 * The Rotation enum represents the possible rotations of a Tetromino piece in
 * Tetris.<br>
 * It represents them as {@code NORTH}, {@code EAST}, {@code SOUTH},
 * and {@code WEST}.<br>
 * It also provides a method to rotate clockwise and counter-clockwise,
 * it exposes methods to rotate clockwise and counter-clockwise.
 */
public enum Rotation {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private static final Rotation[] ROTATION_ORDER = Rotation.values();
    private static final int SIZE = ROTATION_ORDER.length;
    private final int value;

    Rotation(int state) {
        this.value = state;
    }

    public int getValue() {
        return value;
    }

    public Rotation rotateClockwise() {
        return next();
    }

    public Rotation rotateCounterClockwise() {
        return previous();
    }

    private Rotation next() {
        return ROTATION_ORDER[(value + 1) % SIZE];
    } // next rotation

    private Rotation previous() {
        return ROTATION_ORDER[(value + (SIZE - 1)) % SIZE];
    } // previous rotation
}
