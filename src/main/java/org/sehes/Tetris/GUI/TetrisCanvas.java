package org.sehes.Tetris.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisCanvas extends JPanel implements KeyListener {
    private static TetrisCanvas instance;

    public static TetrisCanvas getInstance() {
        if (instance == null) {
            instance = new TetrisCanvas();
        }
        return instance;
    }

    private TetrisCanvas() {
        setSize(200,700);
        setLayout(null);
        setVisible(true);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("TEST", 20, 20);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
