package org.sehes.tetris.model;

import java.awt.*;
import java.util.*;
import java.util.List;

public enum TetrominoType {

    I(new Coordinate[][]{
            {new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(2, 0)}, // State 0
            {new Coordinate(1, 1), new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(1, 2)}, // State 1
            {new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 1), new Coordinate(2, 1)}, // State 2
            {new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(0, 2)}// State 3
    },
            Color.CYAN,
            0),
    J(new Coordinate[][]{
            {new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0)}, // 0°
            {new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1)}, // 90°
            {new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0)}, // 180°
            {new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1)}// 270°
    }, Color.BLUE, 1),

    L(new Coordinate[][]{
            {new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, -1)}, // 0°
            {new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)}, // 90°
            {new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(-1, 1)}, // 180°
            {new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(-1, -1)}// 270°
    }, Color.ORANGE, 2),

    O(new Coordinate[][]{
            {new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)}, // 0°
            {new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)}, // Same
            {new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)}, // Same
            {new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)}// Same
    }, Color.YELLOW, 3),

    S(new Coordinate[][]{
            {new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(-1, 0)}, // 0°
            {new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, -1)}, // 90°
            {new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(1, 0)}, // 180°
            {new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, 1)}// 270°
    }, Color.GREEN, 4),

    T(new Coordinate[][]{
            {new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0)}, // 0°
            {new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, -1)}, // 90°
            {new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(1, 0)}, // 180°
            {new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1)}// 270°
    }, Color.MAGENTA, 5),

    Z(new Coordinate[][]{
            {new Coordinate(-1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0)}, // 0°
            {new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1)}, // 90°
            {new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0)}, // 180°
            {new Coordinate(-1, 1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)}// 270°
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

    private final Coordinate[][] shapeState;

    private final Color color;

    private final int intValue;

    public Color getColor() {
        return color;
    }

    public List <Coordinate> getTetrominoState(int state) {
        return Collections.unmodifiableList(Arrays.asList(shapeState[state].clone()));
    }// clone the array and return an unmodifiable list


    /**
     * Gets the next state of the tetromino based on the given state.
     *
     * @param state the current state of the tetromino
     * @return the next state of the tetromino
     */
    public List <Coordinate> getNextState(int state) {
        int nextState = (state + 1) % 4;
        return Collections.unmodifiableList(Arrays.asList(shapeState[nextState].clone()));
    }

    public List <Coordinate> getPreviousState(int state) {
        int previousState = (state + 3) % 4;
        return Collections.unmodifiableList(Arrays.asList(shapeState[previousState].clone()));
    }

    TetrominoType(final Coordinate[][] shapeState, final Color color, final int intValue) {
        this.color = color;
        this.shapeState = shapeState;
        this.intValue = intValue;
    }
}