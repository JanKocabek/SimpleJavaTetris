package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.TetrisKeyInputHandler;

public class GuiFactory {

    public static GameWindow createGUI(final GameManager gameManager, final TetrisDrawingHandler drawingHandler, final TetrisKeyInputHandler keyInputHandler) {
        final TetrisCanvas canvas = assemblyCanvas(drawingHandler, keyInputHandler, gameManager);
        final ScorePanel scoreUI = assemblyScoreUI();
        final GameContainer gameContainer = assembleyGameContainer(canvas);
        final MainPane mainPane = assemblyMainPane(gameContainer, scoreUI);
        return new GameWindow(mainPane, canvas, scoreUI);
    }

    private static MainPane assemblyMainPane(final GameContainer container, ScorePanel scoreP) {
        final MainPane pane = new MainPane(new GridBagLayout());
        GridBagConstraints gbcContain = makeGBC(GridBagConstraints.BOTH, 1, 1, 0, 0);
        pane.add(container, gbcContain);
        GridBagConstraints gbcScore = makeGBC(GridBagConstraints.HORIZONTAL, 0, 0, 1, 0);
        gbcScore.anchor = GridBagConstraints.NORTHWEST;
        pane.add(scoreP, gbcScore);
        return pane;
    }

    /**
     * Utility method to create GridBagConstraints with specified parameters.
     *
     * @param type The fill type for the constraints (e.g.,
     * GridBagConstraints.VERTICAL).
     * @param weightx The weight for the x-axis, determining how extra space is
     * distributed.
     * @param weighty The weight for the y-axis, determining how extra space is
     * distributed.
     * @param gridx The grid x-coordinate for the component.
     * @param gridy The grid y-coordinate for the component.
     * @return A GridBagConstraints object configured with the specified
     * parameters
     */
    private static GridBagConstraints makeGBC(final int type, final double weightx, final double weighty, final int gridx, final int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = type;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        return gbc;
    }

    private static GameContainer assembleyGameContainer(TetrisCanvas canvas) {
        final GameContainer container = new GameContainer();
        container.add(canvas, BorderLayout.CENTER);
        return container;
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
     * @param keyInputHandler The TetrisKeyInputHandler responsible for handling
     * keyboard input for the game.
     * @param window The GameWindow to which the canvas will be added.
     */
    private static TetrisCanvas assemblyCanvas(final TetrisDrawingHandler drawingHandler, final TetrisKeyInputHandler keyInputHandler, final GameManager gm) {
        final TetrisCanvas canvas = new TetrisCanvas(drawingHandler, gm);
        canvas.addKeyListener(keyInputHandler);
        return canvas;
    }

    private static ScorePanel assemblyScoreUI() {
        return new ScorePanel();
    }

    private GuiFactory() {
        /* This utility class should not be instantiated */
    }
}
