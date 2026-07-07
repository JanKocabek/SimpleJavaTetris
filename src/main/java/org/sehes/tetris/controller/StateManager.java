package org.sehes.tetris.controller;

public interface  StateManager <T> {
    public  void setState(T state);
    public  T getState();
}
