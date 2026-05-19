package org.sehes.tetris.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.BlockContent;
import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.Tetromino;

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
                BlockContent content = boardView.getBlockContent(row, col);
                if (content != BlockContent.EMPTY) {
                    int x = col * GameParameters.BLOCK_SIZE;
                    int y = (row - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
                    drawBlock(g2d, content.getColor(), x, y);
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
    public void drawCurrentTetromino(Graphics2D g2d, Tetromino t) {
        int[] pixelCoordinates = t.getPixelCoordinates();
        for (int i = 0; i < pixelCoordinates.length; i += 2) {
            drawBlock(g2d, t.getColor(), pixelCoordinates[i], pixelCoordinates[i + 1]);
        }
    }

    /**
     * Draws a single block of the Tetromino on the given Graphics2D object.
     * The block is drawn with its assigned color at the specified pixel coordinates.
     *
     * @param g the Graphics2D object to draw on
     * @param c the color of the block
     * @param x   the x-coordinate of the block's top-left pixel
     * @param y   the y-coordinate of the block's top-left pixel
     */
    private void drawBlock(Graphics2D g, Color c, int x, int y) {
        final int startFillX = x + 1;
        final int startFillY = y + 1;
        g.setColor(c);
        g.fillRect(startFillX, startFillY, GameParameters.BLOCK_SIZE - 1, GameParameters.BLOCK_SIZE - 1);
        addLight(g, c, x, y);
        addShadow(g, c, x, y);
    }

    private void addShadow(Graphics2D g, Color c, int x, int y) {
        final int bottom = y + GameParameters.BLOCK_SIZE - 1;// bottom of the block
        final int right = x + GameParameters.BLOCK_SIZE - 1; // right of the block
        final int left = x + 1; // left of the block
        final int top = y + 1; // top of the block

        g.setColor(c.darker());
        g.drawLine(left, bottom, right, bottom);
        g.drawLine(right, bottom, right, top);
    }

    private void addLight(Graphics2D g, Color c, int x, int y) {
        g.setColor(c.brighter());
        g.drawLine(x, y + GameParameters.BLOCK_SIZE - 1, x, y);
        g.drawLine(x, y, x + GameParameters.BLOCK_SIZE - 1, y);
    }
}
