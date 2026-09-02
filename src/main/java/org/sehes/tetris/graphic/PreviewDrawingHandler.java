package org.sehes.tetris.graphic;

import org.sehes.tetris.model.Orientation;
import org.sehes.tetris.model.ShapeProvider;
import org.sehes.tetris.model.TetrominoType;

import javax.swing.Painter;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PreviewDrawingHandler implements Painter<TetrominoType> {

    private final AssetsManager assets;
    private final RenderingHints hints;

    public PreviewDrawingHandler(RenderingHints hints, AssetsManager assetsManager) {
        this.assets = assetsManager;
        this.hints = hints;
    }


    @Override
    public void paint(Graphics2D g, TetrominoType type, int width, int height) {
        if (type == null) return;
        g.setRenderingHints(hints);
        final var tile = assets.getTile(type);
        final var coordinates = ShapeProvider.getTetrominoState(type, Orientation.NORTH);
        for (var block : coordinates) {
            final int x = block.x() * GameParameters.BLOCK_SIZE + GameParameters.BLOCK_SIZE;
            final int y = block.y() * GameParameters.BLOCK_SIZE + 2 * GameParameters.BLOCK_SIZE;
            g.drawImage(tile, x, y, null);
        }
    }


}
