package org.sehes.tetris.model;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.sehes.tetris.config.GameParameters;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBoardDiffblueTest {


    /**
     * Test getters and setters.
     *
     * <p>Methods under test:
     *
     * <ul>
     *   <li>{@link GameBoard#trySpawnTetromino(Tetromino)}
     *   <li>{@link GameBoard#getBoardView()}
     *   <li>{@link GameBoard#getCurrentTetromino()}
     * </ul>
     */
    @Test
    @DisplayName("Test getters and setters")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({
            "org.sehes.tetris.model.BoardView GameBoard.getBoardView()",
            "Tetromino GameBoard.getCurrentTetromino()",
            "int GameBoard.getScore()",
            "void GameBoard.spawnTetrominoForTestOnly(Tetromino)"
    })
    void testGettersAndSetters() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        Tetromino tetromino =
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT);

        // Act
        gameBoard.trySpawnTetromino(tetromino);
        Tetromino actualCurrentTetromino = gameBoard.getCurrentTetromino();

        assertSame(tetromino, actualCurrentTetromino);
    }

    /**
     * Test {@link GameBoard#trySetNewTetromino()}.
     *
     * <p>Method under test: {@link GameBoard#trySetNewTetromino()}
     */
    @Test
    @DisplayName("Test trySetNewTetromino()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.trySetNewTetromino()"})
    void testTrySetNewTetromino() {
        // Arrange
        GameBoard gameBoard = new GameBoard();

        // Act
        boolean actualTrySetNewTetrominoResult = gameBoard.trySetNewTetromino();

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(2).y());
        assertEquals(1, currentTetromino.getPositionY());
        assertEquals(4, currentTetromino.getPositionX());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertTrue(actualTrySetNewTetrominoResult);
    }

    /**
     * Test {@link GameBoard#tryMovePiece(DirectionFlag)}.
     *
     * <ul>
     *   <li>Given {@link GameBoard} (default constructor).
     *   <li>When {@code DOWN}.
     *   <li>Then return {@code false}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryMovePiece(DirectionFlag)}
     */
    @Test
    @DisplayName(
            "Test tryMovePiece(DirectionFlag); given GameBoard (default constructor); when 'DOWN'; then return 'false'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryMovePiece(DirectionFlag)"})
    void testTryMovePiece_givenGameBoard_whenDown_ThrowExceptionWithoutTetromino() {
        // Arrange, Act and Assert
        assertThatNullPointerException().isThrownBy(() -> new GameBoard().trySoftDrop());
    }

    /**
     * Test {@link GameBoard#tryMovePiece(DirectionFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino PositionY is minus one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryMovePiece(DirectionFlag)}
     */
    @Test
    @DisplayName(
            "Test tryMovePiece(DirectionFlag); then GameBoard (default constructor) CurrentTetromino PositionY is minus one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryMovePiece(DirectionFlag)"})
    void testTryMovePiece_thenGameBoardCurrentTetrominoPositionYIsMinusOne() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        final var startPos = new Coordinate(-1, -1);
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, startPos));

        // Act
        boolean actualTryMovePieceResult = gameBoard.tryMovePiece(DirectionFlag.RIGHT);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        assertEquals(-1, currentTetromino.getPositionY());
        assertFalse(actualTryMovePieceResult);
        assertArrayEquals(
                new int[]{-40, -120, 0, -120, -80, -120, 40, -120},
                currentTetromino.getPixelCoordinates());
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece6() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.J, new Coordinate(0, 22)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(1, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{-30, 570, -30, 600, 0, 600, 30, 600}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.J, new Coordinate(0, 22)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        assertTrue(actualTryRotatePieceResult);
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(1, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{-40, 760, -40, 800, 0, 800, 40, 800}, currentTetromino.getPixelCoordinates());
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece22() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(4, 1)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(2, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{0, 30, 30, 30, -30, 30, 60, 30}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece5() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.O, GameParameters.SPAWN_POINT));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        Coordinate getResult = stateCord.get(3);
        assertEquals(-1, getResult.y());
        assertEquals(0, getResult.x());
        assertEquals(0, stateCord.get(1).y());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{120, -80, 120, -40, 160, -40, 160, -80}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece4() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(Integer.MIN_VALUE, 3)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(2, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{0, 40, 40, 40, -40, 40, 80, 40}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece3() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.J, new Coordinate(Integer.MIN_VALUE, 3)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(1, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{-40, 0, -40, 40, 0, 40, 40, 40}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece2() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(Integer.MIN_VALUE, -2147483647)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(2, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{0, -40, 40, -40, -40, -40, 80, -40}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Given {@link GameBoard} (default constructor).
     *   <li>When {@code CLOCKWISE}.
     *   <li>Then return {@code false}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); given GameBoard (default constructor); when 'CLOCKWISE'; then return 'false'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_givenGameBoard_whenClockwise_thenReturnFalse() {
        // Arrange, Act and Assert
        assertFalse(new GameBoard().tryRotatePiece(RotationFlag.CLOCKWISE));
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece32() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.O, GameParameters.SPAWN_POINT));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        Coordinate getResult = stateCord.get(3);
        assertEquals(-1, getResult.y());
        assertEquals(0, getResult.x());
        assertEquals(0, stateCord.get(1).y());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{90, -60, 90, -30, 120, -30, 120, -60}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord first x is one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord first x is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordFirstXIsOne() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(1, stateCord.get(0).x());
        assertEquals(1, stateCord.get(2).x());
        assertEquals(1, stateCord.get(3).x());
        assertEquals(Orientation.EAST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{200, 0, 200, -80, 200, -40, 200, 40}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>When {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag); when 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_whenNull() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(null);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(2, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{160, -40, 200, -40, 120, -40, 240, -40}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord second y is minus
     *       one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord second y is minus one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordSecondYIsMinusOne() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        Coordinate getResult = stateCord.get(1);
        assertEquals(-1, getResult.y());
        assertEquals(0, getResult.x());
        Coordinate getResult2 = stateCord.get(2);
        assertEquals(0, getResult2.x());
        Coordinate getResult3 = stateCord.get(3);
        assertEquals(0, getResult3.x());
        assertEquals(1, getResult2.y());
        assertEquals(2, getResult3.y());
        assertEquals(Orientation.WEST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{160, -40, 160, -80, 160, 0, 160, 40}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord second y is one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord second y is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordSecondYIsOne() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.J, new Coordinate(0, 3)));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(3).y());
        assertEquals(1, stateCord.get(1).y());
        assertEquals(1, currentTetromino.getPositionX());
        assertArrayEquals(
                new int[]{0, 80, 40, 80, 40, 40, 40, 0}, currentTetromino.getPixelCoordinates());
    }


    /**
     * Test {@link GameBoard#lockTetrominoInPlace()}.
     *
     * <ul>
     *   <li>Given {@link GameBoard} (default constructor).
     *   <li>Then throw {@link IllegalStateException}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#lockTetrominoInPlace()}
     */
    @Test
    @DisplayName(
            "Test lockTetrominoInPlace(); given GameBoard (default constructor); then throw IllegalStateException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void GameBoard.lockTetrominoInPlace()"})
    void testLockTetrominoInPlace_givenGameBoard_thenThrowIllegalStateException2() {
        // Arrange, Act and Assert
        GameBoard gameBoard = new GameBoard();
        assertThrows(IllegalStateException.class, gameBoard::lockTetrominoInPlace);
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece52() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(Integer.MIN_VALUE, -2147483647)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(2, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{0, -30, 30, -30, -30, -30, 60, -30}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#lockTetrominoInPlace()}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#lockTetrominoInPlace()}
     */
    @Test
    @DisplayName(
            "Test lockTetrominoInPlace(); then GameBoard (default constructor) CurrentTetromino is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"void GameBoard.lockTetrominoInPlace()"})
    void testLockTetrominoInPlace_thenGameBoardCurrentTetrominoIsNull() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.lockTetrominoInPlace();

        // Assert
        assertNull(gameBoard.getCurrentTetromino());
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord first x is one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord first x is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordFirstXIsOne3() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(1, stateCord.get(0).x());
        assertEquals(1, stateCord.get(2).x());
        assertEquals(1, stateCord.get(3).x());
        assertEquals(Orientation.EAST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{200, 0, 150, -60, 150, -30, 150, 30}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord first x is one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord first x is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordFirstXIsOne2() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(1, stateCord.get(0).x());
        assertEquals(1, stateCord.get(2).x());
        assertEquals(1, stateCord.get(3).x());
        assertEquals(Orientation.EAST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{150, 0, 150, -60, 150, -30, 150, 30}, currentTetromino.getPixelCoordinates());
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord second y is minus
     *       one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord second y is minus one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordSecondYIsMinusOne2() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        Coordinate getResult = stateCord.get(1);
        assertEquals(-1, getResult.y());
        assertEquals(0, getResult.x());
        Coordinate getResult2 = stateCord.get(2);
        assertEquals(0, getResult2.x());
        Coordinate getResult3 = stateCord.get(3);
        assertEquals(0, getResult3.x());
        assertEquals(1, getResult2.y());
        assertEquals(2, getResult3.y());
        assertEquals(Orientation.WEST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{120, -30, 120, -60, 120, 0, 120, 30}, currentTetromino.getPixelCoordinates());
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord second y is one.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord second y is one")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordSecondYIsOne2() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.J, new Coordinate(0, 3)));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(3).y());
        assertEquals(1, stateCord.get(1).y());
        assertEquals(1, currentTetromino.getPositionX());
        assertArrayEquals(
                new int[]{0, 60, 30, 60, 30, 30, 30, 0}, currentTetromino.getPixelCoordinates());
    }


    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>When {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName("Test tryRotatePiece(RotationFlag); when 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_whenNull2() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.trySpawnTetromino(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(null);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).y());
        Coordinate getResult = stateCord.get(3);
        assertEquals(0, getResult.y());
        assertEquals(2, getResult.x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{120, -30, 150, -30, 90, -30, 180, -30}, currentTetromino.getPixelCoordinates());
    }


}
