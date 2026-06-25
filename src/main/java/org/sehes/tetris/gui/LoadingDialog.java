package org.sehes.tetris.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class LoadingDialog extends JDialog {
    private final JProgressBar progressBar;
    private final JLabel messageLabel = new JLabel();

    LoadingDialog(JFrame owner, String title, boolean modal, JProgressBar progressBar) {
        super(owner, title, modal);
        this.progressBar = progressBar;
        this.messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.add(progressBar, BorderLayout.CENTER);
        this.add(messageLabel, BorderLayout.PAGE_END);
    }


    public void updateLoading(int value, String message) {
        this.progressBar.setValue(value);
        this.messageLabel.setText(message);
    }


}
