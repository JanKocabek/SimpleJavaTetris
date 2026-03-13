package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.sehes.tetris.config.GameParameters;

class GameBoardTest {

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard();
    }

    @Nested
    @DisplayName("basic functionality")
    class BasicFunctionality {
        @Test
        void testGameBoardInitialization() {
            assertNotNull(gameBoard);
            assertEquals(0, gameBoard.getScore());
            assertNull(gameBoard.getCurrentTetromino());
        }

        @Test
        void testGetBoardView() {
            BoardView boardView = gameBoard.getBoardView();
            assertNotNull(boardView);
            assertEquals(GameParameters.COLUMNS, boardView.getWidth());
            assertEquals(GameParameters.ROWS, boardView.getHeight());
        }

        @Test
        void testBoardViewGetBlockContent() {
            BoardView boardView = gameBoard.getBoardView();
            assertEquals(BlockContent.EMPTY, boardView.getBlockContent(0, 0));
        }

        @Test
        void testTrySetNewTetromino() {
            boolean result = gameBoard.trySetNewTetromino();
            assertTrue(result);
            assertNotNull(gameBoard.getCurrentTetromino());
        }

        @Test
        void testGetCurrentTetrominoAfterSet() {
            gameBoard.trySetNewTetromino();
            Tetromino current = gameBoard.getCurrentTetromino();
            assertNotNull(current);
        }

    }

    @Nested
    @DisplayName("crash cases")
    class CrashCases {

        @Test
        void testTryMovePieceWithoutTetromino() {
            boolean result = gameBoard.tryMovePiece(DirectionFlag.LEFT);
            assertFalse(result);
        }

        @Test
        void testTryRotatePieceWithoutTetromino() {
            boolean result = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);
            assertFalse(result);
        }

        @Test
        void testBoardViewGetBlockContentOutOfBounds() {
            BoardView boardView = gameBoard.getBoardView();
            assertThrows(IndexOutOfBoundsException.class, () -> boardView.getBlockContent(-1, 0));
        }

        @Test
        void testTryAddBlockToBoardWithoutTetromino() {
            assertThrows(IllegalStateException.class, gameBoard::lockTetrominoInPlace);
        }
    }

    @Test
    void testAddBlockToBoard() {
        // given
        gameBoard.trySetNewTetromino();
        Tetromino tetromino = gameBoard.getCurrentTetromino();
        // when
        gameBoard.lockTetrominoInPlace();
        BoardView boardView = gameBoard.getBoardView();
        // then
        assertNotNull(tetromino);
        assertNotNull(boardView);
        boolean hasNonEmptyBlock = false;
        for (int row = 0; row < GameParameters.ROWS; row++) {
            for (int col = 0; col < GameParameters.COLUMNS; col++) {
                if (boardView.getBlockContent(row, col) != BlockContent.EMPTY) {
                    hasNonEmptyBlock = true;
                    break;
                }
            }
        }
        assertTrue(hasNonEmptyBlock, "Board should have at least one non-empty block after adding tetromino");
        assertNull(gameBoard.getCurrentTetromino(), "Current tetromino should be null after adding to board");
    }

    @Test
    void testCheckAndClearLinesNoLines() {
        // when
        boolean result = gameBoard.checkAndClearLines();
        // then
        assertFalse(result);
    }

    @Test
    void testScoreInitialization() {
        assertEquals(0, gameBoard.getScore());
    }

    @Test
    void testSetLineForTest() {
        // given
        gameBoard.fillLineForTestOnly();
        // when
        boolean result = gameBoard.checkAndClearLines();
        // then
        assertTrue(result);
    }

    @Test
    void testUpdateScoreSingleLine() {
        // given
        int initialScore = gameBoard.getScore();
        gameBoard.fillLineForTestOnly();
        // when
        gameBoard.checkAndClearLines();
        // then
        assertEquals(initialScore + 100, gameBoard.getScore());
    }

    @Nested
    @DisplayName("movement cases")
    class MovementCases {

        @Test
        void testTryMovePieceLeft() {
            // given
            gameBoard.trySetNewTetromino();
            Tetromino tetromino = gameBoard.getCurrentTetromino();
            Coordinate initialPos = new Coordinate(tetromino.getPositionX(), tetromino.getPositionY());
            // when
            boolean moved = gameBoard.tryMovePiece(DirectionFlag.LEFT);
            // then
            assertTrue(moved);
            Coordinate newPos = new Coordinate(tetromino.getPositionX(), tetromino.getPositionY());
            assertEquals(initialPos.x() - 1, newPos.x());
            assertEquals(initialPos.y(), newPos.y());

        }

        @Test
        void testTryMovePieceRight() {
            // given
            gameBoard.trySetNewTetromino();
            Tetromino tetromino = gameBoard.getCurrentTetromino();
            assertNotNull(tetromino);
            Coordinate initialPos = new Coordinate(tetromino.getPositionX(), tetromino.getPositionY());
            // when
            boolean moved = gameBoard.tryMovePiece(DirectionFlag.RIGHT);
            // then
            assertTrue(moved);
            Coordinate newPos = new Coordinate(tetromino.getPositionX(), tetromino.getPositionY());
            assertNotNull(newPos);
            assertEquals(initialPos.x() + 1, newPos.x());
            assertEquals(initialPos.y(), newPos.y());

        }

        @Test
        void testTryMovePieceDown() {
            // given
            gameBoard.trySetNewTetromino();
            Tetromino tetromino = gameBoard.getCurrentTetromino();
            Coordinate initialPos = new Coordinate(tetromino.getPositionX(), tetromino.getPositionY());
            // when
            boolean moved = gameBoard.tryMovePiece(DirectionFlag.DOWN);
            // then
            assertTrue(moved);
            Coordinate newPos = new Coordinate(tetromino.getPositionX(), tetromino.getPositionY());
            assertEquals(initialPos.x(), newPos.x());
            assertEquals(initialPos.y() + 1, newPos.y());

        }

    }

    @Nested
    @DisplayName("rotation cases")
    class RotationCases {

        /**
         * Tests that the position of the tetromino is not changed after rotation. it
         * tests both directions
         *
         * @param rotation The direction to rotate the tetromino.
         */
        @ParameterizedTest
        @EnumSource(value = RotationFlag.class, names = { "CLOCKWISE", "COUNTER_CLOCKWISE" })
        void testPositionIsTheSameAfterRotation(RotationFlag rotation) {
            // given
            Tetromino tetromino = TetrominoFactory.spawnSpecificTetromino(TetrominoType.T, new Coordinate(4, 1));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            Tetromino currentTetromino = gameBoard.getCurrentTetromino();
            Coordinate initialPos = new Coordinate(currentTetromino.getPositionX(), currentTetromino.getPositionY());
            // when
            boolean rotated = gameBoard.tryRotatePiece(rotation);
            // then
            assertTrue(rotated);
            Coordinate newPos = new Coordinate(currentTetromino.getPositionX(), currentTetromino.getPositionY());
            assertEquals(initialPos.x(), newPos.x());
            assertEquals(initialPos.y(), newPos.y());
        }

        @ParameterizedTest
        @EnumSource(value = TetrominoType.class, names = { "I", "O", "S", "Z", "L", "J", "T" })
        void testTryReturnIntoBaseState(TetrominoType type) {
            // given
            Tetromino tetromino = TetrominoFactory.spawnSpecificTetromino(type, new Coordinate(4, 2));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            var baseCord = tetromino.getStateCord();
            Orientation tetrominoState = tetromino.getCurrentState();
            // when
            for (int i = 0; i < 4; i++) {
                gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);
            }
            // then
            List<Coordinate> finalCord = gameBoard.getCurrentTetromino().getStateCord();
            assertEquals(tetrominoState, gameBoard.getCurrentTetromino().getCurrentState());
            assertEquals(baseCord, finalCord);

        }

        @Test
        void testTryRotatePieceRight() {
            // given
            Tetromino tetromino = TetrominoFactory.spawnSpecificTetromino(TetrominoType.T, new Coordinate(4, 1));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            // when
            boolean rotated = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);
            // then
            assertTrue(rotated);
            List<Coordinate> afterCord = gameBoard.getCurrentTetromino().getStateCord();
            List<Coordinate> expected = List.of(
                    new Coordinate(0, 0),
                    new Coordinate(0, -1),
                    new Coordinate(0, 1),
                    new Coordinate(1, 0));
            assertEquals(4, afterCord.size());
            assertTrue(afterCord.containsAll(expected));

        }

        @Test
        void testTryRotatePieceLeft() {
            // given
            Tetromino tetromino = TetrominoFactory.spawnSpecificTetromino(TetrominoType.T, new Coordinate(4, 1));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            // when
            boolean rotated = gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);
            // then
            assertTrue(rotated);
            List<Coordinate> afterCord = gameBoard.getCurrentTetromino().getStateCord();
            List<Coordinate> expected = List.of(
                    new Coordinate(0, 0),
                    new Coordinate(0, 1),
                    new Coordinate(0, -1),
                    new Coordinate(-1, 0));
            assertEquals(4, afterCord.size());
            assertTrue(afterCord.containsAll(expected));
        }
    }

}
