package org.sehes.tetris.controller;

import org.jspecify.annotations.NonNull;
import org.sehes.tetris.model.score.HardDropEvent;
import org.sehes.tetris.model.score.LockPieceEvent;
import org.sehes.tetris.model.score.ScoreEvent;
import org.sehes.tetris.model.score.ScoreInfoDTO;
import org.sehes.tetris.model.score.ScoreLineClearType;
import org.sehes.tetris.model.score.SoftDropEvent;
import org.sehes.tetris.model.score.TSpin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScoreManager implements Observable<ScoreInfoDTO> {

    private static final byte COMBO_EMPTY = -1;
    private final List<Observer<ScoreInfoDTO>> observers = new CopyOnWriteArrayList<>();
    private int score;
    /** Whether a difficult clear has started a B2B chain. */
    private boolean isBackToBackChain;
    private int combo = COMBO_EMPTY;

    public ScoreManager() {
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
        combo = COMBO_EMPTY;
        isBackToBackChain = false;
        notifyObservers(new ScoreInfoDTO(score, ScoreLineClearType.NONE, combo, false));
    }

    public void updateScore(ScoreEvent scoreEvent) {
        ScoreLineClearType clearType = ScoreLineClearType.NONE;
        boolean backToBackBonus = false;
        int points = switch (scoreEvent) {
            case SoftDropEvent(int cell) -> cell;
            case HardDropEvent(int cell) -> cell * 2;
            case LockPieceEvent(int clearedLines, TSpin tspin) -> {
                clearType = geLineClearType(clearedLines, tspin);
                boolean difficultClear = isDifficultClear(clearedLines, tspin);
                backToBackBonus = difficultClear && isBackToBackChain;
                yield lockPieceScoreCalculation(clearType, clearedLines, difficultClear, backToBackBonus);
            }
        };
        score += points;
        notifyObservers(new ScoreInfoDTO(score, clearType, combo, backToBackBonus));
    }

    private int lockPieceScoreCalculation(ScoreLineClearType clearType, int clearedLines, boolean difficultClear, boolean applyBackToBackBonus) {
        updateBackToBackChain(clearedLines, difficultClear);
        combo = (clearedLines != 0) ? ++combo : COMBO_EMPTY;

        int baseScore = switch (clearType) {
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
        int score = applyBackToBackBonus ? (int) (baseScore * 1.5) : baseScore;
        return combo > 0 ? score + (combo * 50) : score;
    }

    private boolean isDifficultClear(int clearedLines, TSpin tspin) {
        return clearedLines == 4 || (clearedLines > 0 && tspin != TSpin.NONE);
    }

    private void updateBackToBackChain(int clearedLines, boolean difficultClear) {
        if (difficultClear) {
            isBackToBackChain = true;
        } else if (clearedLines > 0) {
            isBackToBackChain = false;
        }
    }

    private ScoreLineClearType geLineClearType(int clearedLines, @NonNull TSpin tspin) {
        return switch (tspin) {
            case NONE -> switch (clearedLines) {
                case 0 -> ScoreLineClearType.NONE;
                case 1 -> ScoreLineClearType.SINGLE;
                case 2 -> ScoreLineClearType.DOUBLE;
                case 3 -> ScoreLineClearType.TRIPLE;
                case 4 -> ScoreLineClearType.TETRIS;
                default ->
                        throw new IllegalStateException("Invalid number of cleared Lines TSpin.NONE: %d, Error must happened in upfront calling  ".formatted(clearedLines));
            };
            case FULL -> switch (clearedLines) {
                case 0 -> ScoreLineClearType.T_SPIN_NO_LINES;
                case 1 -> ScoreLineClearType.T_SPIN_SINGLE;
                case 2 -> ScoreLineClearType.T_SPIN_DOUBLE;
                case 3 -> ScoreLineClearType.T_SPIN_TRIPLE;
                default ->
                        throw new IllegalStateException("Invalid number of cleared Lines with TSpin_FULL: %d, Error must happened in upfront calling  ".formatted(clearedLines));
            };
            case MINI -> switch (clearedLines) {
                case 0 -> ScoreLineClearType.MINI_T_SPIN_NO_LINES;
                case 1 -> ScoreLineClearType.MINI_T_SPIN_SINGLE;
                case 2 -> ScoreLineClearType.MINI_T_SPIN_DOUBLE;
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
    public void addObserver(Observer<ScoreInfoDTO> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ScoreInfoDTO> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ScoreInfoDTO event) {
        observers.forEach(observer -> observer.update(event));
    }
}
