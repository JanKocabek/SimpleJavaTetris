package org.sehes.tetris.model;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.sehes.tetris.model.util.MatrixTransformations;

/**
 * The Tetromino class represents the individual Tetris pieces in the game. Each
 * Tetromino has a specific shape defined by a 2D boolean grid, a color for
 * rendering, and a position on the game board. The class provides methods for
 * moving and rotating the Tetromino, as well as a factory method for generating
 * random Tetromino pieces. The inner enum TETROMINO_TYPE defines the seven
 * standard Tetris pieces (I, J, L, O, S, T, Z), each with its own shape and
 * color. The Tetromino class interacts with the GameBoard to manage the current
 * piece's state and position during gameplay.
 */
public class Tetromino {

    private static final Random random = new Random();
    private final Color color;
    private final Point position;//X column, Y row
    private boolean[][] grid;

    public static Tetromino tetrominoFactory(Point position) {
        TETROMINO_TYPE[] values = TETROMINO_TYPE.values();
        int tetrominoType = random.nextInt(values.length);
        return new Tetromino(values[tetrominoType], position);
    }

    private Tetromino(TETROMINO_TYPE type, Point spawnPosition) {
        color = type.color;
        grid = type.grid;
        this.position = new Point(spawnPosition); // Create a new Point to avoid external modification
    }

    public Color getColor() {
        return color;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns the current position of the tetromino as an array of integers.
     * The first element is the X coordinate (column), and the second is the Y
     * coordinate (row).
     *
     * @return The position of the tetromino as [X, Y].
     */
    public Point getPosition() {
    return new Point(position);
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
        if (flag == null) {
            return;
        }
        position.x += flag.getX();
        position.y += flag.getY();
    }

    /**
     * Rotates the tetromino grid based on the direction flag
     * <p>
     * The method first transposes the grid to switch rows and columns. Then,
     * depending on the rotation direction (right or left), it either swaps
     * columns (for right rotation) or swaps rows (for left rotation) to achieve
     * the correct orientation of the tetromino after rotation.
     * <p>
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
