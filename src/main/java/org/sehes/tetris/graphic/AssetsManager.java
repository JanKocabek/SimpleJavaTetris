package org.sehes.tetris.graphic;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.TetrominoType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public final class AssetsManager {
    private final EnumMap<TetrominoType, BufferedImage> tiles;
    private final MinoBlock block;
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
        this.block = new MinoBlock(GameParameters.BLOCK_SIZE, Config.BLOCK_THICKNESS);
        tiles = new EnumMap<>(TetrominoType.class);
    }

    public Map<TetrominoType, BufferedImage> createAssets() {
        for (TetrominoType type : TetrominoType.getTetrominoShapes()) {
            tiles.put(type, createTile(type));
        }

        return tiles;
    }

    public BufferedImage createGhostTile() {
        BufferedImage buffer = new BufferedImage(GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        final var g2d = buffer.createGraphics();
        final var THICKNESS = 3f;
        Stroke thickStroke = new BasicStroke(THICKNESS);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(thickStroke);
        g2d.drawRect(0, 0, (int) (GameParameters.BLOCK_SIZE - THICKNESS), (int) (GameParameters.BLOCK_SIZE - THICKNESS));
        g2d.dispose();
        return buffer;
    }
}
