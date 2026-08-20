package org.sehes.tetris.model;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.controller.ScoreManager;
import org.sehes.tetris.model.score.LockPieceEvent;
import org.sehes.tetris.model.score.TSpin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sehes.tetris.model.TetrominoFixtures.SpawnedTetromino;
import static org.sehes.tetris.model.TetrominoFixtures.spawn;
import static org.sehes.tetris.model.TetrominoFixtures.spawnT;
import static org.sehes.tetris.model.UtilForTests.getFullBoard;
import static org.sehes.tetris.model.UtilForTests.prepareBoard;

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
    private static final String T_SPIN_BOARD = getFullBoard("""
            ###II#####
            I###I#####
            II#II#####""");

    // =========================================================================
    // T-Spin Logic Tests
    // =========================================================================

    @Test
    void lockingTWithoutRotation_withThreeOccupiedCorners_returnsNone() {
        // This board has three occupied corners around the T pivot.  Corner
        // occupancy alone must not classify a lock as a T-spin: the last action
        // also has to be a rotation.
        assertTSpin(
                T_SPIN_BOARD,
                spawnT(new Coordinate(2, 20), Orientation.WEST, null),
                Orientation.WEST,
                TSpin.NONE,
                0,
                0
        );
    }

    @Test
    void lockingTAfterClockwiseRotation_withOneFrontAndTwoBackCorners_returnsMini() {
        //   N
        // W   E
        //   S

        assertTSpin(
                T_SPIN_BOARD,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.CLOCKWISE),
                Orientation.NORTH,
                TSpin.MINI,
                0,
                100
        );
    }

    @Test
    void lockingTAfterCounterClockwiseRotation_withOneBackAndTwoFrontCorners_returnsFull() {
        //   N
        // W   E
        //   S

        assertTSpin(
                T_SPIN_BOARD,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE),
                Orientation.SOUTH,
                TSpin.FULL,
                0,
                400
        );
    }

    // =========================================================================
    // T-Spin Scoring Pipeline Tests
    // =========================================================================

    @Test
    void testScoreTSpinFullNoLines() {
        assertTSpin(
                T_SPIN_BOARD,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE),
                Orientation.SOUTH,
                TSpin.FULL,
                0,
                400
        );
    }

    @Test
    void testScoreTSpinFullSingle() {
        final var board = getFullBoard("""
                II#II#####
                I###I#####
                II#IIIIIII
                """);

        assertTSpin(
                board,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE),
                Orientation.SOUTH,
                TSpin.FULL,
                1,
                800
        );
    }

    @Test
    void testScoreTSpinFullDouble() {
        final var board = getFullBoard("""
                II#II#####
                I###IIIIII
                II#IIIIIII
                """);

        assertTSpin(
                board,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.COUNTER_CLOCKWISE),
                Orientation.SOUTH,
                TSpin.FULL,
                2,
                1200
        );
    }

    @Test
    void testScoreTSpinFullTriple() {
        final var board = getFullBoard("""
                        ########I#
                        ########II
                        #########I
                        IIIIIIII#I
                        IIIIIII##I
                        IIIIIIII#I
                """);

        assertTSpin(
                board,
                spawnT(new Coordinate(7, 18), Orientation.NORTH, RotationFlag.COUNTER_CLOCKWISE),
                Orientation.WEST,
                TSpin.FULL,
                3,
                1600
        );
    }

    @Test
    void testScoreTSpinMiniNoLines() {
        assertTSpin(
                T_SPIN_BOARD,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.CLOCKWISE),
                Orientation.NORTH,
                TSpin.MINI,
                0,
                100
        );
    }

    @Test
    void testScoreTSpinMiniSingle() {
        final var board = getFullBoard("""
                ###II#####
                I###IIIIII
                II#II#####""");

        assertTSpin(
                board,
                spawnT(new Coordinate(2, 20), Orientation.WEST, RotationFlag.CLOCKWISE),
                Orientation.NORTH,
                TSpin.MINI,
                1,
                200
        );
    }

    @Test
    void testScoreTSpinMiniDouble() {
        final var board = getFullBoard("""
                ########II
                #######III
                #########I
                ########II
                IIIIII##II
                IIIIIII#II
                """);

        assertTSpin(
                board,
                spawnT(new Coordinate(7, 18), Orientation.SOUTH, RotationFlag.CLOCKWISE),
                Orientation.WEST,
                TSpin.MINI,
                2,
                400
        );
    }

    // =========================================================================
    // Unified Reusable Helper Methods & Configurations
    // =========================================================================

    public static TSpinTestResult assertTSpin(
            String initialBoard,
            SpawnedTetromino spawnedTetromino,
            Orientation expectedOrientation,
            TSpin expectedTSpin
    ) {
        return assertTSpin(new TSpinTestCase(
                initialBoard,
                spawnedTetromino,
                expectedOrientation,
                expectedTSpin,
                null,
                null,
                null
        ));
    }

    public static TSpinTestResult assertTSpin(
            String initialBoard,
            SpawnedTetromino spawnedTetromino,
            Orientation expectedOrientation,
            TSpin expectedTSpin,
            Integer expectedLinesCleared,
            Integer expectedScore
    ) {
        return assertTSpin(new TSpinTestCase(
                initialBoard,
                spawnedTetromino,
                expectedOrientation,
                expectedTSpin,
                expectedLinesCleared,
                expectedScore,
                null
        ));
    }

    public static TSpinTestResult assertTSpin(
            String initialBoard,
            Coordinate spawnCord,
            Orientation startOrie,
            RotationFlag rotation,
            Orientation expectedOrientation,
            TSpin expectedTSpin,
            Integer expectedLinesCleared,
            Integer expectedScore
    ) {
        return assertTSpin(new TSpinTestCase(
                initialBoard,
                spawnT(spawnCord, startOrie, rotation),
                expectedOrientation,
                expectedTSpin,
                expectedLinesCleared,
                expectedScore,
                null
        ));
    }

    /**
     * Unified test runner method for T-Spin logic and scoring tests.
     */
    public static TSpinTestResult assertTSpin(TSpinTestCase testCase) {
        final GameBoard gameBoard = prepareBoard(testCase.initialBoard());
        final SpawnedTetromino spawned = testCase.spawnedTetromino();
        final Tetromino tetromino = spawned.tetromino();

        assertThat(gameBoard.trySpawnTetromino(tetromino))
                .withFailMessage("Tetromino failed to spawn on board")
                .isTrue();

        if (spawned.rotation() != null) {
            assertThat(gameBoard.tryRotatePiece(spawned.rotation()))
                    .withFailMessage("Tetromino failed to rotate")
                    .isTrue();
        }

        if (testCase.expectedOrientation() != null) {
            assertThat(tetromino.getCurrentOrientation())
                    .withFailMessage("Expected orientation: %s, but was: %s", testCase.expectedOrientation(), tetromino.getCurrentOrientation())
                    .isEqualTo(testCase.expectedOrientation());
        }

        gameBoard.lockTetrominoInPlace();
        gameBoard.clearLines();

        final var lastAction = gameBoard.getLastAction();

        if (testCase.expectedTSpin() != null) {
            assertThat(lastAction.tSpin())
                    .withFailMessage("Expected T-Spin: %s, but was: %s", testCase.expectedTSpin(), lastAction.tSpin())
                    .isEqualTo(testCase.expectedTSpin());
        }

        if (testCase.expectedLinesCleared() != null) {
            assertThat(lastAction.linesCleared())
                    .withFailMessage("Expected lines cleared: %d, but was: %d", testCase.expectedLinesCleared(), lastAction.linesCleared())
                    .isEqualTo(testCase.expectedLinesCleared());
        }

        final ScoreManager scoreMgr = testCase.scoreManager() != null ? testCase.scoreManager() : new ScoreManager();
        scoreMgr.updateScore(new LockPieceEvent(lastAction.linesCleared(), lastAction.tSpin()));

        if (testCase.expectedScore() != null) {
            assertThat(scoreMgr)
                    .extracting("score")
                    .isEqualTo(testCase.expectedScore());
        }

        return new TSpinTestResult(gameBoard, tetromino, lastAction.tSpin(), lastAction.linesCleared(), scoreMgr);
    }

    public record TSpinTestCase(
            String initialBoard,
            SpawnedTetromino spawnedTetromino,
            Orientation expectedOrientation,
            TSpin expectedTSpin,
            Integer expectedLinesCleared,
            Integer expectedScore,
            ScoreManager scoreManager
    ) {
        public static TSpinTestBuilder builder() {
            return new TSpinTestBuilder();
        }

        public static class TSpinTestBuilder {
            private String initialBoard;
            private SpawnedTetromino spawnedTetromino;
            private Orientation expectedOrientation;
            private TSpin expectedTSpin;
            private Integer expectedLinesCleared;
            private Integer expectedScore;
            private ScoreManager scoreManager;

            public TSpinTestBuilder initialBoard(String initialBoard) {
                this.initialBoard = initialBoard;
                return this;
            }

            public TSpinTestBuilder spawnedTetromino(SpawnedTetromino spawnedTetromino) {
                this.spawnedTetromino = spawnedTetromino;
                return this;
            }

            public TSpinTestBuilder spawnedTetromino(Coordinate spawnCord, Orientation startOrie, RotationFlag rotation) {
                this.spawnedTetromino = spawnT(spawnCord, startOrie, rotation);
                return this;
            }

            public TSpinTestBuilder spawnedTetromino(TetrominoType type, Coordinate spawnCord, Orientation startOrie, RotationFlag rotation) {
                this.spawnedTetromino = spawn(type, spawnCord, startOrie, rotation);
                return this;
            }

            public TSpinTestBuilder expectedOrientation(Orientation expectedOrientation) {
                this.expectedOrientation = expectedOrientation;
                return this;
            }

            public TSpinTestBuilder expectedTSpin(TSpin expectedTSpin) {
                this.expectedTSpin = expectedTSpin;
                return this;
            }

            public TSpinTestBuilder expectedLinesCleared(Integer expectedLinesCleared) {
                this.expectedLinesCleared = expectedLinesCleared;
                return this;
            }

            public TSpinTestBuilder expectedScore(Integer expectedScore) {
                this.expectedScore = expectedScore;
                return this;
            }

            public TSpinTestBuilder scoreManager(ScoreManager scoreManager) {
                this.scoreManager = scoreManager;
                return this;
            }

            public TSpinTestCase build() {
                return new TSpinTestCase(
                        initialBoard,
                        spawnedTetromino,
                        expectedOrientation,
                        expectedTSpin,
                        expectedLinesCleared,
                        expectedScore,
                        scoreManager
                );
            }
        }
    }

    public record TSpinTestResult(
            GameBoard gameBoard,
            Tetromino tetromino,
            TSpin tSpin,
            int linesCleared,
            ScoreManager scoreManager
    ) {
    }
}
