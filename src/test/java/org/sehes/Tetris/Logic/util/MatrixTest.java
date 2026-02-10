package org.sehes.Tetris.Logic.util;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import org.sehes.tetris.logic.util.Util;

public class MatrixTest {

    @Test
    public void testTranspose() {
        // Input matrix
        boolean[][] input = {
                {true, false, true},
                {false, true, false}
        };

        // Expected transposed matrix
        boolean[][] expected = {
                {true, false},
                {false, true},
                {true, false}
        };

        // Call the transpose method
        boolean[][] result = Util.transposeMatrix(input);

        // Assert the transposed matrix is as expected
        assertArrayEquals(expected, result);
    }

    @Test
    public void testTransposeEmptyMatrix() {
        // Input is an empty matrix
        boolean[][] input = {};

        // Expected transposed matrix is also empty
        boolean[][] expected = {};

        // Call the transpose method
        boolean[][] result = Util.transposeMatrix(input);

        // Assert the result is an empty matrix
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSwapColumns() {
        boolean[][] input = {
                {true, false},
                {false, true},
                {false, true}
        };
        boolean[][] expected = {
                {false, true},
                {true, false},
                {true, false}
        };
        Util.swapColumns(input);
        assertArrayEquals(expected, input);

    }
}
