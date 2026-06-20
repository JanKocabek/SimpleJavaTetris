package org.sehes.tetris.model.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MatrixTest {

    @Test
    void testTranspose() {
        // Input matrix
        boolean[][] input = {
                { true, false, true },
                { false, true, false }
        };

        // Expected transposed matrix
        boolean[][] expected = {
                { true, false },
                { false, true },
                { true, false }
        };

        // Call the transpose method
        boolean[][] result = MatrixTransformations.transposeMatrix(input);

        // Assert the transposed matrix is as expected
        assertArrayEquals(expected, result);
    }

    @Test
    void testTransposeEmptyMatrix() {
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
    void testSwapColumns() {
        boolean[][] input = {
                { true, false },
                { false, true },
                { false, true }
        };
        boolean[][] expected = {
                { false, true },
                { true, false },
                { true, false }
        };
        MatrixTransformations.swapColumns(input);
        assertArrayEquals(expected, input);

    }

    @Test
    void testSwapColumnsEmptyMatrix() {
        boolean[][] input = {};
        boolean[][] expected = {};

        MatrixTransformations.swapColumns(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSwapColumnsSingleRowMatrix() {
        boolean[][] input = {
                { true, true, false }
        };
        boolean[][] expected = {
                { false, true, true }
        };

        MatrixTransformations.swapColumns(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSwapColumnsSingleColumnMatrix() {
        boolean[][] input = {
                { true },
                { false },
                { true }
        };
        boolean[][] expected = {
                { true },
                { false },
                { true }
        };

        MatrixTransformations.swapColumns(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSwapRowsEmptyMatrix() {
        boolean[][] input = {};
        boolean[][] expected = {};

        MatrixTransformations.swapRows(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSwapRowsSingleRowMatrix() {
        boolean[][] input = {
                { true, false, true }
        };
        boolean[][] expected = {
                { true, false, true }
        };

        MatrixTransformations.swapRows(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSwapRowsSingleColumnMatrix() {
        boolean[][] input = {
                { true },
                { true },
                { false }
        };
        boolean[][] expected = {
                { false },
                { true },
                { true }
        };

        MatrixTransformations.swapRows(input);

        assertArrayEquals(expected, input);
    }

    @Test
    void testSwapRows() {
        boolean[][] input = {
                { true, false },
                { false, true },
                { false, false }
        };
        boolean[][] expected = {
                { false, false },
                { false, true },
                { true, false }
        };
        MatrixTransformations.swapRows(input);
        assertArrayEquals(expected, input);

    }
}
