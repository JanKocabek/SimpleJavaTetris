package org.sehes.tetris.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
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

 /**
     * tetromino is defined by list of points where y is row and x is column
     * <p>
     * the base level is y = 0 and upper one is -1
     * center (pivot) is (0,0)
     * left side is x=-1, right side is x=1
     * <p>
     * The Tetromino is defined as List of points represent the tetromino shape,
     * color and int value The color field assigns a specific color to each
     * tetromino type for rendering purposes.
     * The intValue is a unique
     * identifier for each tetromino type, used for easy retrieval from the map.
     * The static block initializes a map that allows for quick lookup of
     * tetromino types based on their integer value, facilitating the random
     * generation of pieces in the factory method.
     * <p>
     * x is column, y is row
     *
     */
public class Tetromino {

    private static final Random random = new Random();

    public static Tetromino tetrominoFactory(final Point position) {
        final TetrominoType[] values = TetrominoType.values();
        final int tetrominoType = random.nextInt(values.length);
        return new Tetromino(values[tetrominoType], position);
    }

    private final Color color;// X column, Y row
    private final Point position;
    private List<Point> stateCoordination;
    private int state;

    private final TetrominoType type;

    private Tetromino(final TetrominoType type, final Point spawnPosition) {
        this.state = 0;
        color = type.getColor();
        stateCoordination = type.getTetrominoState(state);
        this.position = new Point(spawnPosition);
        this.type = type;

    }
    public String getTypeValue() {
        return type.name();
    }


    public Color getColor() {
        return color;
    }

    public List<Point> getStateCord() {
        return cloneCoordinates(stateCoordination);
    }

    /**
     * Returns the current position of the tetromino as an immutable Point
     * object. The X is the column coordinate, and the second is the Y row
     * coordinate.
     *
     * @see Point
     * @return A new Point object representing the current position of the
     *         tetromino.
     */
    public Point getPosition() {
        return new Point(position);
    }

    public void move(final DirectionFlag flag) {
        if (flag == null) {
            return;
        }
        position.x += flag.getX();
        position.y += flag.getY();
    }

    /**
     * grab the next or previous state of the tetromino from {@link TetrominoType} and return as unmodifiable list of points.
     * <p>
     *
     * @param flag ROTATE_R for next state, ROTATE_L for previous state
     * @return the next or previous state of the tetromino based on the flag
     */
    public List<Point> rotate(final DirectionFlag flag) {
        return flag == DirectionFlag.ROTATE_R ? type.getNextState(state) : type.getPreviousState(state);
    }

    void setState(final List<Point> coordinates, final DirectionFlag flag) {
        this.stateCoordination = coordinates;
        if (flag == DirectionFlag.ROTATE_R) {
            setNextState();
        } else if (flag == DirectionFlag.ROTATE_L) {
            setPreviousState();
        }
    }

    /**
     * Sets the next state of the tetromino by incrementing the current state.
     * its modulus of 4 because there are 4 possible states and modulo 4 return the
     * value between 0 and 3, this makes sure that the state is always between 0 and
     * 3
     */
    private void setNextState() {
        state = (state + 1) % 4;// 0,1,2,3
    }

    /**
     * Sets the previous state of the tetromino by decrementing the current state.
     * its +3 because 0+3= 3 and modulo of 4 makes it left 3 which assure that its
     * return previous position
     *
     * @see #setNextState()
     */
    private void setPreviousState() {
        state = (state + 3) % 4;// 0,3,2,1
    }

    /**
     * Helper method for making always deep copy of the points list
     *
     * @param coordinates current tetromino coordinates
     * @return deep copy of the input coordinates
     */

    private List<Point> cloneCoordinates(final List<Point> coordinates) {
        final List<Point> newCoordinates = new ArrayList<>(coordinates.size());
        for (final Point point : coordinates) {
            newCoordinates.add(new Point(point));
        }
        return newCoordinates;
    }

   

    // methods for junit testing purpose
    static Tetromino spawnSpecificTetromino(final TetrominoType tetrominoType, final Point startPos) {
        return new Tetromino(tetrominoType, startPos);
    }

    int getCurrentState() {
        return state;
    }
}
