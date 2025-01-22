package org.sehes.Tetris;

import org.sehes.Tetris.GUI.GameWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::getInstance);
    }

}