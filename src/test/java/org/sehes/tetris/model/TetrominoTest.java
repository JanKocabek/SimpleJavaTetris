package org.sehes.tetris.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.sehes.tetris.config.GameParameters;

class TetrominoTest {

    @Test
    void testTetrominoFactory() {
        // given
        Tetromino tetromino = Tetromino.tetrominoFactory(GameParameters.SPAWN_POINT);
        // then
        assertNotNull(tetromino);
        assertEquals(GameParameters.SPAWN_POINT, tetromino.getPosition());
        assertNotSame(GameParameters.SPAWN_POINT, tetromino.getPosition());
        assertNotSame(tetromino.getStateCord(), TetrominoType.valueOf(tetromino.getTypeValue()).getTetrominoState(0));
        assertNotNull(tetromino.getColor());
        assertEquals(0, tetromino.getCurrentState());
        assertNotNull(tetromino.getStateCord());

    }

    @ParameterizedTest

    @EnumSource(value = TetrominoType.class, names = { "I", "O", "S", "Z", "L",
            "J", "T" })
    void tryDefineNewTetromino(TetrominoType names) {
        // given
        Tetromino tetromino = Tetromino.spawnSpecificTetromino(names, GameParameters.SPAWN_POINT);
        // then
        assertNotNull(tetromino);
        assertEquals(GameParameters.SPAWN_POINT, tetromino.getPosition());
        assertNotSame(GameParameters.SPAWN_POINT, tetromino.getPosition());
        assertNotSame(tetromino.getStateCord(),
                org.sehes.tetris.model.TetrominoType.valueOf(tetromino.getTypeValue()).getTetrominoState(0));
        assertNotNull(tetromino.getColor());
        assertEquals(0, tetromino.getCurrentState());
        assertNotNull(tetromino.getStateCord());
    }

}