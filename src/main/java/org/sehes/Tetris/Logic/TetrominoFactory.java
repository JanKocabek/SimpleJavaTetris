package org.sehes.Tetris.Logic;

import java.awt.geom.Rectangle2D;

public class TetrominoFactory {
    private final Rectangle2D.Double rectangle;

    public TetrominoFactory(double x, double y, double width, double height) {
        rectangle = new Rectangle2D.Double(x, y, width, height);
    }

    public void move(double x, double y) {
        rectangle.x+=x;
        rectangle.y+=y;
    }


    public Rectangle2D.Double getRectangle() {
        return rectangle;
    }

    public double getX() {
        return rectangle.x;
    }

    public double getY() {
        return rectangle.y;
    }
}
