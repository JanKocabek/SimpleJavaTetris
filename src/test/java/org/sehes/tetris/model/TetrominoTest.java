package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.config.GameParameters;

class TetrominoTest {

    @Test
    void testTetrominoFactory_neverReturnsNullAndUsesCorrectSpawnPosition() {
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
    void testTetrominoFactory_throwsExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> Tetromino.tetrominoFactory(null),
                "tetrominoFactory should throw IllegalArgumentException for null input");
    }

}
