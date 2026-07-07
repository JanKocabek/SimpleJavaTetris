package org.sehes.tetris.controller;

public interface Observer<T> {
    void update(T state);
}
