package org.sehes.tetris.model;

public interface IBoardView {

    int getWidth();

    int getHeight();

    BlockContent getBlockContent(int row, int column);

}
