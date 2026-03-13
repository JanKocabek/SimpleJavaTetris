package org.sehes.tetris.model;

import java.awt.Color;

public enum TetrominoType {

        I(Color.CYAN),
        J(Color.BLUE),
        L(Color.ORANGE),
        O(Color.YELLOW),
        S(Color.GREEN),
        T(Color.MAGENTA),
        Z(Color.RED);

        private static final TetrominoType[] cachedEnum = values();
        private final Color color;

        /**
         * 
         * @return the number of tetromino types used in the tetromino factory method
         *         for random number of tetromino type
         */
        public static int size() {
                return cachedEnum.length;
        }

        /**
         * 
         * @param intValue
         * @return returns the tetromino type based on the intValue
         */
        public static TetrominoType get(final int intValue) {
                if (intValue < 0 || intValue >= cachedEnum.length) {
                        throw new IllegalArgumentException("Invalid intValue: " + intValue);
                }
                return cachedEnum[intValue];
        }

        TetrominoType(final Color color) {
                this.color = color;
        }

        Color getColor() {
                return color;
        }

}