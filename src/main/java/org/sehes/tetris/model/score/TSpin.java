package org.sehes.tetris.model.score;

import org.sehes.tetris.model.Orientation;

public enum TSpin {
    NONE, FULL, MINI;

    /**
     * Returns the front offset of the T corners blocks express as x,y coordinates.
     *
     * @param orientation orientation of the T shape
     * @return front corners offset
     */
    public static int[][] getFrontCornersOffset(Orientation orientation) {
        return switch (orientation) {
            case NORTH -> new int[][]{{-1, -1}, {1, -1}};
            case EAST -> new int[][]{{1, -1}, {1, 1}};
            case SOUTH -> new int[][]{{-1, 1}, {1, 1}};
            case WEST -> new int[][]{{-1, -1}, {-1, 1}};
        };
    }

    public static int[][] getBackCornersOffset(Orientation orientation) {
        return switch (orientation) {
            case NORTH -> new int[][]{{-1, 1}, {1, 1}};
            case EAST -> new int[][]{{-1, -1}, {-1, 1}};
            case SOUTH -> new int[][]{{-1, -1}, {1, -1}};
            case WEST -> new int[][]{{1, -1}, {1, 1}};
        };
    }

    public static TSpin getTSpin(int frontCornersCount, int backCornersCount, boolean wasRotate) {
        if (!wasRotate) return NONE;
        if (frontCornersCount == 2 && backCornersCount == 1) return FULL;
        return (frontCornersCount == 1 && backCornersCount == 2) ? MINI : NONE;
    }
}
