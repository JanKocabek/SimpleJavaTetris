package org.sehes.tetris.model;

import java.util.Random;

public class TetrominoFactory {
    private final Random random = new Random();

    public Tetromino createNewRandomTetromino(final Coordinate startingPosition) {
        final int piece = random.nextInt(TetrominoType.size());
        return new Tetromino(TetrominoType.get(piece), startingPosition);
    }

    // methods for junit testing purpose
    static Tetromino spawnSpecificTetromino(final TetrominoType tetrominoType, final Coordinate startPos) {
        return new Tetromino(tetrominoType, startPos);
    }

}
