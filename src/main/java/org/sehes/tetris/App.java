package org.sehes.tetris;

import org.sehes.tetris.controller.GameManager;
import org.sehes.tetris.controller.GameSnapshot;
import org.sehes.tetris.controller.TetrisKeyInputHandler;
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

import static org.sehes.tetris.gui.GuiFactory.assembly;

public class App {
    private final GameManager gameManager = new GameManager();

    public void run() {
        final GuiFactory.BasicGui mainGUI = assembly();
        final SwingWorker<Map<TetrominoType, BufferedImage>, Void> initCanvas = new CanvasWorker(mainGUI);
        initCanvas.execute();
    }

    private class CanvasWorker extends SwingWorker<Map<TetrominoType, BufferedImage>, Void> {
        private final GuiFactory.BasicGui mainGUI;

        public CanvasWorker(GuiFactory.BasicGui mainGUI) {
            this.mainGUI = mainGUI;
        }

        @Override
        protected Map<TetrominoType, BufferedImage> doInBackground() {
            final RenderingHints qualityRenderingHints = RenderingHintsFactory.qualityRenderingHints();
            return AssetsManager.CreateAssets(qualityRenderingHints).getAssets();
        }

        @Override
        protected void done() {
            try {
                final var assets = get();
                final Painter<GameSnapshot> painter = new TetrisDrawingHandler(assets, RenderingHintsFactory.qualityRenderingHints());
                final KeyAdapter keyInputHandler = new TetrisKeyInputHandler(gameManager);
                final TetrisCanvas canvas = GuiFactory.assemblyCanvas(painter, keyInputHandler);
                final GuiFactory.WholeGui gui = GuiFactory.assembly(mainGUI, canvas);
                gameManager.prepareGame(gui);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(System.err);
                Thread.currentThread().interrupt();
            }
        }
    }
}
