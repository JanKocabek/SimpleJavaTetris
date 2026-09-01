package org.sehes.tetris.gui;

import org.sehes.tetris.controller.Observer;
import org.sehes.tetris.model.TetrominoType;
import org.sehes.tetris.model.score.ScoreInfoDTO;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class RightPanel extends JPanel {
    private final ScorePanel scorePanel;
    private final PreviewWindow previewWindow;

    RightPanel(PreviewWindow previewWindow) {
        super();
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //    setBorder(new LineBorder(Color.ORANGE,2));
        scorePanel = new ScorePanel();
        this.previewWindow = previewWindow;
        add(scorePanel);

        add(previewWindow);
        add(Box.createVerticalGlue());
    }

    public Observer<TetrominoType> previewObserver() {
        return previewWindow.previewObserver();
    }

    public Observer<ScoreInfoDTO> getScoreObserver() {
        return scorePanel;
    }

}
