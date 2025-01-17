package org.sehes.Tetris.Logic;

import java.awt.*;

public class TetrisDrawingHandler {
    private TetrisDrawingHandler() {

    }

    public static void initialize(Graphics2D g2d) {
       RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       g2d.setRenderingHints(hints);

    }

    public static  void drawingTetromino(Graphics2D g2d, Shape shape) {
        g2d.setColor(Color.BLUE);
        g2d.fill(shape);
    }
}
