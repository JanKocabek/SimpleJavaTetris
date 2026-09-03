package org.sehes.tetris.model;

import java.util.Arrays;

/**
 * Enumerates the seven playable Tetris tetromino shapes and the {@link #NON} sentinel value.
 */
public enum TetrominoType {

    /** The straight, four-block tetromino. */
    I,
    /** The left-facing hook tetromino. */
    J,
    /** The right-facing hook tetromino. */
    L,
    /** The square tetromino. */
    O,
    /** The S-shaped tetromino. */
    S,
    /** The T-shaped tetromino. */
    T,
    /** The Z-shaped tetromino. */
    Z,
    /** Sentinel representing the absence of a tetromino. */
    NON;

    /** The seven playable tetromino shapes, excluding {@link #NON}. */
    private static final TetrominoType[] PLAYABLE_TETROMINO_SHAPES;

    /** Every enum value, including the {@link #NON} sentinel. */
    private static final TetrominoType[] ALL_TETROMINO_TYPES;

    static {
        ALL_TETROMINO_TYPES = values();
        PLAYABLE_TETROMINO_SHAPES = Arrays.stream(ALL_TETROMINO_TYPES)
                .filter(type -> type != NON)
                .toArray(TetrominoType[]::new);
    }

    /**
     * Returns the array of the seven playable tetromino shapes, excluding {@link #NON}.
     *
     * @return the playable tetromino-shape array
     */
    public static TetrominoType[] getTetrominoShapes() {
        return PLAYABLE_TETROMINO_SHAPES;
    }

    /**
     * Returns the array of all enum values, including {@link #NON}.
     *
     * @return the array containing every {@code TetrominoType} value
     */
    public static TetrominoType[] getTetrominoTypes() {
        return ALL_TETROMINO_TYPES;
    }

    /**
     * Returns the number of playable tetromino shapes.
     *
     * @return the number of shapes in the playable tetromino-shape array
     */
    public static int size() {
        return PLAYABLE_TETROMINO_SHAPES.length;
    }

    /**
     * Returns the playable tetromino shape at the supplied zero-based index.
     *
     * @param intValue the index of a playable tetromino shape
     * @return the playable tetromino shape at {@code intValue}
     * @throws IllegalArgumentException if {@code intValue} is outside the playable-shape range
     */
    public static TetrominoType get(final int intValue) {
        if (intValue < 0 || intValue >= PLAYABLE_TETROMINO_SHAPES.length) {
            throw new IllegalArgumentException("Invalid intValue: " + intValue);
        }
        return PLAYABLE_TETROMINO_SHAPES[intValue];
    }
}
