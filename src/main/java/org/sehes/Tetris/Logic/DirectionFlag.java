package org.sehes.Tetris.Logic;

public enum DirectionFlag {
    DOWN(1, 0),
    LEFT(0, 1),
    RIGHT(0, -1);

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
