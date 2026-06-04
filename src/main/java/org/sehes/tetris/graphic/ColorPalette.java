package org.sehes.tetris.graphic;

import org.sehes.tetris.model.TetrominoType;

import java.awt.Color;
import java.awt.Paint;
import java.util.EnumMap;
import java.util.Map;


/**
 *
 * Palette of colors and shades for each tetromino shape.
 * Each tetromino shape has a unique color palette, with 5 colors:
 * - 4 colors for the 4 sides of the tetromino
 * - 1 color for the center square
 * Array is ordered clockwise
 * as [top, right, bottom, left, center]
 */

public enum ColorPalette {
    CYAN(TetrominoType.I, new Paint[]{
            Colors.I_SHADE[0], Colors.I_SHADE[1], Colors.I_SHADE[2], Colors.I_SHADE[3], Colors.I_SHADE[4]}
    ),
    YELLOW(TetrominoType.O, new Paint[]{Colors.O_SHADES[0], Colors.O_SHADES[1], Colors.O_SHADES[2], Colors.O_SHADES[3], Colors.O_SHADES[4]}),
    GREEN(TetrominoType.S, new Color[]{Colors.S_SHADE[0], Colors.S_SHADE[1], Colors.S_SHADE[2], Colors.S_SHADE[3], Colors.S_SHADE[4]}),
    BLUE(TetrominoType.J, new Color[]{Colors.J_SHADE[0], Colors.J_SHADE[1], Colors.J_SHADE[2], Colors.J_SHADE[3], Colors.J_SHADE[4]}),
    ORANGE(TetrominoType.L, new Color[]{Colors.L_SHADE[0], Colors.L_SHADE[1], Colors.L_SHADE[2], Colors.L_SHADE[3], Colors.L_SHADE[4]}),
    RED(TetrominoType.Z, new Color[]{Colors.Z_SHADE[0], Colors.Z_SHADE[1], Colors.Z_SHADE[2], Colors.Z_SHADE[3], Colors.Z_SHADE[4]}),
    MAGENTA(TetrominoType.T, new Color[]{Colors.T_SHADE[0], Colors.T_SHADE[1], Colors.T_SHADE[2], Colors.T_SHADE[3], Colors.T_SHADE[4]});

    private final Paint[] colors;
    private final TetrominoType mino;
    private static final ColorPalette[] CACHE = values();
    private static final Map<TetrominoType, Paint[]> COLOR_MAP;


    static {
        COLOR_MAP = new EnumMap<>(TetrominoType.class);
        for (var e : CACHE) {
            if (e.mino != TetrominoType.NON) {
                COLOR_MAP.put(e.mino, e.colors);
            }
        }
    }

    ColorPalette(TetrominoType mino, Paint[] colors) {
        this.colors = colors;
        this.mino = mino;
    }

    public static Paint getPaint(TetrominoType type, Side side) {
        final var sideMap = COLOR_MAP.get(type);
        return switch (side) {
            case TOP -> sideMap[0];
            case RIGHT -> sideMap[1];
            case BOTTOM -> sideMap[2];
            case LEFT -> sideMap[3];
            case CENTER -> sideMap[4];
        };
    }

    static class Colors {
        private static final Color O_BASE_GOLD = new Color(255, 205, 18);
        private static final float[] O_HSB = Color.RGBtoHSB(O_BASE_GOLD.getRed(), O_BASE_GOLD.getGreen(), O_BASE_GOLD.getBlue(), null
        );
        static final Color[] O_SHADES = new Color[]{
                Color.getHSBColor(O_HSB[0] + 1 / 360f, O_HSB[1] * 0.70f, O_HSB[2]), Color.getHSBColor(O_HSB[0], O_HSB[1], O_HSB[2] * 0.85f),
                Color.getHSBColor(O_HSB[0], O_HSB[1], O_HSB[2] * 0.70f),
                Color.getHSBColor(O_HSB[0] - 1 / 360f, O_HSB[1] * 0.90f, O_HSB[2] * 0.90f),
                Color.getHSBColor(O_HSB[0], O_HSB[1], O_HSB[2])


        };
        // I - Lavender
        private static final Color I_BASE = new Color(165, 145, 205);
        private static final float[] I_HSB = Color.RGBtoHSB(I_BASE.getRed(), I_BASE.getGreen(), I_BASE.getBlue(), null);
        static final Color[] I_SHADE = {
                Color.getHSBColor(I_HSB[0] + 1, I_HSB[1] * 0.70f, I_HSB[2]),
                Color.getHSBColor(I_HSB[0], I_HSB[1], I_HSB[2] * 0.85f),
                Color.getHSBColor(I_HSB[0], I_HSB[1], I_HSB[2] * 0.70f),
                Color.getHSBColor(I_HSB[0] - 1, I_HSB[1] * 0.90f, I_HSB[2] * 0.90f),
                Color.getHSBColor(I_HSB[0], I_HSB[1], I_HSB[2])
        };

