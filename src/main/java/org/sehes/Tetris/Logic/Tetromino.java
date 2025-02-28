package org.sehes.Tetris.Logic;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tetromino {
    private final int SIZEREC = 30;
    private final Color color;
    private final boolean[][] grid;
    private final int[] position;
    private final int[] STARTPOS = {4, 0};/*X left to right, Y up to bottom*/
    private final static Random random = new Random();

    public static Tetromino tetrominoFactory() {
        int tetrominoType = random.nextInt(7);
        return new Tetromino(TETROMINO_TYPE.get(tetrominoType));
    }

    private Tetromino(TETROMINO_TYPE type) {
        color = type.getColor();
        grid = type.getGrid();
        position = STARTPOS;
    }

    public Color getColor() {
        return color;
    }

    public int getSIZEREC() {
        return SIZEREC;
    }

    public void move(DirectionFlag flag) {
        position[0] += flag.getX();
        position[1] += flag.getY();
    }

    public int getxCoord() {
        return  position[0] * SIZEREC;
    }

    public int getyCoord() {
        return (position[1] - 1) * SIZEREC;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public int[] getPosition() {
        return position;
    }

    enum TETROMINO_TYPE {

        I(new boolean[][]{
                {true, true, true, true}}, Color.CYAN, 0),
        J(new boolean[][]{
                {true, false, false},
                {true, true, true}}, Color.BLUE, 1),
        L(new boolean[][]{
                {false, false, true},
                {true, true, true}}, Color.ORANGE, 2),

        O(new boolean[][]{
                {true, true},
                {true, true}}, Color.YELLOW, 3),

        S(new boolean[][]{
                {false, true, true},
                {true, true, false}}, Color.GREEN, 4),

        T(new boolean[][]{
                {false, true, false},
                {true, true, true}
        }, Color.MAGENTA, 5),

        Z(new boolean[][]{
                {true, true, false},
                {false, true, true}
        }, Color.RED, 6);

        private final Color color;
        private final boolean[][] grid;
        private final int intValue;

        TETROMINO_TYPE(boolean[][] grid, Color color, int intValue) {
            this.color = color;
            this.grid = grid;
            this.intValue = intValue;
        }

        public Color getColor() {
            return color;
        }

        public boolean[][] getGrid() {
            return grid;
        }

        public int getIntValue() {
            return intValue;
        }

        private static final Map<Integer, TETROMINO_TYPE> map = new HashMap<>();

        static {
            for (TETROMINO_TYPE type : TETROMINO_TYPE.values()) {
                map.put(type.intValue, type);
            }
        }

        public static TETROMINO_TYPE get(int intValue) {
            return map.get(intValue);
        }
    }
}
