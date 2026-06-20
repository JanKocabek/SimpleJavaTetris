package org.sehes.tetris.gui;

import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.controller.GameSnapshot;

import javax.swing.Painter;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;

public class GuiFactory {

    private GuiFactory() {
    }

    public static BasicGui assembly() {
        final ScorePanel scoreUI = assemblyScoreUI();
        final GameContainer gameContainer = assemblyGameContainer();
        final InfoPanel infoP = new InfoPanel();
        final MainPane mainPane = assemblyMainPane(gameContainer, scoreUI, infoP);
        final GameWindow window = new GameWindow(mainPane);
        SwingUtilities.invokeLater(() -> {
            window.pack();
            window.setVisible(true);
            window.requestFocusInWindow();
        });
        return new BasicGui(scoreUI, infoP, window, gameContainer);
    }

    public static WholeGui assembly(BasicGui gui, TetrisCanvas canvas) {
        gui.container().add(canvas, BorderLayout.CENTER);
        gui.container().revalidate();
        gui.container().repaint();
        gui.window().revalidate();
        gui.window().repaint();
        gui.window().pack();
        gui.window().setResizable(false);
        canvas.requestFocusInWindow();

        return new WholeGui(canvas, gui.scoreUI, gui.infoP, gui.window);
    }

    private static MainPane assemblyMainPane(final GameContainer container, ScorePanel scoreP, InfoPanel infoP) {
        final MainPane pane = new MainPane(new GridBagLayout());

        GridBagConstraints gbcContain = new GridBagConstraints();
        gbcContain.gridx = 0;
        gbcContain.gridy = 0;
        gbcContain.weightx = 0.75;
        gbcContain.weighty = 0.0;
        gbcContain.gridwidth = 1;
        gbcContain.anchor = GridBagConstraints.CENTER;
        gbcContain.fill = GridBagConstraints.BOTH;
        // These insets replace the EmptyBorder from GameContainer.
        // top=10, left=20, bottom=10, right=5
        gbcContain.insets = new Insets(12, 10, 10, 0);
        pane.add(container, gbcContain);

        GridBagConstraints gbcScore = new GridBagConstraints();
        gbcScore.gridx = 1;//second column for score panel
        gbcScore.gridy = 0;
        gbcScore.weightx = 0.25;
        gbcScore.weighty = 0.0;
        gbcScore.gridwidth = 1;
        gbcScore.anchor = GridBagConstraints.NORTHWEST;
        gbcScore.fill = GridBagConstraints.NONE;
        // Insets provide padding. A 5px left inset here + 5px right inset on the
        // container creates a 10px gap between components.
        int top = 0; // top padding to align with the container's top border
        int left = 5; // left padding to create space between the container and score panel
        int bottom = 10; // bottom padding to align with the container's bottom border
        int right = 10; // right padding to provide space on the right side of the score panel
        gbcScore.insets = new Insets(top, left, bottom, right);
        pane.add(scoreP, gbcScore);

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 1;//second row for info panel
        gbcInfo.weightx = 1.0;
        gbcInfo.weighty = 0.0;
        gbcInfo.gridwidth = 2;
        gbcInfo.gridheight = 1;
        gbcInfo.insets = new Insets(0, 0, 0, 0);
        gbcInfo.anchor = GridBagConstraints.SOUTHWEST;
        gbcInfo.fill = GridBagConstraints.BOTH;
        pane.add(infoP, gbcInfo);
        return pane;
    }

    private static GameContainer assemblyGameContainer() {
        GameContainer container = new GameContainer(new BorderLayout());
        container.setPreferredSize(GameParameters.CANVAS_SIZE);
        return container;
    }

    /**
     * Method to set the canvas for the game. The new canvas is positioned based
     * on the predefined coordinates and dimensions of the window, ensuring that
     * it is centered and appropriately sized within the game window. This
     * method is called when initializing the GUI to ensure that the canvas is
     * properly added to the window and displayed to the user.
     *
     * @param painter handler which is responsible for drawing stuff on the canvas
     * @param keyInputHandler The TetrisKeyInputHandler responsible for handling
     * keyboard input for the game.
     * @return the TetrisCanvas that has been added to the window
     */
    public static TetrisCanvas assemblyCanvas(final Painter<GameSnapshot> painter, final KeyAdapter keyInputHandler) {
        final TetrisCanvas canvas = new TetrisCanvas(painter);
        canvas.addKeyListener(keyInputHandler);
        return canvas;
    }

    private static ScorePanel assemblyScoreUI() {
        /*creation of ScorePanel is putted here if their be any needs of additional assembly in the future */
        return new ScorePanel();
    }

    public record WholeGui(TetrisCanvas canvas, ScorePanel scoreUI, InfoPanel infoP, GameWindow window) {
    }

    public record BasicGui(ScorePanel scoreUI, InfoPanel infoP, GameWindow window, GameContainer container) {
    }
}
