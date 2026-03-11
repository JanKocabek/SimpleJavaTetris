package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.sehes.tetris.config.GameParameters;

class TetrominoTest {

    @ParameterizedTest
    @EnumSource(value = TetrominoType.class)
    void testTetrominoFactory(TetrominoType type) {
        // given
        Tetromino tetromino = Tetromino.spawnSpecificTetromino(type, GameParameters.SPAWN_POINT);
        // then
        assertNotNull(tetromino);
        assertEquals(GameParameters.SPAWN_POINT.x(), tetromino.getPositionX());
        assertEquals(GameParameters.SPAWN_POINT.y(), tetromino.getPositionY());
        assertSame(GameParameters.SPAWN_POINT.x(), tetromino.getPositionX());
        assertSame(GameParameters.SPAWN_POINT.y(), tetromino.getPositionY());
        assertEquals(type.name(), tetromino.getTypeValue());
        assertNotNull(tetromino.getStateCord());
    }

}
