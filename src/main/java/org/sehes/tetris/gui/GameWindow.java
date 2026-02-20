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

    /**
     * Constructs a new GameWindow with the specified width and height. The window is set up with a white background, a preferred size based on the provided dimensions, and a null layout. The canvas for drawing the game will be added later using the setCanvas method.
     * @param width The width of the game window.
     * @param height    The height of the game window.

     */
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

    /** 
     *  Method to set the canvas for the game. If a canvas already exists, it is removed before adding the new one. The new canvas is positioned based on the predefined coordinates and dimensions, and the window is packed and made visible. The canvas is also requested to gain focus to ensure it can receive user input.
     * @param canvas The TetrisCanvas to be set as the current canvas for the game.
     */
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
        SwingUtilities.invokeLater(tetrisCanvas::requestFocusInWindow);//request focus for the canvas to receive key events
    }
}
