package org.sehes.tetris.gui;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;

public class LoadingBar extends JProgressBar {
    final  Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    final Border insideBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    final Border barBorder = BorderFactory.createCompoundBorder(outerBorder, insideBorder);

    LoadingBar() {
        super();
        this.setVisible(true);
        this.setPreferredSize(new Dimension(300, 50));
        this.setMinimumSize(new Dimension(300, 50));
        this.setMaximum(100);
        this.setValue(0);
        this.setStringPainted(true);
        this.setBorder(barBorder);
    }


}
