package org.sehes.tetris.gui;

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
    /*@Nullable
    private BufferedImage backgroundGrid = null;*/
    private final BufferedImage boardImg;
    private final Map<TetrominoType, BufferedImage> assets;
    private final RenderingHints renderingHints;

    public TetrisDrawingHandler(Map<TetrominoType, BufferedImage> assets, RenderingHints renderingHints) {
        this.assets = assets;
        this.renderingHints = renderingHints;
        boardImg = new BufferedImage(GameParameters.COLUMNS * GameParameters.BLOCK_SIZE,
                GameParameters.VISIBLE_ROWS * GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
    }

    //TODO: decided if we will use backgroundGrid or not
    /*@Nullable
    public BufferedImage getBackgroundGrid() {
        return backgroundGrid;
    }

    private void drawGrid() {
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
    }*/

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
    private void drawCurrentTetromino(Graphics2D g2d, Tetromino t) {

        //todo separate pixel coordinates from tetromino object
        int[] pixelCoordinates = t.getPixelCoordinates();
        for (int i = 0; i < pixelCoordinates.length; i += 2) {
            g2d.drawImage(assets.get(t.getType()), pixelCoordinates[i], pixelCoordinates[i + 1], null);
        }
    }


    @Override
    public void paint(Graphics2D g, @Nullable GameSnapshot snapshot, int width, int height) {
        if (snapshot != null) {
            final BoardView board = snapshot.boardView();
            final Tetromino currentTetromino = snapshot.currentTetromino();
            final boolean wasBoardDirty = snapshot.isBoardDirty();
            if (board != null) {
                paintGameBoard(g, board, wasBoardDirty);
                if (currentTetromino != null) {
                    drawCurrentTetromino(g, currentTetromino);
                }
            }
        }
    }

}
