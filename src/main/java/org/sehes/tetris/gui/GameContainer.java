package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GameContainer extends JPanel {

    private static final int RIGHT_BORDER_SIZE = 10;
    private static final int TOP_BORDER_SIZE = 10;
    private static final int LEFT_BORDER_SIZE = 20;
    private static final int BOTTOM_BORDER_SIZE = 10;

    GameContainer() {
        super();

        final Border border = BorderFactory.createEmptyBorder(TOP_BORDER_SIZE, LEFT_BORDER_SIZE, BOTTOM_BORDER_SIZE, RIGHT_BORDER_SIZE);
        final BorderLayout layout = new BorderLayout();
        setOpaque(true);
        setLayout(layout);
        setBorder(border);
        setBackground(Color.WHITE);
    }
}
