package org.sehes.tetris.model;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import org.sehes.tetris.config.GameParameters;

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
    private static final TetrominoType[] TETROMINO_TYPES = TetrominoType.values();

    public static Tetromino tetrominoFactory(final Coordinate position) {
        final int tetrominoType = random.nextInt(TETROMINO_TYPES.length);
        return new Tetromino(TETROMINO_TYPES[tetrominoType], position);
    }

    private final Color color;// X column, Y row
    private int positionX;
    private int positionY;
    private List<Coordinate> stateCoordination;
    private int rotationState; // number represent curent rotation state of the tetromino
    private final int[] shape;
    private final int[] pixelCoordinates;// pixel coordination of the blocks for the drawing

    private final TetrominoType type;

    private Tetromino(final TetrominoType type, final Coordinate spawnPosition) {
        if (type == null || spawnPosition == null) {
            throw new IllegalArgumentException("Tetromino type and spawn position cannot be null.");
        }
        this.rotationState = 0;
        color = type.getColor();
        stateCoordination = type.getTetrominoState(rotationState);
        this.positionX = spawnPosition.x();
        this.positionY = spawnPosition.y();
        this.type = type;
        shape = new int[stateCoordination.size() * 2];
        pixelCoordinates = new int[shape.length];
        setShape();
        updatePixelCoordinates();
    }

    public String getTypeValue() {
        return type.name();
    }

    public Color getColor() {
        return color;
    }

    public List<Coordinate> getStateCord() {
        return stateCoordination;
    }

    public int[] getPixelCoordinates() {
        return pixelCoordinates;
    }

    private void updatePixelCoordinates() {
        // +2 because we have x and y coordinate after each other
        int pX = positionX * GameParameters.BLOCK_SIZE;
        int pY = (positionY - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
        for (int i = 0; i < shape.length; i += 2) {
            pixelCoordinates[i] = pX + (shape[i] * GameParameters.BLOCK_SIZE);
            pixelCoordinates[i + 1] = pY + (shape[i + 1] * GameParameters.BLOCK_SIZE);
        }
    }

    /**
     * Returns a flattened 1D int array representing the shape of the Tetromino,
     * where each element is a pair of X and Y coordinates put after each other
     * 
     * @return a 1D int array representing the shape of the Tetromino
     */
    public int[] getShape() {
        return shape;
    }

    private void setShape() {
        if (stateCoordination.size() * 2 != shape.length) {
            throw new IllegalStateException("State coordination size mismatch");
        }
        for (int i = 0; i < stateCoordination.size(); i++) {
            Coordinate p = stateCoordination.get(i);
            shape[i * 2] = p.x();
            shape[i * 2 + 1] = p.y();
        }
    }

    /**
     * Returns the X (column) coordinate of the tetromino's position.
     *
     * @return The column position of the tetromino.
     */
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void move(final DirectionFlag flag) {
        if (flag == null) {
            return;
        }
        positionX += flag.getX();
        positionY += flag.getY();
        updatePixelCoordinates();
    }

    /**
     * grab the next or previous state of the tetromino from {@link TetrominoType}
     * and return as unmodifiable list of points.
     * <p>
     *
     * @param flag ROTATE_R for next state, ROTATE_L for previous state
     * @return the next or previous state of the tetromino based on the flag
     */
    public List<Coordinate> rotate(final DirectionFlag flag) {
        return flag == DirectionFlag.ROTATE_R ? type.getNextState(rotationState) : type.getPreviousState(rotationState);
    }

    /**
     * Sets the state of the Tetromino to the given coordinates and rotation flag.
     * If the flag is ROTATE_R, it sets the next state of the Tetromino based on the given coordinates.
     * If the flag is ROTATE_L, it sets the previous state of the Tetromino based on the given coordinates.
     * If the flag is neither ROTATE_R nor ROTATE_L, it does nothing.
     * After setting the state, it updates the shape and pixel coordinates accordingly.
     * @param coordinates the new state of the Tetromino
     * @param directionFlag the rotation flag to determine the next or previous state of the Tetromino
     */
    void setState(final List<Coordinate> coordinates, final DirectionFlag directionFlag) {
        this.stateCoordination = coordinates;
        switch (directionFlag) {
            case ROTATE_R:
                setNextState();
                break;
            case ROTATE_L:
                setPreviousState();
                break;
            default:
                // do nothing
        }
        setShape();
        updatePixelCoordinates();
    }

    /**
     * Sets the next state of the tetromino by incrementing the current state.
     * its modulus of 4 because there are 4 possible states and modulo 4 return the
     * value between 0 and 3, this makes sure that the state is always between 0 and
     * 3
     */
    private void setNextState() {
        rotationState = (rotationState + 1) % 4;// 0,1,2,3
    }

    /**
     * Sets the previous state of the tetromino by decrementing the current state.
     * its +3 because 0+3= 3 and modulo of 4 makes it left 3 which assure that its
     * return previous position
     *
     * @see #setNextState()
     */
    private void setPreviousState() {
        rotationState = (rotationState + 3) % 4;// 0,3,2,1
    }

    // methods for junit testing purpose
    static Tetromino spawnSpecificTetromino(final TetrominoType tetrominoType, final Coordinate startPos) {
        return new Tetromino(tetrominoType, startPos);
    }

    int getCurrentState() {
        return rotationState;
    }
}
