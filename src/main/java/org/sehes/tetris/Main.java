package org.sehes.tetris;

import org.sehes.tetris.controller.GameManager;

public class Main {

/**
 *
 * This class is responsible for launching the application. It creates an instance of GameManager and calls the startApp method to set up the game window and initialize the game components.
 *
 */

    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startApp();
        
    }

}
