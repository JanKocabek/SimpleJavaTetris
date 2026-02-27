package org.sehes.tetris.gui;

import java.awt.LayoutManager2;

import javax.swing.JPanel;

public class GameContainer extends JPanel {

    GameContainer(LayoutManager2 layoutManager) {
        super();

        setOpaque(true);
        setLayout(layoutManager);
    }
}
