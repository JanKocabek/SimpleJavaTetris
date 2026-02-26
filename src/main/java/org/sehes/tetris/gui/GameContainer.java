package org.sehes.tetris.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class GameContainer extends JPanel {

    GameContainer() {
        super();

        final BorderLayout layout = new BorderLayout();
        setOpaque(true);
        setLayout(layout);
        // The border is now handled by GridBagConstraints.insets in GuiFactory
        setBackground(Color.red);
    }
}
