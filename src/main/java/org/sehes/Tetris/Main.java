package org.sehes.tetris;

import javax.swing.SwingUtilities;

import org.sehes.tetris.gui.GameWindow;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::getInstance);
    }

}