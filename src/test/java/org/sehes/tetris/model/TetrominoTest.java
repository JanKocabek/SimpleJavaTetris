package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.config.GameParameters;

class TetrominoTest {

    @Test
    void testTetrominoFactory_ValidTetrominoCreation() {
        // given
        Tetromino tetromino = Tetromino.tetrominoFactory(GameParameters.SPAWN_POINT);
        // when
        // then
        assertNotNull(tetromino, "tetrominoFactory should not return null");
        assertEquals(GameParameters.SPAWN_POINT.x(), tetromino.getPositionX());
        assertEquals(GameParameters.SPAWN_POINT.y(), tetromino.getPositionY());
        assertNotNull(tetromino.getTypeValue());
        assertNotNull(tetromino.getStateCord());
    }

    @Test
    void shouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> Tetromino.tetrominoFactory(null),
                "tetrominoFactory should throw IllegalArgumentException for null input");
    }

    @Test
    void validTetrominoCreationFromFactory() {
        // given
        Tetromino tetromino = Tetromino.tetrominoFactory(GameParameters.SPAWN_POINT);
        // then
        assertNotNull(tetromino);
        assertEquals(GameParameters.SPAWN_POINT.x(), tetromino.getPositionX());
        assertEquals(GameParameters.SPAWN_POINT.y(), tetromino.getPositionY());
    }

    @Test
    void testPixelCoordinatesShapeConsistency() {
        // given
        Tetromino tetromino = Tetromino.tetrominoFactory(GameParameters.SPAWN_POINT);
        // when
        // when
        var stateCoords = tetromino.getStateCord(); // relative grid coordinates of each block
        int[] flatShape = tetromino.getShape(); // flattened shape representation
        var pixelCoordinates = tetromino.getPixelCoordinates(); // pixel-based coordinates used for rendering

        // then
        assertNotNull(stateCoords);
        assertNotNull(flatShape);
        assertNotNull(pixelCoordinates);
        // rely on the relationship between stateCoords and the flattened/pixel arrays
        // instead of hard-coding specific lengths
        assertEquals(stateCoords.size(), flatShape.length / 2);
        assertEquals(stateCoords.size(), pixelCoordinates.length / 2);
        assertEquals(8, flatShape.length);
        assertEquals(8, pixelCoordinates.length);
        assertEquals(stateCoords.size(), flatShape.length / 2);
        assertEquals(stateCoords.size(), pixelCoordinates.length / 2);
    }

}
