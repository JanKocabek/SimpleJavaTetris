package org.sehes.tetris.gui;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.TetrisKeyInputHandler;

public class GuiFactory {

    public static GameWindow createGUI(final GameManager gameManager, final TetrisDrawingHandler drawingHandler, final TetrisKeyInputHandler keyInputHandler) {
        final GameWindow window = new GameWindow(GameParameters.WINDOW_WIDTH, GameParameters.WINDOW_HEIGHT);
        final TetrisCanvas canvas = new TetrisCanvas(drawingHandler, gameManager);
        setCanvas(canvas, keyInputHandler, window);
        return window;
    }

    /**
     * Method to set the canvas for the game. The new canvas is positioned based
     * on the predefined coordinates and dimensions of the window, ensuring that
     * it is centered and appropriately sized within the game window. This
     * method is called when initializing the GUI to ensure that the canvas is
     * properly added to the window and displayed to the user.
     *
     * @param canvas The TetrisCanvas to be set as the current canvas for the
     * game.
     */
    private static void setCanvas(final TetrisCanvas canvas, final TetrisKeyInputHandler keyInputHandler, final GameWindow window) {
        final int canvasW = GameParameters.WINDOW_WIDTH / 2;
        final int canvasH = GameParameters.WINDOW_HEIGHT - GameParameters.CANVAS_HEIGHT_MARGIN;//
        final int canvasX = GameParameters.WINDOW_WIDTH / 4;
        final int canvasY = GameParameters.CANVAS_Y_OFFSET;
        canvas.setBounds(canvasX, canvasY, canvasW, canvasH);
        canvas.addKeyListener(keyInputHandler);
        window.setCanvas(canvas);
    }

    private GuiFactory() {
        /* This utility class should not be instantiated */
    }
}
