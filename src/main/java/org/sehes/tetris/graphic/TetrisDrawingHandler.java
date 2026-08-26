package org.sehes.tetris.graphic;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.controller.GameSnapshot;
import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.Tetromino;
import org.sehes.tetris.model.TetrominoType;

import javax.swing.Painter;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
@NullMarked
public class TetrisDrawingHandler implements Painter<GameSnapshot> {

    private static final int COORDS_PER_BLOCK = 2;
    private final BufferedImage boardImg;
    private final Map<TetrominoType, BufferedImage> assets;
    private final RenderingHints renderingHints;
    private final BufferedImage ghostTile;

    public TetrisDrawingHandler(Map<TetrominoType, BufferedImage> assets, RenderingHints renderingHints,BufferedImage ghostBlock) {
        this.assets = assets;
        this.renderingHints = renderingHints;
        boardImg = new BufferedImage(GameParameters.COLUMNS * GameParameters.BLOCK_SIZE,
                GameParameters.VISIBLE_ROWS * GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        this.ghostTile=ghostBlock;
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
                    int x = col * GameParameters.BLOCK_SIZE;
                    int y = (row - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
                    g2d.drawImage(assets.get(content), x, y, null);
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
    private void drawCurrentTetromino(Graphics2D g2d, Tetromino t,int ghostDistance) {

        //todo separate pixel coordinates from tetromino object
        int[] pixelCoordinates = calculatePixelCoordinates(t);
        for (int i = 0; i < pixelCoordinates.length; i += 2) {
            g2d.drawImage(assets.get(t.getType()), pixelCoordinates[i], pixelCoordinates[i + 1], null);
        }
        drawCurrentGhostBlock(g2d, pixelCoordinates, ghostDistance);
    }

    private void drawCurrentGhostBlock(Graphics2D g2d, int[] coordinate, int distance) {
        final var PIXEL_DIST = distance * GameParameters.BLOCK_SIZE;
        for (int i = 0; i < coordinate.length; i += 2) {
            g2d.drawImage(ghostTile, coordinate[i] , coordinate[i + 1] + PIXEL_DIST, null);
        }
    }

    /**
     * Calculate the pixel coordinates of the tetromino for drawing them based on its current state
     * and position. The method takes the state coordinates, scales them by the
     * block size, and then applies the position offset to get the final
     * pixel coordinates.
     */
    private int[] calculatePixelCoordinates(Tetromino tetromino) {
        int[] pixelCoordinates = new int[2 * tetromino.getStateCord().size()];
        final int pX = tetromino.getPositionX() * GameParameters.BLOCK_SIZE;
        final int pY = (tetromino.getPositionY() - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
        for (int i = 0; i < tetromino.getStateCord().size(); i++) {
            final int x = tetromino.getStateCord().get(i).x() * GameParameters.BLOCK_SIZE + pX;
            final int y = tetromino.getStateCord().get(i).y() * GameParameters.BLOCK_SIZE + pY;
            pixelCoordinates[i * COORDS_PER_BLOCK] = x;
            pixelCoordinates[(i * COORDS_PER_BLOCK) + 1] = y;
        }
        return pixelCoordinates;
    }

    @Override
    public void paint(Graphics2D g, @Nullable GameSnapshot snapshot, int width, int height) {
        if (snapshot == null) {
            return;
        }
        final BoardView board = snapshot.boardView();
        final boolean wasBoardDirty = snapshot.isBoardDirty();
        paintGameBoard(g, board, wasBoardDirty);
        snapshot.currentTetromino().ifPresent(tetromino -> drawCurrentTetromino(g, tetromino,snapshot.distance()));
    }
}

