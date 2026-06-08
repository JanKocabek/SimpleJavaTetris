package org.sehes.tetris.gui;

import org.jspecify.annotations.NonNull;
import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.graphic.BlockGraphic;
import org.sehes.tetris.graphic.ColorPalette;
import org.sehes.tetris.graphic.Config;
import org.sehes.tetris.graphic.Renderable;
import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.Tetromino;
import org.sehes.tetris.model.TetrominoType;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * The TetrisDrawingHandler class is responsible for rendering the game state
 * onto the screen. It provides methods to initialize the graphics context, draw
 * the game grid, and render the current Tetromino piece based on the game
 * board's state. The drawing handler interacts with the GameManager to retrieve
 * necessary information about the game state and ensures that the visual
 * representation of the game is accurate and up to date.
 */
public class TetrisDrawingHandler {
    private BufferedImage backgroundGrid = null;
    private BufferedImage boardImg = null;
    private final RenderingHints hints = createFastRenderingHints();
    private final BlockGraphic block;

    public TetrisDrawingHandler() {
        block = new BlockGraphic(GameParameters.BLOCK_SIZE, Config.THICKNESS);
    }

    /**
     * Creates a RenderingHints object with settings optimized for fast rendering
     * with nearest neighbor interpolation. The resulting object is used to
     * configure the graphics context for rendering the Tetris game board.
     * <p>
     *
     * @return The RenderingHints object with optimized settings for fast rendering
     */
    private static RenderingHints createFastRenderingHints() {
        return new RenderingHints(
                Map.of(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF,
                        RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED,
                        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR));
    }

    public void initialize(Graphics2D g2d) {
        g2d.setRenderingHints(hints);
    }

    public BufferedImage getGrid() {
        return backgroundGrid;
    }

    public void drawGrid() {
        backgroundGrid = new BufferedImage(GameParameters.COLUMNS * GameParameters.BLOCK_SIZE,
                GameParameters.VISIBLE_ROWS * GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = backgroundGrid.createGraphics();
        final int width = backgroundGrid.getWidth();
        final int height = backgroundGrid.getHeight();

        g2d.setColor(Color.YELLOW);
        for (int i = 1; i < GameParameters.COLUMNS; i++) {
            g2d.drawLine(GameParameters.BLOCK_SIZE * i, 0, GameParameters.BLOCK_SIZE * i, height);
        }
        for (int i = 1; i < GameParameters.VISIBLE_ROWS; i++) {
            g2d.drawLine(0, GameParameters.BLOCK_SIZE * i, width, GameParameters.BLOCK_SIZE * i);
        }
        g2d.dispose();
    }

    public void paintGameBoard(Graphics2D g2d, BoardView boardView, boolean isBoardDirty) {
        if (boardImg == null) {
            boardImg = new BufferedImage(GameParameters.COLUMNS * GameParameters.BLOCK_SIZE,
                    GameParameters.VISIBLE_ROWS * GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        }
        if (isBoardDirty) {
            bakeBoardImg(boardView);
        }
        g2d.drawImage(boardImg, 0, 0, null);
    }

    /**
     * Bakes the game board image based on the current board view.
     *
     * @param boardView the current board view
     * @throws NullPointerException if the board view is null
     */
    private void bakeBoardImg(BoardView boardView) {
        Graphics2D g2d = boardImg.createGraphics();
        g2d.setRenderingHints(hints);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, boardImg.getWidth(), boardImg.getHeight());
        g2d.setComposite(AlphaComposite.SrcOver);
        for (int row = boardView.getHeight() - 1; row >= GameParameters.HIDDEN_ROWS; row--) {
            for (int col = boardView.getWidth() - 1; col >= 0; col--) {
                TetrominoType content = boardView.getBlockContent(row, col);
                if (content != TetrominoType.NON && content != null) {
                    int x = col * GameParameters.BLOCK_SIZE;
                    int y = (row - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
                    drawBlock(g2d, content, x, y);
                }
            }
        }
        g2d.dispose();
    }

    /**
     * Draws the current Tetromino on the given Graphics2D object.
     * The Tetromino is drawn with its assigned color and at its current position
     * on the game board, specified by its pixel coordinates.
     * <p>
     *
     * @param g2d the Graphics2D object to draw on
     * @param t   the Tetromino to draw
     */
    public void drawCurrentTetromino(Graphics2D g2d, @NonNull Tetromino t) {

        //todo seperate pixel coordinates from tetromino object
        int[] pixelCoordinates = t.getPixelCoordinates();
        for (int i = 0; i < pixelCoordinates.length; i += 2) {
            drawBlock(g2d, t.getType(), pixelCoordinates[i], pixelCoordinates[i + 1]);
        }
    }



    /**
     * Renders a single block of the Tetromino at the specified pixel coordinates with the given color.
     * the block is rendered from {@link BlockGraphic} object  4 bevels and center square which are drawn
     * by {@link Renderable} objects.
     *
     * @param g2d the {@link Graphics2D} object to draw on
     * @param type the tetromino shape
     * @param x the x-coordinate of the block's top-left pixel
     * @param y the y-coordinate of the block's top-left pixel
     */
    private void drawBlock(Graphics2D g2d, TetrominoType type, double x, double y) {
        final AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(x, y);
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
        g2d.setTransform(originalTransform);
    }
}
