package org.sehes.tetris;

import org.sehes.tetris.controller.GameManager;

/**
 * The Main class is the entry point of the Tetris game application. It is
 * responsible for initializing the game by creating a new instance of the
 * GameManager class.<br>
 * Game start from {@code GameManager} constructor.
 * 
 * @author Sehes
 * @version 0.5
 */
public class Main {

    public static void main(String[] args) {
        new GameManager();
    }
}