package org.sehes.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class GameLoop {
    private static final int TIMER_DELAY = 1000; // 1 second delay
    private static final int DURATION = 60; // 1 minute duration
    private Timer timer;

    public GameLoop() {
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            private int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (count < DURATION) {
                    // Code to update game state and redraw the screen
                    System.out.println("Game loop running... Count: " + count);
                    count++;
                } else {
                    timer.stop();
                    System.out.println("Game loop finished.");
                }
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        GameLoop gameLoop = new GameLoop();
        try {
            Thread.sleep(TIMER_DELAY * DURATION); // Keep the main thread alive for the duration
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
}
