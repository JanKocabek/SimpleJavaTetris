package org.sehes.tetris;

import org.sehes.tetris.controller.*;
import org.sehes.tetris.controller.input.*;
import org.sehes.tetris.graphic.AssetsManager;
import org.sehes.tetris.graphic.RenderingHintsFactory;
import org.sehes.tetris.gui.GuiFactory;
import org.sehes.tetris.gui.TetrisDrawingHandler;
import org.sehes.tetris.model.TetrominoType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.sehes.tetris.gui.GuiFactory.assembly;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private final GameStateManager stateManager = new GameStateManager(GameState.INIT);
    private final GameManager gameManager = new GameManager(stateManager);
    private final InputMapper inputMapper = new InputMapper(KeyMap.createDefault());
    private final InputReceiver inputRouter = new InputRouter(inputMapper, gameManager);
    private final KeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(inputRouter);

    public void run() {
        final GuiFactory.BasicGui basicGui = assembly();
        final var canvasWorker = new CanvasWorker(basicGui);
        canvasWorker.execute();
        addObserver(stateManager, basicGui.infoObserver());
        addObserver(gameManager.fpsObservable(), basicGui.fpsObserver());
    }

    /**
     * Adds an observer to the observable and returns a closeable that removes the observer when closed.
     * the closeable is for the future when Gui part will be closed before the application
     *
     * @param sender   the object who is sending the messages
     * @param receiver the object who needs receiving the messages
     * @param <T>      the type of the messages which will be sent
     * @return a closeable that removes the observer when closed
     */

    private <T> AutoCloseable addObserver(Observable<T> sender, Observer<T> receiver) {
        sender.addObserver(receiver);
        return () -> sender.removeObserver(receiver);
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
                final var painter = new TetrisDrawingHandler(assets, qualityRenderingHints);
                final GuiFactory.WholeGui wholeGui = GuiFactory.assembly(this.gui, painter, tetrisKeyAdapter);
                gameManager.prepareGame(wholeGui);
                wholeGui.window().setVisible(true);
                wholeGui.canvas().requestFocusInWindow();


            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.SEVERE, "Interrupted or execution exception in CanvasWorker", e);
                Thread.currentThread().interrupt();
            }
        }

    }

}


