package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.sehes.tetris.controller.GameManager;

public class TetrisCanvas extends JPanel {

    private final GameManager gameManager;
    private final TetrisDrawingHandler drawingHandler;

    public TetrisCanvas(TetrisDrawingHandler drawingHandler, GameManager gameManager) {
        this.drawingHandler = drawingHandler;
        this.gameManager = gameManager;
        setLayout(null);
        setVisible(true);
        setBackground(Color.BLACK);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawingHandler.initialize(g2d);
        if (gameManager.getGameState() == GameManager.GameState.INITIALIZE) {
            return;
        }
        drawingHandler.drawGrid(g2d);
        drawingHandler.tetromino(g2d,gameManager.getBoard().getGrid(), gameManager.getBoard().getCurrentTetromino());
    }

    public void repaintCanvas() {
        repaint();
    }

}
