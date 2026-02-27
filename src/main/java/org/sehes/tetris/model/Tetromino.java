package org.sehes.tetris.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private List<Point> cord;

    public static Tetromino tetrominoFactory(Point position) {
        TETROMINO_TYPE[] values = TETROMINO_TYPE.values();
        int tetrominoType = random.nextInt(values.length);
        return new Tetromino(values[tetrominoType], position);
    }

    private Tetromino(TETROMINO_TYPE type, Point spawnPosition) {
        color = type.color;
        cord = type.points;
        this.position = new Point(spawnPosition); // Create a new Point to avoid external modification
    }

    public Color getColor() {
        return color;
    }

    public List<Point> getPoints() {
        return cord;
    }

    /**
     * Returns the current position of the tetromino as an immutable Point
     * object. The X is the column coordinate, and the second is the Y row
     * coordinate.
     *
     * @see Point
     * @return A new Point object representing the current position of the
     * tetromino.
     */
    public Point getPosition() {
        return new Point(position);
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
    public List<Point> rotate(DirectionFlag flag) {
        List<Point> newPoints = new ArrayList<>(cord);
        if (flag == DirectionFlag.ROTATE_R) {
            for (Point point : newPoints) {
                int tmp = point.x;
                point.x = point.y;
                point.y = -tmp;
            }
        } else if (flag == DirectionFlag.ROTATE_L) {
            for (Point point : newPoints) {
                int tmp = point.x;
                point.x = -point.y;
                point.y = tmp;
            }
        }
        return newPoints;
    }

    public void setTetromino(List<Point> points) {
        this.cord = points;
    }

    /**
     * The Tetromino is define as List of points represent the tetromino shape,
     * color and int value The color field assigns a specific color to each
     * tetromino type for rendering purposes. The intValue is a unique
     * identifier for each tetromino type, used for easy retrieval from the map.
     * The static block initializes a map that allows for quick lookup of
     * tetromino types based on their integer value, facilitating the random
     * generation of pieces in the factory method.
     * <p>
     * x is column, y is row
     *
     */
    enum TETROMINO_TYPE {

        I(List.of(new Point(0, 0),
                new Point(1, 0),
                new Point(-1, 0),
                new Point(2, 0)),
                Color.CYAN,
                0),
        J(List.of(new Point(-1, 1),
                new Point(-1, 0),
                new Point(0, 0),
                new Point(1, 0)),
                Color.BLUE,
                1),
        L(List.of(new Point(-1, 0),
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1)),
                Color.ORANGE,
                2),
        O(List.of(new Point(1, 1),
                new Point(1, 0),
                new Point(0, 0),
                new Point(0, 1)),
                Color.YELLOW,
                3),
        S(List.of(new Point(1, 1),
                new Point(0, 1),
                new Point(0, 0),
                new Point(-1, 0)),
                Color.GREEN,
                4),
        T(List.of(new Point(0, -1),
                new Point(0, 0),
                new Point(1, 0),
                new Point(-1, 0)),
                Color.MAGENTA,
                5),
        Z(List.of(
                new Point(-1, 1),
                new Point(0, 1),
                new Point(0, 0),
                new Point(1, 0)),
                Color.RED,
                6);

        private final List<Point> points;
        private final Color color;
        private final int intValue;

        /*
        points are 
         */
        TETROMINO_TYPE(List<Point> points, Color color,
                       int intValue
        ) {
            this.color = color;
            this.points = points;
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
