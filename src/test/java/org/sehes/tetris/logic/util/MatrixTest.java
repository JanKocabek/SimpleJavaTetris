package org.sehes.tetris.logic.util;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

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
        boolean[][] result = MatrixTransformations.transposeMatrix(input);

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
        boolean[][] result = MatrixTransformations.transposeMatrix(input);

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
        MatrixTransformations.swapColumns(input);
        assertArrayEquals(expected, input);

    }

    @Test
    public void testSwapColumnsEmptyMatrix() {
        boolean[][] input = {};
        boolean[][] expected = {};

        MatrixTransformations.swapColumns(input);

        assertArrayEquals(expected, input);
    }

    @Test
    public void testSwapColumnsSingleRowMatrix() {
        boolean[][] input = {
                {true, true, false}
        };
        boolean[][] expected = {
                {false, true, true}
        };

        MatrixTransformations.swapColumns(input);

        assertArrayEquals(expected, input);
    }

    @Test
    public void testSwapColumnsSingleColumnMatrix() {
        boolean[][] input = {
                {true},
                {false},
                {true}
        };
        boolean[][] expected = {
                {true},
                {false},
                {true}
        };

        MatrixTransformations.swapColumns(input);

        assertArrayEquals(expected, input);
    }

    @Test
    public void testSwapRowsEmptyMatrix() {
        boolean[][] input = {};
        boolean[][] expected = {};

        MatrixTransformations.swapRows(input);

        assertArrayEquals(expected, input);
    }

    @Test
    public void testSwapRowsSingleRowMatrix() {
        boolean[][] input = {
                {true, false, true}
        };
        boolean[][] expected = {
                {true, false, true}
        };

        MatrixTransformations.swapRows(input);

        assertArrayEquals(expected, input);
    }

    @Test
    public void testSwapRowsSingleColumnMatrix() {
        boolean[][] input = {
                {true},
                {true},
                {false}
        };
        boolean[][] expected = {
                {false},
                {true},
                {true}
        };

        MatrixTransformations.swapRows(input);

        assertArrayEquals(expected, input);
    }
    @Test
    public void testSwapRows() {
        boolean[][] input = {
                {true, false},
                {false, true},
                {false, false}
        };
        boolean[][] expected = {
                {false, false},
                {false, true},
                {true, false}
        };
        MatrixTransformations.swapRows(input);
        assertArrayEquals(expected, input);

    }
}
