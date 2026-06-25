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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.sehes.tetris.gui.GuiFactory.assembly;

public class App {
    private final GameManager gameManager = new GameManager();

    public void run() {
        final GuiFactory.BasicGui basicGui = assembly();
        final var initCanvas = new CanvasWorker(basicGui);
        initCanvas.execute();
    }

    private class CanvasWorker extends SwingWorker<Map<TetrominoType, BufferedImage>, AssetsManager.Update> implements PropertyChangeListener {
        private final GuiFactory.BasicGui gui;
        private final int taskSize;
        private final AssetsManager as;
        private final RenderingHints qualityRenderingHints;

        public CanvasWorker(GuiFactory.BasicGui gui) {
            this.gui = gui;
            qualityRenderingHints = RenderingHintsFactory.qualityRenderingHints();
            this.as = new AssetsManager(qualityRenderingHints);
            this.taskSize = as.getTotal();
            this.as.addPropertyChangeListener(this);
        }


        @Override
        protected Map<TetrominoType, BufferedImage> doInBackground() {
            return as.createAssets();
        }

        /**
         * Receives data chunks from the {@code publish} method asynchronously on the
         * <i>Event Dispatch Thread</i>.
         *
         * <p>
         * Please refer to the {@link #publish} method for more details.
         *
         * @param chunks intermediate results to process
         * @see #publish
         *
         */
        @Override
        protected void process(List<AssetsManager.Update> chunks) {
            super.process(chunks);
            final Integer progress = chunks.getLast().progress();
            final String message = chunks.getLast().message();
            final var value = progress * 100 / taskSize;
            gui.dialog().updateLoading(value, message);
        }

        @Override
        protected void done() {
            try {
                final var assets = get();
                final Painter<GameSnapshot> painter = new TetrisDrawingHandler(assets, qualityRenderingHints);
                final KeyAdapter keyInputHandler = new TetrisKeyInputHandler(gameManager);
                final TetrisCanvas canvas = GuiFactory.assemblyCanvas(painter, keyInputHandler);
                as.removePropertyChangeListener(this);
                final GuiFactory.WholeGui wholeGui = GuiFactory.assembly(this.gui, canvas);
                gameManager.prepareGame(wholeGui);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(System.err);
                Thread.currentThread().interrupt();
            }
        }

        /**
         * This method gets called when a bound property is changed.
         *
         * @param evt A PropertyChangeEvent object describing the event source
         *            and the property that has changed.
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("progress") && evt.getNewValue() instanceof AssetsManager.Update update) {
                publish(update);
            }
        }
    }
}

