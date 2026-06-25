package org.sehes.tetris.graphic;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.TetrominoType;

import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumMap;
import java.util.Map;

public final class AssetsManager {
    private final EnumMap<TetrominoType, BufferedImage> tiles;
    private final BlockGraphic block;
    private final RenderingHints hints;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final int total = TetrominoType.getTetrominoTypes().length ;
    private Integer progress;
    private String message;
    public record Update(Integer progress, String message) {

    }

    private void setProgress(Integer progress, String message) {
        pcs.firePropertyChange("progress", new Update(this.progress, this.message),  new Update(progress, message));
        this.progress = progress;
        this.message = message;
    }

    private void updateProgress(String updateMsg) {
        setProgress(this.progress + 1, updateMsg);
    }

    public int getTotal() {
        return total;
    }


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
        setProgress(0, "Loading assets");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted because happen this error: ");
            e.printStackTrace(System.err);
            Thread.currentThread().interrupt();
        }
        for (TetrominoType type : TetrominoType.getTetrominoTypes()) {
            if (type != TetrominoType.NON) {
                tiles.put(type, createTile(type));
                var updateMsg = "Creating textures for Tetromino: " + type;
                updateProgress(updateMsg);
                //todo: remove this when progress bar will have something to load of itself
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println("Thread was interrupted because happen error: ");
                    e.printStackTrace(System.err);
                    Thread.currentThread().interrupt();
                }
            }
        }
        return tiles;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


}
