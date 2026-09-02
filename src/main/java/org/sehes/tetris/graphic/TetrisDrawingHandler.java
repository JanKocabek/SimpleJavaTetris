package org.sehes.tetris.graphic;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.config.GhostType;
import org.sehes.tetris.controller.GameSnapshot;
import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.Tetromino;
import org.sehes.tetris.model.TetrominoType;

import javax.swing.Painter;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * The TetrisDrawingHandler class is responsible for rendering the game state
 * onto the screen. It provides methods to initialize the graphics context, draw
 * the game grid, and render the current Tetromino piece based on the game
 * board's state. The drawing handler interacts with the GameManager to retrieve
 * necessary information about the game state and ensures that the visual
 * representation of the game is accurate and up to date.
 */
@NullMarked
public class TetrisDrawingHandler implements Painter<GameSnapshot> {
    private final BufferedImage boardImg;
    private final RenderingHints renderingHints;
    private final AssetsManager assets;

    public TetrisDrawingHandler(RenderingHints renderingHints, AssetsManager assets) {
        this.assets = assets;
        this.renderingHints = renderingHints;
        boardImg = new BufferedImage(GameParameters.COLUMNS * Config.BLOCK_SIZE,
                GameParameters.VISIBLE_ROWS * Config.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
    }


    private void paintGameBoard(Graphics2D g2d, BoardView boardView, boolean isBoardDirty) {
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
        g2d.setRenderingHints(renderingHints);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, boardImg.getWidth(), boardImg.getHeight());
        g2d.setComposite(AlphaComposite.SrcOver);
        for (int row = boardView.getHeight() - 1; row >= GameParameters.HIDDEN_ROWS; row--) {
            for (int col = boardView.getWidth() - 1; col >= 0; col--) {
                TetrominoType content = boardView.getBlockContent(row, col);
                if (content != TetrominoType.NON && content != null) {
                    int x = col * Config.BLOCK_SIZE;
                    int y = (row - GameParameters.HIDDEN_ROWS) * Config.BLOCK_SIZE;
                    g2d.drawImage(assets.getTile(content), x, y, null);
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
     * @param t   the Tetromino to draw - cannot be null
     */
    private void drawCurrentTetromino(Graphics2D g2d, Tetromino t, int ghostDistance, GhostType ghostType) {
        //  int[] pixelCoordinates = calculatePixelCoordinates(t);
        if (ghostType != GhostType.NONE)
            drawGhostMino(g2d, t, ghostDistance, ghostType);//draw first so the real block is draw over not otherwise around
        drawTetromino(g2d, t);
    }

    private void drawTetromino(Graphics2D g2d, Tetromino t) {
        drawBlocks(g2d, t, assets.getTile(t.getType()), 0);
    }

    private void drawGhostMino(Graphics2D g2d, Tetromino t, int ghostOffset, GhostType ghostType) {
        final var PIXEL_OFFSET = ghostOffset * Config.BLOCK_SIZE;

        drawBlocks(g2d, t, assets.getGhostTile(ghostType), PIXEL_OFFSET);

    }

    private void drawBlocks(Graphics2D g2d, Tetromino t, BufferedImage tile, int offsetY) {
        final int pX = t.getPositionX() * GameParameters.BLOCK_SIZE;
        final int pY = (t.getPositionY() - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
        for (var block : t.getStateCord()) {
            final int x = block.x() * GameParameters.BLOCK_SIZE + pX;
            final int y = block.y() * GameParameters.BLOCK_SIZE + pY + offsetY;
            g2d.drawImage(tile, x, y, null);
        }
    }

    @Override
    public void paint(Graphics2D g, @Nullable GameSnapshot snapshot, int width, int height) {
        if (snapshot == null) {
            return;
        }
        final BoardView board = snapshot.boardView();
        final boolean wasBoardDirty = snapshot.isBoardDirty();
        paintGameBoard(g, board, wasBoardDirty);
        snapshot.currentTetromino().ifPresent(tetromino -> drawCurrentTetromino(g, tetromino, snapshot.distance(), snapshot.ghostType()));
    }
}

