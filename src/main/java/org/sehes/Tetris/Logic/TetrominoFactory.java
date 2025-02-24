package org.sehes.Tetris.Logic;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TetrominoFactory {
    private final int SIZEREC = 30;
    private final Rectangle2D.Double rectangle;
    private final Color color;
    private final boolean[][] grid;
    private final int[] position;
    private final int[] STARTPOS = {4, -1};

    public TetrominoFactory() {
        rectangle = new Rectangle2D.Double(STARTPOS[0] * SIZEREC, STARTPOS[1] * SIZEREC, SIZEREC, SIZEREC);
        color = Color.BLUE;
        grid = new boolean[][]
                {
                        {true},
                };
        position = STARTPOS;
    }

    public Color getColor() {
        return color;
    }

    public void move(DirectionFlag flag) {
        rectangle.x += flag.getDCol() * SIZEREC;
        rectangle.y += flag.getDRow() * SIZEREC;
        position[0] += flag.getDCol();
        position[1] += flag.getDRow();
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public int[] getPosition() {
        return position;
    }


    public Rectangle2D.Double getRectangle() {
        return rectangle;
    }
}
