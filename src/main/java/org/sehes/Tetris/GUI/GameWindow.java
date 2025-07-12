package org.sehes.Tetris.GUI;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private static GameWindow instance;
    private final int WIDTH = 600;
    private final int HEIGHT = 800;
    private final int CWIDTH = WIDTH / 2;
    private final int CHEIGHT = HEIGHT - 200;
    private final int CANVASX = WIDTH / 4;
    private final int CANVASY = 100;
    private final TetrisCanvas tetrisCanvas;

    public static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;
    }

    //private final FlowLayout layout = new FlowLayout();/if we added manager later
    private GameWindow() {
        //basic setting for a window
        super("Tetris");
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        //adding my custom Jpanel
        tetrisCanvas = TetrisCanvas.getInstance();
        tetrisCanvas.setBounds(CANVASX, CANVASY, CWIDTH, CHEIGHT);
        add(tetrisCanvas);
        pack();//setting all size how I set
        setResizable(false);
        setVisible(true);
        SwingUtilities.invokeLater(tetrisCanvas::requestFocusInWindow);//ensure for focus on my Jpanel


    }

}
