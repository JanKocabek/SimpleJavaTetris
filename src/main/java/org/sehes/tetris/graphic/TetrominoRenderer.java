package org.sehes.tetris.graphic;

import org.jspecify.annotations.NullMarked;
import org.sehes.tetris.model.Coordinate;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

@NullMarked
public class TetrominoRenderer {

    /**
     * Draws a tetromino at a specified pixel origin, applying optional normalization
     * of its local coordinate system.
     *
     * <p>This method is the fully parameterized version. It allows callers to specify
     * both the screen-space origin (originX, originY) and the local-space origin
     * (localOriginX, localOriginY). The local origin is typically the minimum X/Y
     * inside the tetromino's coordinate list, used to normalize rotated or preview
     * shapes so they start at (0,0).</p>
     *
     * @param g             the Graphics2D context used for drawing
     * @param coordinates   list of block coordinates in tetromino-local space
     * @param tile          the tile image used for each block
     * @param tileSize      pixel size of a single block
     * @param originX       pixel-space X origin where the tetromino is placed
     * @param originY       pixel-space Y origin where the tetromino is placed
     * @param localOriginX  local-space X normalization offset (usually minX)
     * @param localOriginY  local-space Y normalization offset (usually minY)
     * @param offsetY       additional vertical pixel offset (usually GhostPieceOffset)
     */
    static void drawMinoAt(Graphics2D g, List<Coordinate> coordinates, BufferedImage tile, int tileSize, int originX, int originY, int localOriginX, int localOriginY, int offsetY) {
        for (var block : coordinates) {
            int x = originX + ((block.x() - localOriginX) * tileSize);
            int y = originY + ((block.y() - localOriginY) * tileSize) + offsetY;
            g.drawImage(tile, x, y, null);
        }
    }

    /**
     * Overloaded version used for main board rendering.
     * and for GhostPiece Rendering
     *
     * <p>This variant assumes the tetromino's local coordinates already begin at (0,0),
     * which is true for standard board drawing. It forwards to the full method with
     * localOriginX and localOriginY set to zero.</p>
     *
     * @param g             the Graphics2D context used for drawing
     * @param coordinates   list of block coordinates in tetromino-local space
     * @param tile          the tile image used for each block
     * @param tileSize      pixel size of a single block
     * @param originX       pixel-space X origin where the tetromino is placed
     * @param originY       pixel-space Y origin where the tetromino is placed
     * @param offsetY       additional vertical pixel offset (usually GhostPieceOffset)
     */
    static void drawMinoAt(Graphics2D g, List<Coordinate> coordinates, BufferedImage tile, int tileSize, int originX, int originY, int offsetY) {
        drawMinoAt(g, coordinates, tile, tileSize, originX, originY, 0, 0, offsetY);
    }

    /**
     * Overloaded version used when normalization is required but no vertical offset is needed.
     *
     * <p>This is typically used for UI elements such as next-piece previews or hold-piece
     * rendering, where the tetromino should be normalized (localOriginX/localOriginY)
     * but drawn without additional board offset.</p>
     *
     * @param g             the Graphics2D context used for drawing
     * @param coordinates   list of block coordinates in tetromino-local space
     * @param tile          the tile image used for each block
     * @param tileSize      pixel size of a single block
     * @param originX       pixel-space X origin where the tetromino is placed
     * @param originY       pixel-space Y origin where the tetromino is placed
     * @param localOriginX  local-space X normalization offset (usually minX)
     * @param localOriginY  local-space Y normalization offset (usually minY)
     */
    static void drawMinoAt(Graphics2D g, List<Coordinate> coordinates, BufferedImage tile, int tileSize, int originX, int originY, int localOriginX, int localOriginY) {
        drawMinoAt(g, coordinates, tile, tileSize, originX, originY, localOriginX, localOriginY, 0);
    }

    private TetrominoRenderer() {
    }
}