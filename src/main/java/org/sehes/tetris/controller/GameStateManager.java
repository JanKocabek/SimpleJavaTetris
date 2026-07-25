package org.sehes.tetris.controller;

import java.util.ArrayList;
import java.util.List;


public class GameStateManager implements StateManager<GameState>, Observable<GameState> {
    private final List<Observer<GameState>> observers;
    private GameState currentState;

    public GameStateManager(GameState state) {
        this.observers = new ArrayList<>();
        this.currentState = state;
    }

    @Override
    public GameState getState() {
        return this.currentState;
    }

    @Override
    public void setState(GameState state) {
        this.currentState = state;
        notifyObservers(state);
    }

    @Override
    public void addObserver(Observer<GameState> observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<GameState> observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(GameState event) {
        this.observers.forEach(observer -> observer.update(event));
    }
}
