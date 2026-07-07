package org.sehes.tetris;

import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.GameSnapshot;
import org.sehes.tetris.controller.GameState;
import org.sehes.tetris.controller.GameStateManager;
import org.sehes.tetris.controller.input.TetrisKeyInputHandler;
import org.sehes.tetris.graphic.AssetsManager;
import org.sehes.tetris.graphic.RenderingHintsFactory;
import org.sehes.tetris.gui.GuiFactory;
import org.sehes.tetris.gui.TetrisCanvas;
import org.sehes.tetris.gui.TetrisDrawingHandler;
import org.sehes.tetris.model.TetrominoType;

import javax.swing.Painter;
import javax.swing.SwingWorker;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.sehes.tetris.gui.GuiFactory.assembly;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    final GameStateManager stateManager = new GameStateManager(GameState.INIT);
    private final GameManager gameManager = new GameManager(stateManager);

    public void run() {

        final GuiFactory.BasicGui basicGui = assembly();
        stateManager.addObserver(basicGui.infoP().infoUpdateObserver());
        gameManager.addFpsUpdateObserver(basicGui.infoP().fpsUpdateObserver());
        final var initCanvas = new CanvasWorker(basicGui);
        initCanvas.execute();
    }

    private class CanvasWorker extends SwingWorker<Map<TetrominoType, BufferedImage>, Void> {
        private final GuiFactory.BasicGui gui;
        private final AssetsManager assetsManager;
        private final RenderingHints qualityRenderingHints;

        public CanvasWorker(GuiFactory.BasicGui gui) {
            this.gui = gui;
            qualityRenderingHints = RenderingHintsFactory.qualityRenderingHints();
            this.assetsManager = new AssetsManager(qualityRenderingHints);
        }


        @Override
        protected Map<TetrominoType, BufferedImage> doInBackground() {
            return assetsManager.createAssets();
        }


        @Override
        protected void done() {
            try {
                final var assets = get();
                final Painter<GameSnapshot> painter = new TetrisDrawingHandler(assets, qualityRenderingHints);
                final KeyAdapter keyInputHandler = new TetrisKeyInputHandler(gameManager);
                final TetrisCanvas canvas = GuiFactory.assemblyCanvas(painter, keyInputHandler);
                final GuiFactory.WholeGui wholeGui = GuiFactory.assembly(this.gui, canvas);
                gameManager.prepareGame(wholeGui);


            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.SEVERE, "Interrupted or execution exception in CanvasWorker", e);
                Thread.currentThread().interrupt();
            }
        }

    }
}


