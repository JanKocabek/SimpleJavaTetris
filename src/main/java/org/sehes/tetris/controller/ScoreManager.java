package org.sehes.tetris.controller;

import org.sehes.tetris.model.score.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements Observable<Integer> {

    private final List<Observer<Integer>> observers = new ArrayList<>();
    private int score;//this is the current game score
    private boolean isBackToBack;

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



        int baseScore = switch (getTypeOfScoring(clearedLines,tspin)) {
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
        return applyBonus ? (int) (baseScore * 1.5) : baseScore;
    }

    private ClearType getTypeOfScoring(int clearedLines, TSpin tspin){
        switch (tspin) {
            case NONE -> {
                return switch (clearedLines) {
                    case 1 -> ClearType.SINGLE;
                    case 2 -> ClearType.DOUBLE;
                    case 3 -> ClearType.TRIPLE;
                    case 4 -> ClearType.TETRIS;
                    default -> ClearType.NONE;
                };
            }
            case FULL -> {
                return switch (clearedLines) {
                    case 0 -> ClearType.T_SPIN_NO_LINES;
                    case 1 -> ClearType.T_SPIN_SINGLE;
                    case 2 -> ClearType.T_SPIN_DOUBLE;
                    case 3 -> ClearType.T_SPIN_TRIPLE;
                    default -> ClearType.NONE;
                };
            }
            case MINI -> {
                return switch (clearedLines) {
                    case 0 -> ClearType.MINI_T_SPIN_NO_LINES;
                    case 1 -> ClearType.MINI_T_SPIN_SINGLE;
                    case 2 -> ClearType.MINI_T_SPIN_DOUBLE;
                    default -> ClearType.NONE;
                };
            } default -> {
                return ClearType.NONE;
            }
        }
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
