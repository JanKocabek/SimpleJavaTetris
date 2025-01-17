package org.sehes.Tetris.GUI;

import org.sehes.Tetris.Logic.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class TetrisCanvas extends JPanel {
    private static TetrisCanvas instance;

    public static TetrisCanvas getInstance() {
        if (instance == null) {
            instance = new TetrisCanvas();
        }
        return instance;
    }

    private TetrisCanvas() {
        setSize(200,700);
        setLayout(null);
        addKeyListener(new KeyHandler());
        setBackground(Color.WHITE);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