        // J - Sky Blue
        private static final Color J_BASE = new Color(65, 145, 205);
        private static final float[] J_HSB = Color.RGBtoHSB(J_BASE.getRed(), J_BASE.getGreen(), J_BASE.getBlue(), null);
        static final Color[] J_SHADE = {
                Color.getHSBColor(J_HSB[0] + 1, J_HSB[1] * 0.70f, J_HSB[2]),
                Color.getHSBColor(J_HSB[0], J_HSB[1], J_HSB[2] * 0.85f),
                Color.getHSBColor(J_HSB[0], J_HSB[1], J_HSB[2] * 0.70f),
                Color.getHSBColor(J_HSB[0] - 1, J_HSB[1] * 0.90f, J_HSB[2] * 0.90f),
                Color.getHSBColor(J_HSB[0], J_HSB[1], J_HSB[2])
        };

        // L - Terracotta
        private static final Color L_BASE = new Color(195, 95, 50);
        private static final float[] L_HSB = Color.RGBtoHSB(L_BASE.getRed(), L_BASE.getGreen(), L_BASE.getBlue(), null);
        static final Color[] L_SHADE = {
                Color.getHSBColor(L_HSB[0] + 1, L_HSB[1] * 0.70f, L_HSB[2]),
                Color.getHSBColor(L_HSB[0], L_HSB[1], L_HSB[2] * 0.85f),
                Color.getHSBColor(L_HSB[0], L_HSB[1], L_HSB[2] * 0.70f),
                Color.getHSBColor(L_HSB[0] - 1, L_HSB[1] * 0.90f, L_HSB[2] * 0.90f),
                Color.getHSBColor(L_HSB[0], L_HSB[1], L_HSB[2])
        };

        // S - Lime Green
        private static final Color S_BASE = new Color(95, 175, 40);
        private static final float[] S_HSB = Color.RGBtoHSB(S_BASE.getRed(), S_BASE.getGreen(), S_BASE.getBlue(), null);
        static final Color[] S_SHADE = {
                Color.getHSBColor(S_HSB[0] + 1, S_HSB[1] * 0.70f, S_HSB[2]),
                Color.getHSBColor(S_HSB[0], S_HSB[1], S_HSB[2] * 0.85f),
                Color.getHSBColor(S_HSB[0], S_HSB[1], S_HSB[2] * 0.70f),
                Color.getHSBColor(S_HSB[0] - 1, S_HSB[1] * 0.90f, S_HSB[2] * 0.90f),
                Color.getHSBColor(S_HSB[0], S_HSB[1], S_HSB[2])
        };

        // T - Medium Purple
        private static final Color T_BASE = new Color(120, 175, 195);
        private static final float[] T_HSB = Color.RGBtoHSB(T_BASE.getRed(), T_BASE.getGreen(), T_BASE.getBlue(), null);
        static final Color[] T_SHADE = {
                Color.getHSBColor(T_HSB[0] + 1, T_HSB[1] * 0.70f, T_HSB[2]),
                Color.getHSBColor(T_HSB[0], T_HSB[1], T_HSB[2] * 0.85f),
                Color.getHSBColor(T_HSB[0], T_HSB[1], T_HSB[2] * 0.70f),
                Color.getHSBColor(T_HSB[0] - 1, T_HSB[1] * 0.90f, T_HSB[2] * 0.90f),
                Color.getHSBColor(T_HSB[0], T_HSB[1], T_HSB[2])
        };

        // Z - Rose Pink
        private static final Color Z_BASE = new Color(215, 110, 135);
        private static final float[] Z_HSB = Color.RGBtoHSB(Z_BASE.getRed(), Z_BASE.getGreen(), Z_BASE.getBlue(), null);
        static final Color[] Z_SHADE = {
                Color.getHSBColor(Z_HSB[0] + 1, Z_HSB[1] * 0.70f, Z_HSB[2]),
                Color.getHSBColor(Z_HSB[0], Z_HSB[1], Z_HSB[2] * 0.85f),
                Color.getHSBColor(Z_HSB[0], Z_HSB[1], Z_HSB[2] * 0.70f),
                Color.getHSBColor(Z_HSB[0] - 1, Z_HSB[1] * 0.90f, Z_HSB[2] * 0.90f),
                Color.getHSBColor(Z_HSB[0], Z_HSB[1], Z_HSB[2])
        };

        private Colors() {
        }
    }
}
