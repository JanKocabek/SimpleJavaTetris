package org.sehes.tetris.gui;

import java.awt.LayoutManager2;

import javax.swing.JPanel;

public class GameContainer extends JPanel {

    GameContainer(LayoutManager2 lm) {
        super();

        setOpaque(true);
        setLayout(lm);

    }
}
