package org.sehes.tetris.logic;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.sehes.tetris.logic.util.MatrixTransformations;

public class Tetromino {

    private static final int SIZEREC = 30;
    private static final int[] STARTPOS = {4, 0};/*X column, Y row*/
    private static final Random random = new Random();
    private final Color color;
    private final int[] position;//X column, Y row
    private  boolean[][] grid;

    public static Tetromino tetrominoFactory() {
        int tetrominoType = random.nextInt(7);
        return new Tetromino(TETROMINO_TYPE.get(tetrominoType));
    }

    private Tetromino(TETROMINO_TYPE type) {
        color = type.color;
        grid = type.grid;
        position = Arrays.copyOf(STARTPOS, STARTPOS.length);
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

    /**
     * Returns the current position of the tetromino as an array of integers.
     * The first element is the X coordinate (column), and the second is the Y coordinate (row).
     * @return The position of the tetromino as [X, Y].
     */
    public int[] getPosition() {
        return position;
    }

    /**
     * Sets the grid of the tetromino to a new 2D boolean array. This method is
     * used to update the tetromino's grid after rotation or other
     * transformations.
     *
     * @param grid The new grid representation of the tetromino.
     */
    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    public void move(DirectionFlag flag) {
        position[0] += flag.getX();
        position[1] += flag.getY();
    }

    /**
     * Rotates the tetromino grid based on the direction flag
     * <p>
     * The method first transposes the grid to switch rows and columns. Then,
     * depending on the rotation direction (right or left), it either swaps
     * columns (for right rotation) or swaps rows (for left rotation) to achieve
     * the correct orientation of the tetromino after rotation.
     * <p>
     * @param grid The current grid representation of the tetromino
     * @param flag Direction of rotation (ROTATE_R or ROTATE_L)
     * @return The rotated grid
     */
    public boolean[][] rotate(DirectionFlag flag) {
        boolean[][] newGrid = MatrixTransformations.transposeMatrix(grid);
        if (flag == DirectionFlag.ROTATE_R) {
            MatrixTransformations.swapColumns(newGrid);
        } else if (flag == DirectionFlag.ROTATE_L) {
            MatrixTransformations.swapRows(newGrid);
        }
        return newGrid;
    }

    /*
        *DISCLAMER: The Tetromino is define as N*N grid, where N is the largest dimension of the piece (e.g., 4 for I, 3 for T, etc.). This allows for a consistent rotation logic across all pieces.
        * Enum representing the 7 standard Tetris tetromino types, each with its own shape, color, and int value for easy retrieval.
        * Each type defines a 2D boolean grid where 'true' represents a block and
        * 'false' represents empty space.
        * The color field assigns a specific color to each tetromino type for rendering purposes.
        * The intValue is a unique identifier for each tetromino type, used for easy retrieval from the map.
        * The static block initializes a map that allows for quick lookup of tetromino types based on their integer value, facilitating the random generation of pieces in the factory method.
        * @param grid The shape of the tetromino represented as a 2D boolean array.
        * @param color The color associated with the tetromino type.
        * @param intValue A unique identifier for the tetromino type.
     */
    enum TETROMINO_TYPE {

        I(new boolean[][]{
            {false, false, false, false},
            {true, true, true, true},
            {false, false, false, false},
            {false, false, false, false}
        },
                Color.CYAN, 0),
        J(new boolean[][]{
            {true, false, false},
            {true, true, true},
            {false, false, false}
        },
                Color.BLUE, 1),
        L(new boolean[][]{
            {false, false, true},
            {true, true, true},
            {false, false, false}
        },
                Color.ORANGE, 2),
        O(new boolean[][]{
            {true, true},
            {true, true}
        },
                Color.YELLOW, 3),
        S(new boolean[][]{
            {false, true, true},
            {true, true, false},
            {false, false, false}
        },
                Color.GREEN, 4),
        T(new boolean[][]{
            {false, true, false},
            {true, true, true},
            {false, false, false}
        }, Color.MAGENTA, 5),
        Z(new boolean[][]{
            {true, true, false},
            {false, true, true},
            {false, false, false}
        }, Color.RED, 6);

        private final Color color;
        private final boolean[][] grid;
        private final int intValue;

        TETROMINO_TYPE(boolean[][] grid, Color color, int intValue) {
            this.color = color;
            this.grid = grid;
            this.intValue = intValue;
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
