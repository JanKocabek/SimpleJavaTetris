package org.sehes.tetris.model;

public enum Rotation {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private static final Rotation[] values = Rotation.values();
    private static final int SIZE = values.length;
    private final int value;

    Rotation(int state) {
        this.value = state;
    }

    public Rotation rotateClockwise() {
        return next();
    }

    public Rotation rotateCounterClockwise() {
        return previous();
    }

    private Rotation next() {
        return values[(value + 1) % SIZE];
    } // next rotation

    public Rotation previous() {
        return values[(value + (SIZE - 1)) % SIZE];
    } // previous rotation
}
