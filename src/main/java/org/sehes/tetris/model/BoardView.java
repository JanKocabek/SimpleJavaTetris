package org.sehes.tetris.model;

public interface BoardView {

    int getWidth();

    int getHeight();

    BlockContent getBlockContent(int row, int column);

}
