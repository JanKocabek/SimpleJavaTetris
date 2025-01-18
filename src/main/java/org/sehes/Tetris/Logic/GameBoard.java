package org.sehes.Tetris.Logic;

import org.sehes.Tetris.GUI.TetrisCanvas;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class GameBoard {
    private static final int MOVE = 30;
    private static GameBoard instance;
    private TetrisCanvas canvas;
    private double width;
    private double height;
    private TetrominoFactory tetromino;
    private final boolean[][] board;
    private final int delay;
    private final Timer gameLoopTimer;


    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public void init(TetrisCanvas canvas) {
        if (this.canvas == null) {
            this.canvas = TetrisCanvas.getInstance();
            width = canvas.getSize().width;
            height = canvas.getSize().height;
            tetromino = new TetrominoFactory(0, 0, 30, 30);
        }
    }

    private GameBoard() {
        board = new boolean[20][10];
        delay = 800;
        gameLoopTimer = new Timer(delay, gameLoopListener);
    }

    public Rectangle2D.Double drawTetromino() {
        return tetromino.getRectangle();
    }

    TetrominoFactory getCurrentTetromino() {
        return tetromino;
    }

    public void movePiece(int x, int y) {
        if (checkCollisions(x, y)) {
            tetromino.move(x, y);
            canvas.repaint();
        }
    }

    /**
     *
     * @param x value how much should tetromino piece move to right (+) or left (-)
     * @param y
     * @return true if it could move, false if not able to move
     */
    private boolean checkCollisions(int x, int y) {
        int dx = (int) (tetromino.getX() + x);
        int dy = (int) (tetromino.getY() + y);
        return dx >= 0 && dx < width && dy >= 0 && dy < height;
    }


    ActionListener gameLoopListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (checkCollisions(0, MOVE)) {

                movePiece(0, MOVE);
            } else gameLoopTimer.stop();
        }
    };

    public void startGame() {
        gameLoopTimer.start();

    }

}
