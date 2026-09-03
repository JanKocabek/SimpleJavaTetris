package org.sehes.tetris.model;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class RandomPieceGenerator implements PieceGenerator {
    private final RandomGenerator generator = RandomGeneratorFactory.getDefault().create();
    private static final TetrominoType[] TETROMINO_SHAPES = TetrominoType.getTetrominoShapes();
    private TetrominoType next = null;

    @Override
    public TetrominoType peekNext() {
        if (next == null) {
            next = generate();
        }
        return next;
    }

    @Override
    public TetrominoType getNextPiece() {
        final var consume = this.peekNext();
        this.next = generate();
        return consume;
    }

    //
    private TetrominoType generate() {
        return TETROMINO_SHAPES[generator.nextInt(TETROMINO_SHAPES.length)];
    }
}
