package org.sehes.Tetris.Logic;

import java.awt.*;
import java.awt.geom.Rectangle2D;


public class TetrominoFactory {
    private final int SIZEREC = 30;
    private final Rectangle2D.Double rectangle;
    private final Color color;

    public TetrominoFactory(double x, double y) {
        rectangle = new Rectangle2D.Double(x, y, SIZEREC, SIZEREC);
        color = Color.BLUE;
    }

    public Color getColor() {
        return color;
    }

    public void move(double x, double y) {
        rectangle.x += x;
        rectangle.y += y;
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
