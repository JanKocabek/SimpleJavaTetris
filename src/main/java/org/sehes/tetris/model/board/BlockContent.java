package org.sehes.tetris.model.board;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum BlockContent {
    CYAN(Color.CYAN),
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    MAGENTA(Color.MAGENTA),
    EMPTY(null);

    private final Color color;

     BlockContent(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    private static final Map<Color, BlockContent> map = new HashMap<>();

    static {
        for (BlockContent type : BlockContent.values()) {
            map.put(type.color, type);
        }
    }

    /**
     * This method takes a Color object from tetromino and returns the
     * corresponding BlockContent enum value. It uses a static map to
     * efficiently look up the BlockContent based on the provided Color. If the
     * color is not found in the map, it will return EMPTY.
     *
     * @param color The Color object for which to find the corresponding
     * BlockContent enum value.
     * @return The BlockContent enum value corresponding to the provided Color,
     * or EMPTY value if the color is not found in the map. EMPTY means that
     * there is no block in that position on the game board.
     */
    public static BlockContent fromColor(Color color) {
        return map.getOrDefault(color, EMPTY);
    }
}
