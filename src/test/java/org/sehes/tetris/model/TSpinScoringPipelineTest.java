package org.sehes.tetris.model;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sehes.tetris.controller.ScoreManager;
import org.sehes.tetris.model.score.LockPieceEvent;
import org.sehes.tetris.model.score.TSpin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sehes.tetris.model.UtilForTests.getFullBoard;
import static org.sehes.tetris.model.UtilForTests.prepareBoard;

class TSpinScoringPipelineTest {

    private ScoreManager scoreManager;


    @BeforeEach
    void setUp() {
        scoreManager = new ScoreManager();
    }

    @Test
    void testScoreTSpinFullNoLines() {
        //arrange:
        final String T_SPIN_BOARD = getFullBoard("""
                ###II#####
                I###I#####
                II#II#####""");

        SpawnedTetromino spawnedTetromino = spawnTetromino(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE);
        final GameBoard board = prepareBoard(T_SPIN_BOARD);
        assertThat(board.trySpawnTetromino(spawnedTetromino.t())).isTrue();
        //act
        assertThat(board.tryRotatePiece(spawnedTetromino.rotation())).isTrue();
        assertThat(spawnedTetromino.t().getCurrentOrientation()).isEqualTo(Orientation.SOUTH);
        board.lockTetrominoInPlace();
        board.clearLines();
        //assert
        var action = board.getLastAction();
        assertThat(action.tSpin()).isEqualTo(TSpin.FULL);
        assertThat(action.linesCleared()).isZero();
        scoreManager.updateScore(new LockPieceEvent(action.linesCleared(), action.tSpin()));
        assertThat(scoreManager).extracting("score").isEqualTo(400);
    }


    @Test
    void testScoreTSpinFullSingle() {
        final var T_SPIN_SINGLE = getFullBoard("""
                II#II#####
                I###I#####
                II#IIIIIII
                """);

        SpawnedTetromino spawnedTetromino = spawnTetromino(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE);
        final GameBoard board = prepareBoard(T_SPIN_SINGLE);
        assertThat(board.trySpawnTetromino(spawnedTetromino.t())).withFailMessage("tetromino fail to spawn").isTrue();
        //act
        assertThat(board.tryRotatePiece(spawnedTetromino.rotation())).withFailMessage("tetromino fail to rotate").isTrue();
        assertThat(spawnedTetromino.t().getCurrentOrientation()).isEqualTo(Orientation.SOUTH);
        board.lockTetrominoInPlace();
        board.clearLines();
        //assert
        var action = board.getLastAction();
        assertThat(action.tSpin()).isEqualTo(TSpin.FULL);
        assertThat(action.linesCleared()).isOne();
        scoreManager.updateScore(new LockPieceEvent(action.linesCleared(), action.tSpin()));
        assertThat(scoreManager).extracting("score").isEqualTo(800);
    }

    private TSpinScoringPipelineTest.@NonNull SpawnedTetromino spawnTetromino(Coordinate spawnCord, Orientation startOrie, RotationFlag rotation) {
        final Tetromino t = new Tetromino(TetrominoType.T, spawnCord);
        t.setNewState(ShapeProvider.getTetrominoState(TetrominoType.T, startOrie), startOrie);
        return new SpawnedTetromino(rotation, t);
    }

    private record SpawnedTetromino(RotationFlag rotation, Tetromino t) {
    }

}
