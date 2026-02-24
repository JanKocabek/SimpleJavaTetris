package org.sehes.tetris.model.board;

public interface IBoardView {

    int getWidth();

    int getHeight();

    BlockContent getBlockContent(int x, int y);

}
