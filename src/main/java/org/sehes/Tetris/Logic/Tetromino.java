package org.sehes.tetris.logic;

import org.sehes.tetris.logic.util.Util;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tetromino {
    private final int SIZEREC = 30;
    private final Color color;
    private boolean[][] grid;
    private final int[] position;
    private final int[] STARTPOS = {4, 0};/*X left to right, Y up to bottom will be center block*/
    private final int[] pivotGridCord;
    private final int[] nextPivotGridCord = new int[2];
    private final int[] nextPos = new int[2];
    private final int[] pivotPos = new int[2];
    private final static Random random = new Random();


    public static Tetromino tetrominoFactory() {
           int tetrominoType = random.nextInt(7);
        //int tetrominoType = 0;
        return new Tetromino(TETROMINO_TYPE.get(tetrominoType));
    }

    private Tetromino(TETROMINO_TYPE type) {
        color = type.color;
        grid = type.grid;
        position = Arrays.copyOf(STARTPOS, STARTPOS.length);
        pivotGridCord = type.PivotGridCord;
    }

    public Color getColor() {
        return color;
    }

    public int getSIZEREC() {
        return SIZEREC;
    }


    public int getXCoord() {
        return position[0] * SIZEREC;
    }

    public int getYCoord() {
        return (position[1] - 1) * SIZEREC;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public int[] getPosition() {
        return position;
    }

    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    public int[] getNextPos() {
        return nextPos;
    }

    public void move(DirectionFlag flag) {
        position[0] += flag.getX();
        position[1] += flag.getY();
    }

    /**
     * Rotates the tetromino grid based on the direction flag
     * @param flag Direction of rotation (ROTATE_R or ROTATE_L)
     * @return The rotated grid
     */
    public boolean[][] rotate(DirectionFlag flag) {
        setPivotPos();
        boolean[][] newGrid = Util.transposeMatrix(grid);
        switch (flag) {
            case ROTATE_R -> Util.swapColumns(newGrid);
            case ROTATE_L -> Util.swapRows(newGrid);
        }
        calcNextPivotCord(flag);
        createNextPos(newGrid);
        return newGrid;
    }

    /**
     * its calculate position of pivot block in the mainBoard
     *
     *
     */
    private void setPivotPos() {
        pivotPos[0] = position[0] + pivotGridCord[0];
        pivotPos[1] = position[1] + pivotGridCord[1];
    }

    private void calcNextPivotCord(DirectionFlag flag) {
        switch (flag) {
            case ROTATE_R -> {
                nextPivotGridCord[0] = grid[0].length - pivotGridCord[1] - 1;
                nextPivotGridCord[1] = pivotGridCord[0];
            }
            case ROTATE_L -> {
                nextPivotGridCord[0] = pivotGridCord[1];
                nextPivotGridCord[1] = grid.length - pivotGridCord[0] - 1;
            }
        }
    }

    private void createNextPos(boolean[][] newGrid) {
        nextPos[0] = pivotPos[0] - nextPivotGridCord[0];
        nextPos[1] = pivotPos[1] - nextPivotGridCord[1];
    }

    public void applyNewPosition() {
        position[0] = nextPos[0];
        position[1] = nextPos[1];
    }


    enum TETROMINO_TYPE {

        I(new boolean[][]{
                {true, true, true, true}, {false, false, false, false}},
                Color.CYAN, 0, new int[]{2, 0}),
        J(new boolean[][]{
                {true, false, false},
                {true, true, true}},
                Color.BLUE, 1, new int[]{1, 1}),
        L(new boolean[][]{
                {false, false, true},
                {true, true, true}},
                Color.ORANGE, 2, new int[]{1, 1}),

        O(new boolean[][]{
                {true, true},
                {true, true}},
                Color.YELLOW, 3, new int[]{1, 1}),

        S(new boolean[][]{
                {false, true, true},
                {true, true, false}},
                Color.GREEN, 4, new int[]{1, 1}),

        T(new boolean[][]{
                {false, true, false},
                {true, true, true}
        }, Color.MAGENTA, 5, new int[]{1, 1}),

        Z(new boolean[][]{
                {true, true, false},
                {false, true, true}
        }, Color.RED, 6, new int[]{1, 1}),
        ;

        private final Color color;
        private final boolean[][] grid;
        private final int intValue;
        private final int[] PivotGridCord;

        TETROMINO_TYPE(boolean[][] grid, Color color, int intValue, int[] pivotGridCord) {
            this.color = color;
            this.grid = grid;
            this.intValue = intValue;
            this.PivotGridCord = pivotGridCord;
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
