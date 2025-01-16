package org.sehes.Tetris.GUI;

import java.awt.*;

public class GameWindow extends javax.swing.JFrame {
    private static GameWindow instance;
    private final int WIDTH = 400;
    private final int HEIGHT = 800;
    TetrisCanvas gameBoard = TetrisCanvas.getInstance();

    public static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;
    }

    //private final FlowLayout layout = new FlowLayout();/if we added manager later
    private GameWindow() {
        setTitle("Tetris");
        getContentPane().setBackground(Color.red);
        getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        gameBoard.setBounds(100, 50, 200, 700);
        add(gameBoard);
    }

}
