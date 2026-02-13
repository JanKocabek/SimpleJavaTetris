package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.sehes.tetris.logic.KeyListener;

public class TetrisCanvas extends JPanel {

    private static TetrisCanvas instance;

    public static TetrisCanvas getInstance() {
        if (instance == null) {
            instance = new TetrisCanvas();
        }
        return instance;
    }

    private TetrisCanvas() {
        setLayout(null);
        setVisible(true);
        setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        TetrisDrawingHandler.initialize(g2d);
        TetrisDrawingHandler.drawGrid(g2d);
        TetrisDrawingHandler.drawGame(g2d);
    }

}
