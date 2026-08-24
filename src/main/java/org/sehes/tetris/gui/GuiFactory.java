package org.sehes.tetris.gui;

import org.sehes.tetris.controller.GameSnapshot;
import org.sehes.tetris.controller.GameState;
import org.sehes.tetris.controller.Observer;
import org.sehes.tetris.model.score.ScoreInfoDTO;

import javax.swing.Painter;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;

import static org.sehes.tetris.config.GameParameters.BLOCK_SIZE;
import static org.sehes.tetris.config.GameParameters.COLUMNS;
import static org.sehes.tetris.config.GameParameters.VISIBLE_ROWS;

public class GuiFactory {
    public static final Dimension CANVAS_SIZE = new Dimension(BLOCK_SIZE * COLUMNS,
            BLOCK_SIZE * VISIBLE_ROWS);

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
            window.setLocationRelativeTo(null);
        });

        return new BasicGui(scoreUI, infoP, window, gameContainer);
    }

    public static WholeGui assembly(BasicGui gui, Painter<GameSnapshot> painter, KeyAdapter tetrisKeyAdapter) {
        TetrisCanvas canvas = assemblyCanvas(painter, tetrisKeyAdapter);
        gui.container().add(canvas, BorderLayout.CENTER);
        gui.container().revalidate();
        gui.container().repaint();
        gui.window().revalidate();
        gui.window().repaint();
        gui.window().pack();

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
        int topMain = 12;//space which made canvas and score panel at the same starting height
        int leftMain = 8;
        int bottomMain = 4;
        int rightName = 4;
        gbcContain.insets = new Insets(topMain, leftMain, bottomMain, rightName);
        pane.add(container, gbcContain);

        GridBagConstraints gbcScore = new GridBagConstraints();
        gbcScore.gridx = 1;//second column for score panel
        gbcScore.gridy = 0;
        gbcScore.weightx = 0.25;
        gbcScore.weighty = 0.0;
        gbcScore.gridwidth = 1;
        gbcScore.anchor = GridBagConstraints.NORTHEAST;
        gbcScore.fill = GridBagConstraints.NONE;
        // Insets provide padding. A 5px left inset here + 5px right inset on the
        // container creates a 10px gap between components.

        int topScore = 0;
        int leftScore = 0;
        int bottomScore = 0;
        int rightScore = leftMain;
        gbcScore.insets = new Insets(topScore, leftScore, bottomScore, rightScore);
        pane.add(scoreP, gbcScore);

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 1;//second row for info panel
        gbcInfo.weightx = 1.0;
        gbcInfo.weighty = 1.0;
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
        container.setPreferredSize(CANVAS_SIZE);
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
    private static TetrisCanvas assemblyCanvas(final Painter<GameSnapshot> painter, final KeyAdapter keyInputHandler) {
        final TetrisCanvas canvas = new TetrisCanvas(painter);
        canvas.addKeyListener(keyInputHandler);
        return canvas;
    }

    private static ScorePanel assemblyScoreUI() {
        /*creation of ScorePanel is putted here if their be any needs of additional assembly in the future */
        return new ScorePanel();
    }

    public record WholeGui(TetrisCanvas canvas, ScorePanel scoreUI, InfoPanel infoP, GameWindow window) {
        public Runnable exitAction() {
            return () -> window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
    }

    public record BasicGui(ScorePanel scoreUI, InfoPanel infoP, GameWindow window, GameContainer container) {
        public Observer<GameState> infoObserver() {
            return infoP.infoUpdateObserver();
        }

        public Observer<Integer> fpsObserver() {
            return infoP.fpsUpdateObserver();
        }

        public Observer<ScoreInfoDTO> scoreObserver() {
            return scoreUI;
        }
    }
}
