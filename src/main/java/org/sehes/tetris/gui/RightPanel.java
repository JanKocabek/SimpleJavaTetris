package org.sehes.tetris.gui;

import org.sehes.tetris.controller.Observer;
import org.sehes.tetris.model.score.ScoreInfoDTO;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class RightPanel extends JPanel {
    private final ScorePanel scorePanel;
    private final PreviewWindow previewWindow;
    RightPanel() {
        super();
//        setOpaque(true);
//        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        scorePanel = new ScorePanel();
        previewWindow = new PreviewWindow();
        add(scorePanel);
        add(previewWindow);
    }

    public Observer<ScoreInfoDTO> getScoreObserver() {
        return scorePanel;
    }

}
