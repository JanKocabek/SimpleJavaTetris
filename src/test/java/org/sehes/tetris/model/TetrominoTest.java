package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.config.GameParameters;

class TetrominoTest {
    private final TetrominoFactory factory = new TetrominoFactory();

    @Test
    void testTetrominoFactory_ValidTetrominoCreation() {
        // given
        Tetromino tetromino = factory.createNewRandomTetromino(GameParameters.SPAWN_POINT);
        // when
        // then
        assertNotNull(tetromino, "tetrominoFactory should not return null");
        assertEquals(GameParameters.SPAWN_POINT.x(), tetromino.getPositionX());
        assertEquals(GameParameters.SPAWN_POINT.y(), tetromino.getPositionY());
        assertNotNull(tetromino.getType());
        assertNotNull(tetromino.getStateCord());
    }

    @Test
    void shouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> factory.createNewRandomTetromino(null),
                "tetrominoFactory should throw IllegalArgumentException for null input");
    }

    @Test
    void testPixelCoordinatesShapeConsistency() {
        // given
        Tetromino tetromino = factory.createNewRandomTetromino(GameParameters.SPAWN_POINT);
        // when
        var stateCoords = tetromino.getStateCord(); // relative grid coordinates of each block
        var pixelCoordinates = tetromino.getPixelCoordinates(); // pixel-based coordinates used for rendering

        // then
        assertNotNull(stateCoords);
        assertNotNull(pixelCoordinates);
        assertEquals(stateCoords.size(), pixelCoordinates.length / 2);
    }

}
