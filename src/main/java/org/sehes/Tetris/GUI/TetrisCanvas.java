package org.sehes.Tetris.GUI;

import org.sehes.Tetris.Logic.GameBoard;
import org.sehes.Tetris.Logic.KeyListener;

import javax.swing.*;
import java.awt.*;

public class TetrisCanvas extends JPanel {
    private static TetrisCanvas instance;
    private final GameBoard board;

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
        //this need to be added to catch key events
        this.setFocusable(true);
        this.requestFocusInWindow();
        board = GameBoard.getInstance();
        this.addKeyListener(new KeyListener());
    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        TetrisDrawingHandler.initialize(g2d);
        TetrisDrawingHandler.drawGrid(g2d);
        TetrisDrawingHandler.drawingTetromino(g2d, board.drawTetromino());
    }


}

