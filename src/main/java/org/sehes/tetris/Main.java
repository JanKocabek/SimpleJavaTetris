package org.sehes.tetris;

import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.TetrisKeyInputHandler;
import org.sehes.tetris.gui.TetrisDrawingHandler;

import javax.swing.SwingUtilities;

import static org.sehes.tetris.gui.GuiFactory.GuiComponents;
import static org.sehes.tetris.gui.GuiFactory.createGUI;


/**
 * The Main class is the entry point of the Tetris game. It creates and initializes the necessary components and
 * starts the game loop.
 *
 * @author Sehes
 * @version 0.11
 */

public class Main {

    public static void main(String[] args) {
        final TetrisDrawingHandler drawingHandler = new TetrisDrawingHandler();
        final GameManager gameManager = new GameManager(drawingHandler);
        final TetrisKeyInputHandler keyInputHandler = new TetrisKeyInputHandler(gameManager);
        final GuiComponents gui = createGUI(gameManager, drawingHandler, keyInputHandler);

        gameManager.prepareGame(gui);
        SwingUtilities.invokeLater(gui::showGui);

    }
}