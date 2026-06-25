package org.sehes.tetris.graphic;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.TetrominoType;

import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public final class AssetsManager {
    private final EnumMap<TetrominoType, BufferedImage> tiles;
    private final BlockGraphic block;
    private final RenderingHints hints;

    private BufferedImage createTile(TetrominoType type) {
        final var img = new BufferedImage(GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        final var g2d = img.createGraphics();
        g2d.setRenderingHints(hints);
        for (Renderable shape : block.getShapes()) {
            final var side = shape.getSide();
            final var points = shape.getPoints();
            g2d.setPaint(ColorPalette.getPaint(type, side));
            Path2D path = new Path2D.Double();
            path.moveTo(points[0][0], points[0][1]);
            for (int i = 1; i < points.length; i++) {
                path.lineTo(points[i][0], points[i][1]);
            }
            path.closePath();
            g2d.fill(path);
        }
        g2d.dispose();
        return img;
    }

    public AssetsManager(RenderingHints hints) {
        this.hints = hints;
        this.block = new BlockGraphic(GameParameters.BLOCK_SIZE, Config.THICKNESS);
        tiles = new EnumMap<>(TetrominoType.class);
    }

    public Map<TetrominoType, BufferedImage> createAssets() {
        for (TetrominoType type : TetrominoType.getTetrominoTypes()) {
            if (type != TetrominoType.NON) {
                tiles.put(type, createTile(type));
            }
        }
        return tiles;
    }
}
