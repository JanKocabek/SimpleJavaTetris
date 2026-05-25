package org.sehes.tetris.graphic;

public class Bevel implements Renderable {
    static final int VERTEX = 4;

    private final int[][] points;

    Bevel( int[][] points) {
        this.points = points;
    }

    public int[][] getPoints() {
        return points;
    }

    @Override
    public int getVertexCount() {
        return VERTEX;
    }

}
