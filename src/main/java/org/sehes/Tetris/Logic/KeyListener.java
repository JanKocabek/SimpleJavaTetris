package org.sehes.Tetris.Logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        int MOVESIZE = 30;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> GameBoard.getInstance().movePiece(MOVESIZE, 0);
            case KeyEvent.VK_LEFT -> GameBoard.getInstance().movePiece(-MOVESIZE, 0);
            case KeyEvent.VK_DOWN -> GameBoard.getInstance().movePiece(0, MOVESIZE);
            case KeyEvent.VK_ENTER -> GameBoard.getInstance().startGame();
        }
    }
}
