package org.sehes.tetris.controller;

import org.jspecify.annotations.NonNull;
import org.sehes.tetris.model.score.HardDropEvent;
import org.sehes.tetris.model.score.LockPieceEvent;
import org.sehes.tetris.model.score.ScoreEvent;
import org.sehes.tetris.model.score.SoftDropEvent;
import org.sehes.tetris.model.score.TSpin;
import org.sehes.tetris.model.score.scoreLineClearType;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements Observable<Integer> {

    private final List<Observer<Integer>> observers = new ArrayList<>();
    private int score;//this is the current game score
    private boolean isBackToBack;
    private int combo = -1;

    public ScoreManager() {
        isBackToBack = false;
    }

    public Observer<ScoreEvent> scoringObserver() {
        return this::onScoringEvent;
    }

    public Observer<GameState> gameStateObserver() {
        return this::onGameStateChange;
    }

    private void onScoringEvent(ScoreEvent event) {
        updateScore(event);
    }

    private void onGameStateChange(GameState state) {
        if (state == GameState.NEW_GAME) {
            resetScore();
        }
    }

    private void resetScore() {
        score = 0;
        notifyObservers(score);
    }

    public void updateScore(ScoreEvent scoreEvent) {
        this.score += switch (scoreEvent) {
            case SoftDropEvent(int cell) -> cell;
            case HardDropEvent(int cell) -> cell * 2;
            case LockPieceEvent(int clearedLines, TSpin tspin) -> lockPieceScoreCalculation(clearedLines, tspin);
        };
        notifyObservers(score);
    }

    private int lockPieceScoreCalculation(int clearedLines, TSpin tspin) {
        final boolean isDifficult = (clearedLines == 4 || tspin != TSpin.NONE);
        final boolean applyBonus = isDifficult && isBackToBack;
        isBackToBack = isDifficult;
        combo = (clearedLines != 0) ? ++combo : -1;

        int baseScore = switch (geLineClearType(clearedLines, tspin)) {
            case NONE -> 0;
            case SINGLE, MINI_T_SPIN_NO_LINES -> 100;
            case DOUBLE -> 300;
            case TRIPLE -> 500;
            case TETRIS, T_SPIN_SINGLE -> 800;
            case T_SPIN_DOUBLE -> 1200;
            case T_SPIN_TRIPLE -> 1600;
            case MINI_T_SPIN_SINGLE -> 200;
            case MINI_T_SPIN_DOUBLE, T_SPIN_NO_LINES -> 400;
        };
        int score= applyBonus ? (int) (baseScore * 1.5) : baseScore;
        return score+combo*50;
    }

    private scoreLineClearType geLineClearType(int clearedLines, @NonNull TSpin tspin) {
        return switch (tspin) {
            case NONE -> switch (clearedLines) {
                case 0 -> scoreLineClearType.NONE;
                case 1 -> scoreLineClearType.SINGLE;
                case 2 -> scoreLineClearType.DOUBLE;
                case 3 -> scoreLineClearType.TRIPLE;
                case 4 -> scoreLineClearType.TETRIS;
                default ->
                        throw new IllegalStateException("Invalid number of cleared Lines TSpin.NONE: %d, Error must happened in upfront calling  ".formatted(clearedLines));
            };
            case FULL -> switch (clearedLines) {
                case 0 -> scoreLineClearType.T_SPIN_NO_LINES;
                case 1 -> scoreLineClearType.T_SPIN_SINGLE;
                case 2 -> scoreLineClearType.T_SPIN_DOUBLE;
                case 3 -> scoreLineClearType.T_SPIN_TRIPLE;
                default ->
                        throw new IllegalStateException("Invalid number of cleared Lines with TSpin_FULL: %d, Error must happened in upfront calling  ".formatted(clearedLines));
            };
            case MINI -> switch (clearedLines) {
                case 0 -> scoreLineClearType.MINI_T_SPIN_NO_LINES;
                case 1 -> scoreLineClearType.MINI_T_SPIN_SINGLE;
                case 2 -> scoreLineClearType.MINI_T_SPIN_DOUBLE;
                default ->
                        throw new IllegalStateException("Invalid number of cleared Lines with TSpin_MINI: %d, Error must happened in upfront calling  ".formatted(clearedLines));
            };
        };
    }


    /**
     * Registers {@code Observer}. Held by strong reference — callers must
     * remove it via {@link #removeObserver} if the observer's owner
     * (e.g. a panel) is discarded before this manager.<br> Currently, observers
     * are registered once at startup and live for the app's lifetime.
     *
     * @param observer objects who want to be notified of score changes
     */
    @Override
    public void addObserver(Observer<Integer> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Integer> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Integer event) {
        observers.forEach(observer -> observer.update(event));
    }
}
