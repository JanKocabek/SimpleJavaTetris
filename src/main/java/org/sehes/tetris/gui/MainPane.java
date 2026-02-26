package org.sehes.tetris.gui;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class MainPane extends JPanel {

    MainPane(LayoutManager layoutManager) {
        super(layoutManager);
        setOpaque(true);
        setBackground(Color.WHITE);
    }

}
