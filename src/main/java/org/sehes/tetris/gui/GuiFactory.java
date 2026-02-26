package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.TetrisKeyInputHandler;

public class GuiFactory {

    /**
     * Creates the main game window and assembles all the necessary components
     * for the Tetris game. This method initializes the game canvas, score
     * panel, and main pane, and then combines them into a cohesive GUI.<p>
     * The resulting GameWindow object encapsulates all these components and
     * provides a unified interface for managing the game's GUI.
     *
     * @param gameManager The GameManager responsible for managing the game
     * logic and state. comunicating with the canvas to update the game state
     * based on user input and game events.
     * @param drawingHandler The TetrisDrawingHandler responsible for rendering the game graphics on the canvas. It interacts with the GameManager to retrieve the current game state and draw the appropriate visuals based on that state.
     * @param keyInputHandler The TetrisKeyInputHandler responsible for handling keyboard input from the user. It listens for key events and communicates with the GameManager to update the game state accordingly, such as moving or rotating the Tetris pieces based on user input.
     * @return A GameWindow object that contains the assembled GUI components for the Tetris game, including the game canvas and score panel.
     */
    public static GameWindow createGUI(final GameManager gameManager, final TetrisDrawingHandler drawingHandler, final TetrisKeyInputHandler keyInputHandler) {
        final TetrisCanvas canvas = assemblyCanvas(drawingHandler, keyInputHandler, gameManager);
        final ScorePanel scoreUI = assemblyScoreUI();
        final GameContainer gameContainer = assemblyGameContainer(canvas);
        final MainPane mainPane = assemblyMainPane(gameContainer, scoreUI);
        return new GameWindow(mainPane, canvas, scoreUI);
    }

    private static MainPane assemblyMainPane(final GameContainer container, ScorePanel scoreP) {
        final MainPane pane = new MainPane(new GridBagLayout());

        GridBagConstraints gbcContain = new GridBagConstraints();
        gbcContain.gridx = 0;
        gbcContain.gridy = 0;
        gbcContain.weightx = 1.0;
        gbcContain.weighty = 1.0;
        gbcContain.fill = GridBagConstraints.BOTH;
        // These insets replace the EmptyBorder from GameContainer.
        // top=10, left=20, bottom=10, right=5
        gbcContain.insets = new java.awt.Insets(10, 20, 10, 5);
        pane.add(container, gbcContain);

        GridBagConstraints gbcScore = new GridBagConstraints();
        gbcScore.gridx = 1;
        gbcScore.gridy = 0;
        gbcScore.anchor = GridBagConstraints.NORTHWEST;
        gbcScore.fill = GridBagConstraints.HORIZONTAL;
        // Insets provide padding. A 5px left inset here + 5px right inset on the
        // container creates a 10px gap between components.
        int top = 10; // top padding to align with the container's top border
        int left = 5; // left padding to create space between the container and score panel
        int bottom = 10; // bottom padding to align with the container's bottom border
        int right = 10; // right padding to provide space on the right side of the score panel
        gbcScore.insets = new java.awt.Insets(top, left, bottom, right);
        pane.add(scoreP, gbcScore);
        return pane;
    }

    private static GameContainer assemblyGameContainer(TetrisCanvas canvas) {
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
        /*creation of ScorePanel is putted here if their be any needs of additional assembly in the future */
        return new ScorePanel();
    }

    private GuiFactory() {}
}
