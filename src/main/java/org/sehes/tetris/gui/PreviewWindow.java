package org.sehes.tetris.gui;

import org.sehes.tetris.config.GameParameters;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class PreviewWindow extends JPanel {
    private static final Dimension WINDOW_SIZE = new Dimension(GameParameters.BLOCK_SIZE * 4, GameParameters.BLOCK_SIZE * 4);
    private static final Color backgroundColor = new Color(15, 15, 25);

    PreviewWindow() {
        this.setPreferredSize(WINDOW_SIZE);
        this.setMinimumSize(WINDOW_SIZE);
        this.setMaximumSize(WINDOW_SIZE);
        setBackground(backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
