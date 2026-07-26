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


        int baseScore = switch (clearedLines) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };
        return applyBonus ? (int) (baseScore * 1.5) : baseScore;
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
