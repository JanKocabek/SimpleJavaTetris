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
import static org.sehes.tetris.model.UtilForTests.printBoardState;

class TSpinScoringPipelineTest {

    private ScoreManager scoreManager;

    @BeforeEach
    void setUp() {
        scoreManager = new ScoreManager();
    }

    @Test
    void testScoreTSpinFullNoLines() {
        final String T_SPIN_BOARD = getFullBoard("""
                ###II#####
                I###I#####
                II#II#####""");

        tSpinFull_scoreTest(T_SPIN_BOARD, 0, 400);
    }

    @Test
    void testScoreTSpinFullSingle() {
        final var T_SPIN_SINGLE = getFullBoard("""
                II#II#####
                I###I#####
                II#IIIIIII
                """);

        tSpinFull_scoreTest(T_SPIN_SINGLE, 1, 800);
    }

    @Test
    void testScoreTSpinFullDouble() {
        final var T_SPIN_DOUBLE = getFullBoard("""
                II#II#####
                I###IIIIII
                II#IIIIIII
                """);

        tSpinFull_scoreTest(T_SPIN_DOUBLE, 2, 1200);
    }

    @Test
    void testScoreTSpinFullTriple() {
        final var T_SPIN_TRIPLE = getFullBoard("""
                        ########I#
                        ########II
                        #########I
                        IIIIIIII#I
                        IIIIIII##I
                        IIIIIIII#I
                """);
        final var tetromino = spawnTMino(new Coordinate(7, 18), Orientation.NORTH, RotationFlag.COUNTER_CLOCKWISE);
        tSpin_scoreTest(T_SPIN_TRIPLE, tetromino, Orientation.WEST, TSpin.FULL, 3, 1600);
    }

    @Test
    void testScoreTSpinMiniNoLines() {
        final String T_SPIN_BOARD = getFullBoard("""
                ###II#####
                I###I#####
                II#II#####""");

        SpawnedTetromino spawnedTetromino = spawnTMino(new Coordinate(2, 20), Orientation.WEST, RotationFlag.CLOCKWISE);
        tSpin_scoreTest(T_SPIN_BOARD, spawnedTetromino, Orientation.NORTH, TSpin.MINI, 0, 100);
    }

    @Test
    void testScoreTSpinMiniSingle() {
        final var T_SPIN_MINI_SINGLE = getFullBoard("""
                ###II#####
                I###IIIIII
                II#II#####""");

        SpawnedTetromino spawnedTetromino = spawnTMino(new Coordinate(2, 20), Orientation.WEST, RotationFlag.CLOCKWISE);
        tSpin_scoreTest(T_SPIN_MINI_SINGLE, spawnedTetromino, Orientation.NORTH, TSpin.MINI, 1, 200);
    }

    @Test
    void testScoreTSpinMiniDouble() {
        final var T_SPIN_MINI_DOUBLE = getFullBoard("""
                ########II
                #######III
                #########I
                ########II
                IIIIII##II
                IIIIIII#II
                """);
        final var tetromino = spawnTMino(new Coordinate(7, 18), Orientation.SOUTH, RotationFlag.CLOCKWISE);
        printBoardState(prepareBoard(T_SPIN_MINI_DOUBLE).getBoardView(), tetromino.t);
        tSpin_scoreTest(T_SPIN_MINI_DOUBLE, tetromino, Orientation.WEST, TSpin.MINI, 2, 400,true);

    }

    // =========================================================================
    // Unified Reusable Helper Methods
    // =========================================================================

    /**
     * Reusable helper method for default T-Spin scoring cases (WEST spawn rotated COUNTER_CLOCKWISE to SOUTH).
     */
    private void tSpinFull_scoreTest(String boardDescription, int expectedLinesCleared, int expectedScore) {
        SpawnedTetromino spawnedTetromino = spawnTMino(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE);
        tSpin_scoreTest(boardDescription, spawnedTetromino, Orientation.SOUTH, TSpin.FULL, expectedLinesCleared, expectedScore);
    }

    /**
     * Unified reusable helper method for any T-Spin scoring case.
     */
    private void tSpin_scoreTest(String boardDescription, SpawnedTetromino spawnedTetromino, Orientation expectedOrientation, TSpin expectedTSpin, int expectedLinesCleared, int expectedScore, boolean debugPrint) {
        final GameBoard board = prepareBoard(boardDescription);
        assertThat(board.trySpawnTetromino(spawnedTetromino.t())).withFailMessage("Tetromino failed to spawn").isTrue();

        if (spawnedTetromino.rotation() != null) {
            assertThat(board.tryRotatePiece(spawnedTetromino.rotation())).withFailMessage("Tetromino failed to rotate").isTrue();
        }

        if (expectedOrientation != null) {
            assertThat(spawnedTetromino.t().getCurrentOrientation()).isEqualTo(expectedOrientation);
        }
        if (debugPrint) {
            printBoardState(board.getBoardView(), spawnedTetromino.t());
        }
        board.lockTetrominoInPlace();
        board.clearLines();

        var action = board.getLastAction();

        assertThat(action.tSpin()).isEqualTo(expectedTSpin);
        assertThat(action.linesCleared()).isEqualTo(expectedLinesCleared);

        scoreManager.updateScore(new LockPieceEvent(action.linesCleared(), action.tSpin()));
        assertThat(scoreManager).extracting("score").isEqualTo(expectedScore);
    }

    private void tSpin_scoreTest(String boardDescription, SpawnedTetromino spawnedTetromino, Orientation expectedOrientation, TSpin expectedTSpin, int expectedLinesCleared, int expectedScore) {
        tSpin_scoreTest(boardDescription, spawnedTetromino, expectedOrientation, expectedTSpin, expectedLinesCleared, expectedScore, false);
    }

    private @NonNull SpawnedTetromino spawnTMino(Coordinate spawnCord, Orientation startOrie, RotationFlag rotation) {
        return spawnTetromino(TetrominoType.T, spawnCord, startOrie, rotation);
    }

    private @NonNull SpawnedTetromino spawnTetromino(TetrominoType type, Coordinate spawnCord, Orientation startOrie, RotationFlag rotation) {
        final Tetromino t = new Tetromino(type, spawnCord);
        t.setNewState(ShapeProvider.getTetrominoState(type, startOrie), startOrie);
        return new SpawnedTetromino(rotation, t);
    }

    private record SpawnedTetromino(RotationFlag rotation, Tetromino t) {
    }
}
