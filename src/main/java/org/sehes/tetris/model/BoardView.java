package org.sehes.tetris.model;

public interface BoardView {

    int getWidth();

    int getHeight();

    TetrominoType getBlockContent(int row, int column);

}
