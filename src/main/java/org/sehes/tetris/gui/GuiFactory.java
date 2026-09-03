package org.sehes.tetris.gui;

import org.sehes.tetris.controller.GameSnapshot;
import org.sehes.tetris.controller.GameState;
import org.sehes.tetris.controller.Observer;
import org.sehes.tetris.model.TetrominoType;
import org.sehes.tetris.model.score.ScoreInfoDTO;

import javax.swing.Painter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;

import static org.sehes.tetris.config.GameParameters.COLUMNS;
import static org.sehes.tetris.config.GameParameters.VISIBLE_ROWS;
import static org.sehes.tetris.graphic.Config.BLOCK_SIZE;

public class GuiFactory {
    public static final Dimension CANVAS_SIZE = new Dimension(BLOCK_SIZE * COLUMNS, BLOCK_SIZE * VISIBLE_ROWS);
    public static final int GRID_COLUMNS = 2;

    private GuiFactory() {
    }

    public static gameUI assembly(Painter<GameSnapshot> TetrisPainter, KeyAdapter tetrisKeyAdapter,Painter<TetrominoType> previewPainter) {
        /*creation of ScorePanel is putted here if their be any needs of additional assembly in the future */

        final GameContainer gameContainer = assemblyGameContainer();
        final InfoPanel infoP = new InfoPanel();
        final PreviewCanvas previewWindow= new PreviewCanvas(previewPainter);
        final RightPanel rightPanel = new RightPanel(previewWindow);
        final MainPane mainPane = assemblyMainPane(gameContainer, rightPanel, infoP);
        final GameWindow window = new GameWindow(mainPane);
        final TetrisCanvas canvas = assemblyCanvas(TetrisPainter, tetrisKeyAdapter);
        gameContainer.add(canvas, BorderLayout.CENTER);
        gameContainer.revalidate();
        gameContainer.repaint();
        window.revalidate();
        window.repaint();
        window.pack();
        window.setLocationRelativeTo(null);

        return new gameUI(canvas, rightPanel.getScoreObserver(), infoP.infoUpdateObserver(), infoP.fpsUpdateObserver(), window, rightPanel.getPreviewObserver());
    }

    private static MainPane assemblyMainPane(final GameContainer container, RightPanel rightP, InfoPanel infoP) {
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
        int topMain = 11;//space which made canvas and score panel at the same starting height
        int leftMain = 8;
        int bottomMain = 4;
        int rightName = 4;
        gbcContain.insets = new Insets(topMain, leftMain, bottomMain, rightName);
        pane.add(container, gbcContain);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 1;//second column for right panel
        gbcRight.gridy = 0;
        gbcRight.weightx = 0.25;
        gbcRight.weighty = 1.0;
        gbcRight.gridwidth = 1;
        gbcRight.anchor = GridBagConstraints.NORTHEAST;
        gbcRight.fill = GridBagConstraints.NONE;

        int topScore = 0;
        int leftScore = 0;
        int bottomScore = 0;
        int rightScore = leftMain;
        gbcRight.insets = new Insets(topScore, leftScore, bottomScore, rightScore);
        pane.add(rightP, gbcRight);

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.gridx = 0;
        gbcInfo.gridy = 1;//second row for info panel
        gbcInfo.weightx = 1.0;
        gbcInfo.weighty = 0.0;
        gbcInfo.gridwidth = GRID_COLUMNS;
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

    public record gameUI(TetrisCanvas canvas, Observer<ScoreInfoDTO> scoreObserver, Observer<GameState> infoObserver,
                         Observer<Integer> fpsObserver, GameWindow window, Observer<TetrominoType> previewObserver) {
        public Runnable exitAction() {
            return () -> window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }

    }
}
