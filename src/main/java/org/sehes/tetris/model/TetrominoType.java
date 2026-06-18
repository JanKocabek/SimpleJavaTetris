package org.sehes.tetris.model;

public enum TetrominoType {

    I,
    J,
    L,
    O,
    S,
    T,
    Z,
    NON;

    private static final TetrominoType[] TETROMINO_TYPES;

    static {
        TETROMINO_TYPES = new TetrominoType[values().length - 1];
        System.arraycopy(values(), 0, TETROMINO_TYPES, 0, values().length - 1);
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
        return TETROMINO_TYPES.length;
    }

    /**
     *
     * @param intValue the number representing the tetromino type
     * @return returns the tetromino type based on the intValue
     */
    public static TetrominoType get(final int intValue) {
        if (intValue < 0 || intValue >= TETROMINO_TYPES.length) {
            throw new IllegalArgumentException("Invalid intValue: " + intValue);
        }
        return TETROMINO_TYPES[intValue];
    }
}