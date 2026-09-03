package org.sehes.tetris.gui;

import org.sehes.tetris.controller.Observer;
import org.sehes.tetris.graphic.Config;
import org.sehes.tetris.model.TetrominoType;

import javax.swing.JPanel;
import javax.swing.Painter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PreviewCanvas extends JPanel {
    private static final Dimension WINDOW_SIZE = new Dimension(Config.PREVIEW_BLOCK_SIZE * 5, Config.PREVIEW_BLOCK_SIZE * 5);
    private static final Color backgroundColor = new Color(15, 15, 25);
    private final Observer<TetrominoType> previewObserver = this::onUpdatePreview;
    private final Painter<TetrominoType> painter;
    private TetrominoType preview;

    PreviewCanvas(Painter<TetrominoType> painter) {
        this.painter = painter;
        this.setPreferredSize(WINDOW_SIZE);
        this.setMinimumSize(WINDOW_SIZE);
        this.setMaximumSize(WINDOW_SIZE);
        setBackground(backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final var g2d = (Graphics2D) g;
        painter.paint(g2d, preview, getWidth(), getHeight());
    }

    public Observer<TetrominoType> previewObserver() {
        return previewObserver;
    }

    private void onUpdatePreview(TetrominoType tetrominoType) {
        preview = tetrominoType;
        repaint();
    }
}
