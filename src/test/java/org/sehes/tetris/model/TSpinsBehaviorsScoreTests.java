package org.sehes.tetris.model;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.model.score.TSpin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sehes.tetris.model.TestUtil.prepareBoard;


class TSpinsBehaviorsScoreTests {


    @Test
    void testCornerCalculation() {
        //arrange
        String board = """
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

        Tetromino t = new Tetromino(TetrominoType.T, new Coordinate(2, 20));
        t.setNewState(ShapeProvider.getTetrominoState(TetrominoType.T, Orientation.WEST), Orientation.WEST);
        GameBoard gameBoard = prepareBoard(board);
        gameBoard.trySpawnTetromino(t);
        //act
        gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);
        final var result = gameBoard.lockTetrominoInPlace();
        //assert
        assertThat(result).isEqualTo(TSpin.FULL);
    }
}
