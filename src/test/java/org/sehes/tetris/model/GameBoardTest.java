package org.sehes.tetris.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.sehes.tetris.config.GameParameters;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            IBoardView boardView = gameBoard.getBoardView();
            assertNotNull(boardView);
            assertEquals(GameParameters.COLUMNS, boardView.getWidth());
            assertEquals(GameParameters.ROWS, boardView.getHeight());
        }


        @Test
        void testBoardViewGetBlockContent() {
            IBoardView boardView = gameBoard.getBoardView();
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
            boolean result = gameBoard.tryRotatePiece(DirectionFlag.ROTATE_L);
            assertFalse(result);
        }

        @Test
        void testBoardViewGetBlockContentOutOfBounds() {
            IBoardView boardView = gameBoard.getBoardView();
            assertThrows(IndexOutOfBoundsException.class, () -> boardView.getBlockContent(-1, 0));
        }

        @Test
        void testTryAddBlockToBoardWithoutTetromino() {
            assertThrows(IllegalStateException.class, gameBoard::addBlockToBoard);
        }
    }

    @Test
    void testAddBlockToBoard() {
        //given
        gameBoard.trySetNewTetromino();
        Tetromino tetromino = gameBoard.getCurrentTetromino();
        //when
        gameBoard.addBlockToBoard();
        IBoardView boardView = gameBoard.getBoardView();
        //then
        assertNotNull(tetromino);
        assertNotNull(boardView);
    }

    @Test
    void testCheckAndClearLinesNoLines() {
        //when
        boolean result = gameBoard.checkAndClearLines();
        //then
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
            Point initialPos = gameBoard.getCurrentTetromino().getPosition();
            // when
            boolean moved = gameBoard.tryMovePiece(DirectionFlag.LEFT);
            // then
            assertTrue(moved);
            Point newPos = gameBoard.getCurrentTetromino().getPosition();
            assertEquals(initialPos.x - 1, newPos.x);
            assertEquals(initialPos.y, newPos.y);

        }

        @Test
        void testTryMovePieceRight() {
            // given
            gameBoard.trySetNewTetromino();
            Point initialPos = gameBoard.getCurrentTetromino().getPosition();
            // when
            boolean moved = gameBoard.tryMovePiece(DirectionFlag.RIGHT);
            // then
            assertTrue(moved);
            Point newPos = gameBoard.getCurrentTetromino().getPosition();
            assertEquals(initialPos.x + 1, newPos.x);
            assertEquals(initialPos.y, newPos.y);
        }

        @Test
        void testTryMovePieceDown() {
            // given
            gameBoard.trySetNewTetromino();
            Point initialPos = gameBoard.getCurrentTetromino().getPosition();
            // when
            boolean moved = gameBoard.tryMovePiece(DirectionFlag.DOWN);
            // then
            assertTrue(moved);
            Point newPos = gameBoard.getCurrentTetromino().getPosition();
            assertEquals(initialPos.y + 1, newPos.y);
            assertEquals(initialPos.x, newPos.x);

        }

    }

    @Nested
    @DisplayName("rotation cases")
    class RotationCases {

        /**
         * Tests that the position of the tetromino is not changed after rotation. it
         * tests both directions
         *
         * @param directionFlag The direction to rotate the tetromino.
         */
        @ParameterizedTest
        @EnumSource(value = DirectionFlag.class, names = {"ROTATE_L", "ROTATE_R"})
        void testPositionIsTheSameAfterRotation(DirectionFlag directionFlag) {
            // given
            Tetromino tetromino = Tetromino.spawnSpecficTetromino(tetromino_type.T, new Point(4, 1));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            Point initialPos = gameBoard.getCurrentTetromino().getPosition();
            // when
            boolean rotated = gameBoard.tryRotatePiece(directionFlag);
            // then
            assertTrue(rotated);
            Point newPos = gameBoard.getCurrentTetromino().getPosition();
            assertEquals(initialPos.x, newPos.x);
            assertEquals(initialPos.y, newPos.y);
        }

        @ParameterizedTest
        @EnumSource(value = tetromino_type.class, names = {"I", "O", "S", "Z", "L", "J", "T"})
        void testTryReturnIntoBaseState(tetromino_type type) {
            // given
            Tetromino tetromino = Tetromino.spawnSpecficTetromino(type, new Point(4, 2));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            var baseCord = tetromino.getStateCord();
            int tetrominoState = tetromino.getCurrentState();
            // when
            for (int i = 0; i < 4; i++) {
                gameBoard.tryRotatePiece(DirectionFlag.ROTATE_R);
            }
            // then
            List<Point> finalCord = gameBoard.getCurrentTetromino().getStateCord();
            assertEquals(tetrominoState, gameBoard.getCurrentTetromino().getCurrentState());
            assertEquals(baseCord, finalCord);


        }

        @Test
        void testTryRotatePieceRight() {
            // given
            Tetromino tetromino = Tetromino.spawnSpecficTetromino(tetromino_type.T, new Point(4, 1));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            // when
            boolean rotated = gameBoard.tryRotatePiece(DirectionFlag.ROTATE_R);
            // then
            assertTrue(rotated);
            List<Point> afterCord = gameBoard.getCurrentTetromino().getStateCord();
            List<Point> expected = List.of(
                    new Point(0, 0),
                    new Point(0, -1),
                    new Point(0, 1),
                    new Point(1, 0));
            assertEquals(4, afterCord.size());
            assertTrue(afterCord.containsAll(expected));

        }

        @Test
        void testTryRotatePieceLeft() {
            // given
            Tetromino tetromino = Tetromino.spawnSpecficTetromino(tetromino_type.T, new Point(4, 1));
            gameBoard.spawnTetrominoForTestOnly(tetromino);
            // when
            boolean rotated = gameBoard.tryRotatePiece(DirectionFlag.ROTATE_L);
            // then
            assertTrue(rotated);
            List<Point> afterCord = gameBoard.getCurrentTetromino().getStateCord();
            List<Point> expected = List.of(
                    new Point(0, 0),
                    new Point(0, 1),
                    new Point(0, -1),
                    new Point(-1, 0));
            assertEquals(4, afterCord.size());
            assertTrue(afterCord.containsAll(expected));
        }
    }

}
