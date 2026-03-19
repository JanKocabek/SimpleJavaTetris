package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.sehes.tetris.config.GameParameters;

class GameBoardDiffblueTest {
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
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, new Coordinate(10, 22)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(2).x());
        assertEquals(2, stateCord.get(3).x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertEquals(Orientation.WEST, currentTetromino.getNextOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{300, 600, 330, 600, 270, 600, 360, 600}, currentTetromino.getPixelCoordinates());
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
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(Integer.MIN_VALUE, -2147483647)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(2).x());
        assertEquals(2, stateCord.get(3).x());
        assertEquals(Orientation.EAST, currentTetromino.getNextOrientation());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{0, -30, 30, -30, -30, -30, 60, -30}, currentTetromino.getPixelCoordinates());
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
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.O, GameParameters.SPAWN_POINT));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        assertNull(currentTetromino.getNextOrientation());
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(2).x());
        assertEquals(0, stateCord.get(3).x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{90, -60, 90, -30, 120, -30, 120, -60}, currentTetromino.getPixelCoordinates());
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
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(Integer.MIN_VALUE, 3)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(2).x());
        assertEquals(2, stateCord.get(3).x());
        assertEquals(Orientation.EAST, currentTetromino.getNextOrientation());
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
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.J, new Coordinate(Integer.MIN_VALUE, 3)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(2).x());
        assertEquals(1, stateCord.get(3).x());
        assertEquals(Orientation.EAST, currentTetromino.getNextOrientation());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{-30, 0, -30, 30, 0, 30, 30, 30}, currentTetromino.getPixelCoordinates());
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
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino NextOrientation is {@code
     *       WEST}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino NextOrientation is 'WEST'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoNextOrientationIsWest() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(
                        TetrominoType.I, new Coordinate(Integer.MIN_VALUE, 3)));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(2).x());
        assertEquals(2, stateCord.get(3).x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertEquals(Orientation.WEST, currentTetromino.getNextOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{0, 30, 30, 30, -30, 30, 60, 30}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino PositionX is nine.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino PositionX is nine")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoPositionXIsNine() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, new Coordinate(10, 3)));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).x());
        Coordinate getResult = stateCord.get(2);
        assertEquals(0, getResult.x());
        assertEquals(0, stateCord.get(3).x());
        assertEquals(1, getResult.y());
        assertEquals(9, currentTetromino.getPositionX());
        assertEquals(Orientation.WEST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{270, 30, 270, 0, 270, 60, 270, 90}, currentTetromino.getPixelCoordinates());
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
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        Coordinate getResult = stateCord.get(0);
        assertEquals(1, getResult.x());
        assertEquals(1, stateCord.get(2).x());
        assertEquals(1, stateCord.get(3).x());
        assertEquals(1, getResult.y());
        assertEquals(Orientation.EAST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{150, 0, 150, -60, 150, -30, 150, 30}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino StateCord second x is zero.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); then GameBoard (default constructor) CurrentTetromino StateCord second x is zero")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_thenGameBoardCurrentTetrominoStateCordSecondXIsZero() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        gameBoard.tryRotatePiece(RotationFlag.COUNTER_CLOCKWISE);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(0, stateCord.get(1).x());
        Coordinate getResult = stateCord.get(2);
        assertEquals(0, getResult.x());
        assertEquals(0, stateCord.get(3).x());
        assertEquals(1, getResult.y());
        assertEquals(Orientation.WEST, currentTetromino.getCurrentOrientation());
        assertArrayEquals(
                new int[]{120, -30, 120, -60, 120, 0, 120, 30}, currentTetromino.getPixelCoordinates());
    }

    /**
     * Test {@link GameBoard#tryRotatePiece(RotationFlag)}.
     *
     * <ul>
     *   <li>When {@code null}.
     *   <li>Then {@link GameBoard} (default constructor) CurrentTetromino NextOrientation is {@code
     *       null}.
     * </ul>
     *
     * <p>Method under test: {@link GameBoard#tryRotatePiece(RotationFlag)}
     */
    @Test
    @DisplayName(
            "Test tryRotatePiece(RotationFlag); when 'null'; then GameBoard (default constructor) CurrentTetromino NextOrientation is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"boolean GameBoard.tryRotatePiece(RotationFlag)"})
    void testTryRotatePiece_whenNull_thenGameBoardCurrentTetrominoNextOrientationIsNull() {
        // Arrange
        GameBoard gameBoard = new GameBoard();
        gameBoard.spawnTetrominoForTestOnly(
                TetrominoFactory.spawnSpecificTetromino(TetrominoType.I, GameParameters.SPAWN_POINT));

        // Act
        boolean actualTryRotatePieceResult = gameBoard.tryRotatePiece(null);

        // Assert
        Tetromino currentTetromino = gameBoard.getCurrentTetromino();
        assertNull(currentTetromino.getNextOrientation());
        List<Coordinate> stateCord = currentTetromino.getStateCord();
        assertEquals(4, stateCord.size());
        assertEquals(-1, stateCord.get(2).x());
        assertEquals(2, stateCord.get(3).x());
        assertEquals(Orientation.NORTH, currentTetromino.getCurrentOrientation());
        assertFalse(actualTryRotatePieceResult);
        assertArrayEquals(
                new int[]{120, -30, 150, -30, 90, -30, 180, -30}, currentTetromino.getPixelCoordinates());
    }
}
