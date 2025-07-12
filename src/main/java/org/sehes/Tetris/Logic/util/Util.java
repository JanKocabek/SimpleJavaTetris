package org.sehes.Tetris.Logic.util;

public class Util {
    public static boolean[][] transposeMatrix(boolean[][] matrix) {
        if (matrix.length == 0) return matrix;
        boolean[][] newMatrix = new boolean[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                newMatrix[col][row] = matrix[row][col];
            }
        }
        return newMatrix;
    }

    public static void swapColumns(boolean[][] grid) {
        if (grid.length == 0) return;
        int startCol = 0;
        int endCol = grid[0].length - 1 - startCol;
        for (; startCol < endCol; startCol++) {
            for (int row = 0; row < grid.length; row++) {
                boolean tmp = grid[row][startCol];
                grid[row][startCol] = grid[row][endCol];
                grid[row][endCol] = tmp;
            }
            endCol--;
        }
    }

    public static void swapRows(boolean[][] grid) {
        if (grid.length == 0) return;
        int startRow = 0;
        int endRow = grid.length - 1 - startRow;
        for (; startRow < endRow; startRow++) {
            for (int col = 0; col < grid[0].length; col++) {
                boolean tmp = grid[startRow][col];
                grid[startRow][col] = grid[endRow][col];
                grid[endRow][col] = tmp;
            }
            endRow--;
        }
    }
}
