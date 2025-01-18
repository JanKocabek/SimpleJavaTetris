package org.sehes.Tetris.Logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {
    private final GameBoard gameBoard = GameBoard.getInstance();

    @Override
    public void keyPressed(KeyEvent e) {
        int MOVESIZE = 30;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> gameBoard.movePiece(MOVESIZE, 0);
            case KeyEvent.VK_LEFT -> gameBoard.movePiece(-MOVESIZE, 0);
            case KeyEvent.VK_DOWN -> gameBoard.movePiece(0, MOVESIZE);
            case KeyEvent.VK_ENTER -> gameBoard.startGame();
        }
    }
}
