package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GameContainer extends JPanel {

    GameContainer() {
        super();
        final int borderSize = 50;
        final Border border = BorderFactory.createEmptyBorder(0, borderSize, 0, borderSize);
        final BorderLayout layout = new BorderLayout();
        setOpaque(true);
        setLayout(layout);
        setBorder(border);
        setBackground(Color.WHITE);
    }
}
