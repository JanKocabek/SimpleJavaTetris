package org.sehes.tetris.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TetrominoType {

    I(new Coordinate[][] {
            { new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(2, 0) }, // State 0
            { new Coordinate(1, 1), new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(1, 2) }, // State 1
            { new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 1), new Coordinate(2, 1) }, // State 2
            { new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(0, 2) }// State 3
    },
            Color.CYAN,
            0),
    J(new Coordinate[][] {
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0) }, // 0°
            { new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1) }, // 90°
            { new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0) }, // 180°
            { new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1) }// 270°
    }, Color.BLUE, 1),

    L(new Coordinate[][] {
            { new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, -1) }, // 0°
            { new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1) }, // 90°
            { new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(-1, 1) }, // 180°
            { new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(-1, -1) }// 270°
    }, Color.ORANGE, 2),

    O(new Coordinate[][] {
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // 0°
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // Same
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // Same
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }// Same
    }, Color.YELLOW, 3),

    S(new Coordinate[][] {
            { new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(-1, 0) }, // 0°
            { new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }, // 90°
            { new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(1, 0) }, // 180°
            { new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, 1) }// 270°
    }, Color.GREEN, 4),

    T(new Coordinate[][] {
            { new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0) }, // 0°
            { new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, -1) }, // 90°
            { new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(1, 0) }, // 180°
            { new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1) }// 270°
    }, Color.MAGENTA, 5),

    Z(new Coordinate[][] {
            { new Coordinate(-1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0) }, // 0°
            { new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1) }, // 90°
            { new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0) }, // 180°
            { new Coordinate(-1, 1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1) }// 270°
    }, Color.RED, 6);

    private static final Map<Integer, TetrominoType> map = new HashMap<>();

    static {
        for (final TetrominoType type : TetrominoType.values()) {
            map.put(type.intValue, type);
        }
    }

    public static TetrominoType get(final int intValue) {
        return map.get(intValue);
    }

    private final Color color;

    private final int intValue;
    private final List<List<Coordinate>> allStates;

    TetrominoType(final Coordinate[][] shapeState, final Color color, final int intValue) {
        this.color = color;
        this.intValue = intValue;
        this.allStates = Arrays.stream(shapeState).map(List::of).toList();

    }

    public Color getColor() {
        return color;
    }

    /**
     * Returns the tetromino state at the given state index.
     * 
     * @param state the state index of the tetromino
     * @return the List of Coordinates describing the tetromino shape at the given
     *         state index
     */
    public List<Coordinate> getTetrominoState(int state) {

        return allStates.get(state);
    }

    /**
     * Gets the next state of the tetromino based on the given state.
     *
     * @param state the current state of the tetromino
     * @return the List of Coordinates describing the tetromino shape at the given
     *         state index
     */
    public List<Coordinate> getNextState(int state) {
        int nextState = (state + 1) % 4;
        return allStates.get(nextState);
    }

    public List<Coordinate> getPreviousState(int state) {
        int previousState = (state + 3) % 4;
        return allStates.get(previousState);
    }
}