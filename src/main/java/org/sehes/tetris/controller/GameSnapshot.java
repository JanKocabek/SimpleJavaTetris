package org.sehes.tetris.controller;

import org.sehes.tetris.model.BoardView;
import org.sehes.tetris.model.Tetromino;

public record GameSnapshot(BoardView boardView, Tetromino currentTetromino, boolean isBoardDirty) {
}
