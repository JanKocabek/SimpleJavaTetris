package org.sehes.Tetris.Logic;

public enum DirectionFlag {
    DOWN(0, 1),
    LEFT(1, 0),
    RIGHT(-1, 0);

    private final int dX;
    private final int dY;

    DirectionFlag(int dX, int dY) {
        this.dX = dX;
        this.dY = dY;
    }

    public int getDCol() {
        return dX;
    }

    public int getDRow() {
        return dY;
    }
}
