package org.sehes.tetris.controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameStateManager implements StateManager<GameState>, Observable<GameState> {
    private final List<Observer<GameState>> observers;
    private GameState currentState;

    public GameStateManager(GameState state) {
        this.observers = new CopyOnWriteArrayList<>();
        this.currentState = state;
    }

    @Override
    public GameState getState() {
        return this.currentState;
    }

    @Override
    public void setState(GameState state) {
        this.currentState = state;
        notifyObservers();
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
    public void notifyObservers( ) {
        this.observers.forEach(observer -> observer.update(currentState));
    }
}
