package org.sehes.tetris.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public enum TetrominoType {

    I(new Coordinate[][] {
            { new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(2, 0) }, // State 0
            { new Coordinate(1, 1), new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(1, 2) }, // State 1
            { new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 1), new Coordinate(2, 1) }, // State 2
            { new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(0, 2) }// State 3
    },
            Color.CYAN),
    J(new Coordinate[][] {
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0) }, // 0°
            { new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1) }, // 90°
            { new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0) }, // 180°
            { new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1) }// 270°
    }, Color.BLUE),

    L(new Coordinate[][] {
            { new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, -1) }, // 0°
            { new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1) }, // 90°
            { new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(-1, 1) }, // 180°
            { new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(-1, -1) }// 270°
    }, Color.ORANGE),

    O(new Coordinate[][] {
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // 0°
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // Same
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // Same
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }// Same
    }, Color.YELLOW),

    S(new Coordinate[][] {
            { new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(-1, 0) }, // 0°
            { new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // 90°
            { new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(1, 0) }, // 180°
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, 1) }// 270°
    }, Color.GREEN),

    T(new Coordinate[][] {
            { new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0) }, // 0°
            { new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, -1) }, // 90°
            { new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(1, 0) }, // 180°
            { new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1) }// 270°
    }, Color.MAGENTA),

    Z(new Coordinate[][] {
            { new Coordinate(-1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0) }, // 0°
            { new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1) }, // 90°
            { new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0) }, // 180°
            { new Coordinate(-1, 1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }// 270°
    }, Color.RED);

    /**
     * the cached tetromino types
     */
    private static final TetrominoType[] cachedEnum = TetrominoType.values();

    /**
     * 
     * @return the number of tetromino types used in the tetromino factory method for random number of tetromino type
     */
    public static int size() {
        return cachedEnum.length;
    }

    /**
     * 
     * @param intValue
     * @return returns the tetromino type based on the intValue
     */
    public static TetrominoType get(final int intValue) {
        if (intValue < 0 || intValue >= cachedEnum.length) {
            throw new IllegalArgumentException("Invalid intValue: " + intValue);
        }
        return cachedEnum[intValue];
    }

    private final Color color;
    private final List<List<Coordinate>> allStates;

    TetrominoType(final Coordinate[][] shapeState, final Color color) {
        this.color = color;
        this.allStates = Arrays.stream(shapeState).map(List::of).toList();

    }

    Color getColor() {
        return color;
    }

    /**
     * Returns the tetromino state at the given state index.
     * 
     * @param state the state index of the tetromino
     * @return the List of Coordinates describing the tetromino shape at the given
     *         state index
     */
    public List<Coordinate> getTetrominoState(Rotation rotationState) {

        return allStates.get(rotationState.getValue());
    }

    /**
     * Gets the next state of the tetromino based on the given state.
     *
     * @param state the current state of the tetromino
     * @return the List of Coordinates describing the tetromino shape at the given
     *         state index
     */
   
}