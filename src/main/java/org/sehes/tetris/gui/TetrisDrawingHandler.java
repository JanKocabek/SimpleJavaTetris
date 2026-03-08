package org.sehes.tetris.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.BlockContent;
import org.sehes.tetris.model.IBoardView;
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
    private BufferedImage backGroundGrid = null;
    private BufferedImage boardImg = null;

    public void initialize(Graphics2D g2d) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);
    }

    public BufferedImage getGrid() {
        return backGroundGrid;
    }

    public void drawGrid() {
        backGroundGrid = new BufferedImage(GameParameters.COLUMNS * GameParameters.BLOCK_SIZE,
                GameParameters.VISIBLE_ROWS * GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = backGroundGrid.createGraphics();
        final int width = backGroundGrid.getWidth();
        final int height = backGroundGrid.getHeight();

        g2d.setColor(Color.YELLOW);
        for (int i = 1; i < GameParameters.COLUMNS; i++) {
            g2d.drawLine(GameParameters.BLOCK_SIZE * i, 0, GameParameters.BLOCK_SIZE * i, height);
        }
        for (int i = 1; i < GameParameters.VISIBLE_ROWS; i++) {
            g2d.drawLine(0, GameParameters.BLOCK_SIZE * i, width, GameParameters.BLOCK_SIZE * i);
        }
        g2d.dispose();
    }

    public void drawBoard(Graphics2D g2d, IBoardView boardView, boolean isBoardDirty) {
        if (boardImg == null) {
            boardImg = new BufferedImage(GameParameters.COLUMNS * GameParameters.BLOCK_SIZE,
                    GameParameters.VISIBLE_ROWS * GameParameters.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
        }
        if (isBoardDirty) {
            boardImg = reDrawBoard(boardView);
        }
        g2d.drawImage(boardImg, 0, 0, null);
    }

    private BufferedImage reDrawBoard(IBoardView boardView) {
        Graphics2D g2d = boardImg.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, boardImg.getWidth(), boardImg.getHeight());
        g2d.setComposite(AlphaComposite.SrcOver);
        for (int row = boardView.getHeight() - 1; row >= 0; row--) {
            for (int col = boardView.getWidth() - 1; col >= 0; col--) {
                BlockContent content = boardView.getBlockContent(row, col);
                if (content != BlockContent.EMPTY) {
                    g2d.setColor(content.getColor());
                    int x = (col) * GameParameters.BLOCK_SIZE;
                    int y = (row - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
                    g2d.fillRect(x, y, GameParameters.BLOCK_SIZE, GameParameters.BLOCK_SIZE);
                }
            }
        }
        g2d.dispose();
        return boardImg;
    }

    /**
     * Draws the current Tetromino piece based on the game board's state.
     * It renders the Tetromino piece based on its shape and position on the board.
     * The drawing handler interacts with the GameManager to retrieve necessary
     * information about the game state and ensures that the visual representation
     * of the game is accurate and up to date.
     * 
     * @param g2d the graphics context to draw on
     * @param t the Tetromino piece to draw
     */
    public void drawCurrentTetromino(Graphics2D g2d, Tetromino t) {
        if (t == null) {
            return;
        }
        g2d.setColor(t.getColor());
        int[] shapeCoordinates = t.getShape();
        Point position = calculateTetrominoPosition(t);
        for (int i = 0; i < shapeCoordinates.length; i += 2) {
            g2d.fillRect(position.x + (shapeCoordinates[i] * GameParameters.BLOCK_SIZE),
                    position.y + (shapeCoordinates[i + 1] * GameParameters.BLOCK_SIZE), GameParameters.BLOCK_SIZE,
                    GameParameters.BLOCK_SIZE);
        }
    }

    private Point calculateTetrominoPosition(Tetromino t) {
        int x = t.getPosition().x * GameParameters.BLOCK_SIZE;
        int y = (t.getPosition().y - GameParameters.HIDDEN_ROWS) * GameParameters.BLOCK_SIZE;
        return new Point(x, y);
    }
}
