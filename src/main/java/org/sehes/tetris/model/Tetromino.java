package org.sehes.tetris.model;

import java.awt.Color;
import java.util.List;

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

    private int positionX;
    private int positionY;
    private List<Coordinate> stateCoordination;
    private Orientation rotationState; // enum represent current rotation state
    private final int[] pixelCoordinates;// pixel coordination of the blocks for the drawing
    private Orientation nextOrientation;
    private final TetrominoType type;

    Tetromino(final TetrominoType type, final Coordinate spawnPosition) {
        if (type == null || spawnPosition == null) {
            throw new IllegalArgumentException("Tetromino type and spawn position cannot be null.");
        }
        this.rotationState = Orientation.NORTH;
        this.positionX = spawnPosition.x();
        this.positionY = spawnPosition.y();
        this.type = type;
        updateStateCoordination();
        pixelCoordinates = new int[stateCoordination.size() * 2];
        updatePixelCoordinates();
    }

    public TetrominoType getType() {
        return type;
    }

    public Color getColor() {
        return type.getColor();
    }

    public List<Coordinate> getStateCord() {
        return stateCoordination;
    }

    public int[] getPixelCoordinates() {
        return pixelCoordinates;
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

    public void setPosition(final int x, final int y) {
        this.positionX = x;
        this.positionY = y;
        updatePixelCoordinates();
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
     * Rotates the Tetromino in the specified direction (clockwise or
     * counter-clockwise) and returns the new grid configuration.
     * The method first calculates the new orientation of the Tetromino based on the
     * given rotation flag.
     * Then, it retrieves the new grid configuration of the Tetromino using the
     * ShapeProvider class and the new orientation.
     * Finally, it returns the new grid configuration.
     * 
     * @param rotation enum representing the rotation direction
     *                 {@link RotationFlag} can be either {@code CLOCKWISE}
     *                 or {@code COUNTER_CLOCKWISE}
     * @return The new grid configuration of the Tetromino after rotation.
     */
    public List<Coordinate> getNextState(final RotationFlag rotation) {
        if(nextOrientation == null) {
            setNextOrientation(rotation);
        }
        return ShapeProvider.getTetrominoState(type, nextOrientation);
    }

    public Orientation getNextOrientation() {
        return nextOrientation;
    }

    /**
     * Sets the state of the Tetromino to the given coordinates and rotation flag.
     * If the flag is ROTATE_R, it sets the next state of the Tetromino based on the
     * given coordinates.
     * If the flag is ROTATE_L, it sets the previous state of the Tetromino based on
     * the given coordinates.
     * If the flag is neither ROTATE_R nor ROTATE_L, it does nothing.
     * After setting the state, it updates the shape and pixel coordinates
     * accordingly.
     * 
     * @param coordinates   the new state of the Tetromino
     * @param directionFlag the rotation flag to determine the next or previous
     *                      state of the Tetromino
     */
    void setState(final List<Coordinate> coordinates , final RotationFlag rotationFlag) {
        if (coordinates == null ) {
            throw new IllegalArgumentException("Coordinates and direction flag cannot be null.");
        }
        this.stateCoordination = coordinates;
        this.rotationState = nextOrientation!= null     ? nextOrientation :setNextOrientation(rotationFlag);
        nextOrientation = null;
        updatePixelCoordinates();
    }

    Orientation getCurrentOrientation() {
        return rotationState;
    }

    private void updateStateCoordination() {
        stateCoordination = ShapeProvider.getTetrominoState(type, rotationState);
    }

    /**
     * Updates the pixel coordinates of the tetromino based on its current state
     * and position. The method takes the state coordinates, scales them by the
     * block size, and then applies the position offset to get the final
     * pixel coordinates.
     */
    private void updatePixelCoordinates() {
        final int COORDS_PER_BLOCK = 2;
        final int pX = positionX * GameParameters.BLOCK_SIZE;
        final int pY = (positionY - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
        final int blockSize = GameParameters.BLOCK_SIZE;
        for (int i = 0; i < stateCoordination.size(); i++) {
            final int x = stateCoordination.get(i).x() * blockSize + pX;
            final int y = stateCoordination.get(i).y() * blockSize + pY;
            pixelCoordinates[i * COORDS_PER_BLOCK] = x;
            pixelCoordinates[(i * COORDS_PER_BLOCK) + 1] = y;
        }
    }

    public Orientation setNextOrientation(final RotationFlag rotation) {
        Orientation newOrientation = rotation == RotationFlag.CLOCKWISE ? rotationState.rotateClockwise()
                : rotationState.rotateCounterClockwise();
        nextOrientation = newOrientation;
        return newOrientation;
    }
}
