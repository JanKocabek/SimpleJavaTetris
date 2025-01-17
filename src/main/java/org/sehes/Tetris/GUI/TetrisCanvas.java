package org.sehes.Tetris.GUI;

import org.sehes.Tetris.Logic.GameBoard;
import org.sehes.Tetris.Logic.KeyListener;
import org.sehes.Tetris.Logic.TetrisDrawingHandler;
import org.sehes.Tetris.Logic.TetrominoFactory;

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
        setSize(200, 700);
        setLayout(null);
        setVisible(true);
        setBackground(Color.WHITE);
        //this need to be added to catch key events
        this.setFocusable(true);
        this.requestFocusInWindow();
        board = GameBoard.getInstance();
        this.addKeyListener(new KeyListener());
    }
    TetrominoFactory tetromino2 = new TetrominoFactory(50, 50, 30, 30);



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        TetrisDrawingHandler.initialize(g2d);
        TetrisDrawingHandler.drawingTetromino(g2d, board.drawTetromino());
        TetrisDrawingHandler.drawingTetromino(g2d, tetromino2.getRectangle());

    }


}

