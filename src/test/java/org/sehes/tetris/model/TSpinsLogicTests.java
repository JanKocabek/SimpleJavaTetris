package org.sehes.tetris.model;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.model.score.TSpin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sehes.tetris.model.TestUtil.prepareBoard;


class TSpinsLogicTests {


    /*
     * '#' is empty and 'I' is a filled board cell. Coordinates grow right/down.
     *
     * The T starts facing WEST, then rotates clockwise to NORTH around P (2,20):
     *
     *        x: 0 1 2 3 4
     *   y=19    # # T I I       front corners: (1,19) empty, (3,19) filled
     *   y=20    I T P T I
     *   y=21    I I # I I       back corners:  (1,21) filled, (3,21) filled
     *
     * Therefore, the rotation leaves one front and two back corners filled: a MINI T-spin.
     */
    private static final String T_SPIN_BOARD = """
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ##########
            ###II#####
            I###I#####
            II#II#####
            """;

    @Test
    void lockingTAfterClockwiseRotation_withOneFrontAndTwoBackCorners_returnsMini() {
        //   N
        // W   E
        //   S

        // Arrange: WEST-facing T at P, with the annotated corner layout above.
        final Coordinate pivotCord = new Coordinate(2, 20);
        final Orientation startOrie = Orientation.WEST;
        final RotationFlag rotation = RotationFlag.CLOCKWISE;
        // so the T will be rotated to NORTH
        final Tetromino t = new Tetromino(TetrominoType.T, pivotCord);
        t.setNewState(ShapeProvider.getTetrominoState(TetrominoType.T, startOrie), startOrie);
        final GameBoard gameBoard = prepareBoard(T_SPIN_BOARD);

        assertThat(gameBoard.trySpawnTetromino(t)).isTrue();

        // Act: WEST --clockwise--> NORTH, then lock while rotation is the last action.
        assertThat(gameBoard.tryRotatePiece(rotation)).isTrue();
        assertThat(t.getCurrentOrientation()).isEqualTo(Orientation.NORTH);

        TSpin result = gameBoard.lockTetrominoInPlace();

        // Assert
        assertThat(result).isEqualTo(TSpin.MINI);
    }

    @Test
    void lockingTAfterCounterClockwiseRotation_withOneBackAndTwoFrontCorners_returnsFull() {
        //   N
        // W   E
        //   S

        // Arrange: WEST-facing T at P, with the annotated corner layout above.
        final Coordinate pivotCord = new Coordinate(2, 20);
        final Orientation startOrie = Orientation.WEST;
        final RotationFlag rotation = RotationFlag.COUNTER_CLOCKWISE;
        // so the T will be rotated to SOUTH after
        final Tetromino t = new Tetromino(TetrominoType.T, pivotCord);
        t.setNewState(ShapeProvider.getTetrominoState(TetrominoType.T, startOrie), startOrie);
        final GameBoard gameBoard = prepareBoard(T_SPIN_BOARD);

        assertThat(gameBoard.trySpawnTetromino(t)).isTrue();

        // Act: WEST --counterClockwise--> South, then lock while rotation is the last action.
        assertThat(gameBoard.tryRotatePiece(rotation)).isTrue();
        assertThat(t.getCurrentOrientation()).isEqualTo(Orientation.SOUTH);

        TSpin result = gameBoard.lockTetrominoInPlace();

        // Assert
        assertThat(result).isEqualTo(TSpin.FULL);
    }
}
