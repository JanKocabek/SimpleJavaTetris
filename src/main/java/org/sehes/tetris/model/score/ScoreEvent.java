package org.sehes.tetris.model.score;

public sealed interface ScoreEvent permits SoftDropEvent, HardDropEvent, LockPieceEvent {
}


