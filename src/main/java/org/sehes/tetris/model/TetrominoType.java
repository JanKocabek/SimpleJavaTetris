package org.sehes.tetris.model;

import java.util.Arrays;

public enum TetrominoType {

    I,
    J,
    L,
    O,
    S,
    T,
    Z,
    NON;

    private static final TetrominoType[] TETROMINO_SHAPES;
    private static final TetrominoType[] TETROMINO_TYPES;
    static {
        TETROMINO_TYPES = values();
        TETROMINO_SHAPES = Arrays.stream(TETROMINO_TYPES).filter(type -> type != NON).toArray(TetrominoType[]::new);
    }

    public static TetrominoType[] getTetrominoShapes() {
        return TETROMINO_SHAPES;
    }

    public static TetrominoType[] getTetrominoTypes() {
        return TETROMINO_TYPES;
    }

    /**
     *
     * @return the number of tetromino types used in the tetromino factory method
     *         for random number of tetromino type
     */
    public static int size() {
        return TETROMINO_SHAPES.length;
    }

    /**
     *
     * @param intValue the number representing the tetromino type
     * @return returns the tetromino type based on the intValue
     */
    public static TetrominoType get(final int intValue) {
        if (intValue < 0 || intValue >= TETROMINO_SHAPES.length) {
            throw new IllegalArgumentException("Invalid intValue: " + intValue);
        }
        return TETROMINO_SHAPES[intValue];
    }
}