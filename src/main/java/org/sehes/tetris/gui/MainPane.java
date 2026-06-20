package org.sehes.tetris.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.LayoutManager2;

public class MainPane extends JPanel {

    MainPane(LayoutManager2 lm) {
        super(lm);
        setOpaque(true);
        setBackground(Color.WHITE);

    }

}
