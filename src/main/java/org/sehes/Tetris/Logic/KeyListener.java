package org.sehes.Tetris.Logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> GameBoard.getInstance().movePiece(DirectionFlag.RIGHT);
            case KeyEvent.VK_LEFT -> GameBoard.getInstance().movePiece(DirectionFlag.LEFT);
            case KeyEvent.VK_DOWN -> GameBoard.getInstance().movePiece(DirectionFlag.DOWN);
            case KeyEvent.VK_UP -> GameBoard.getInstance().rotatePiece(DirectionFlag.ROTATE_R);
            case KeyEvent.VK_A -> GameBoard.getInstance().rotatePiece(DirectionFlag.ROTATE_L);
            case KeyEvent.VK_ENTER -> GameBoard.getInstance().startGame();
        }
    }
}
