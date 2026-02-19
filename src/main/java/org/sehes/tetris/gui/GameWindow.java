package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameWindow extends JFrame {

    private final int canvasW;
    private final int canvasH;
    private final int canvasX; //the x coordinate of the top left corner of the canvas
    private final int canvasY; //the y coordinate of the top left corner of the canvas
    private TetrisCanvas tetrisCanvas = null;

    //private final FlowLayout layout = new FlowLayout();/if we added manager later
    public GameWindow(int width, int height) {
        super("Tetris");
        this.canvasW = width / 2;
        this.canvasH = height - 200;
        this.canvasX = width / 4;
        this.canvasY = 100;
        //basic setting for a window
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setPreferredSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
    }

    public void setCanvas(TetrisCanvas canvas) {
        if (this.tetrisCanvas != null) {
            remove(this.tetrisCanvas);
        }
        this.tetrisCanvas = canvas;
        tetrisCanvas.setBounds(canvasX, canvasY, canvasW, canvasH);
        add(tetrisCanvas);
        pack();
        setResizable(false);
        setVisible(true);
        SwingUtilities.invokeLater(tetrisCanvas::requestFocusInWindow);
    }
}
