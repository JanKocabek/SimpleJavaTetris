package org.sehes.tetris.logic;

public enum DirectionFlag {
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    ROTATE_R(0, 0),
    ROTATE_L(0, 0);

    private final int dX;
    private final int dY;

    DirectionFlag(int dX, int dY) {
        this.dX = dX;
        this.dY = dY;
    }

    /**
     * Returns the change in the x-coordinate (column) for the given direction flag. For example, for LEFT it returns -1, for RIGHT it returns 1, and for DOWN it returns 0.
     * @return
     */
    public int getX() {
        return dX;
    }

    /**
     * Returns the change in the y-coordinate (row) for the given direction flag. For example, for DOWN it returns 1, for LEFT and RIGHT it returns 0.
     * @return
     */
    public int getY() {
        return dY;
    }
}
