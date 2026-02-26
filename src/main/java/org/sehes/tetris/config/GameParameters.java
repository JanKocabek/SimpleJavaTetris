package org.sehes.tetris.config;

import java.awt.Point;

/**
 * The GameParameters class is a utility class that defines various constants
 * used throughout the Tetris game. These constants include parameters for game
 * mechanics such as movement speed, block size, and the dimensions of the game
 * board, as well as GUI-related parameters for window and canvas dimensions. By
 * centralizing these values in a single class, it allows for easy configuration
 * and maintenance of the game's settings, ensuring consistency across different
 * components of the application.
 */
public final class GameParameters {

    private GameParameters() {
    }
    public static final int MOVE = 30;//how much pix are the rectangles move
    public static final int BLOCK_SIZE = 30;//size of one cell of GameBoard grid, also the size of one block of tetromino
    public static final int COLUMNS = 10;//number of cell in row (columns)
    public static final int ROWS = 22;//number of cell in column (rows)
    public static final int VISIBLE_ROWS = 20;//number of rows that are visible to player, the top 2 rows are for spawning new tetromino and are not visible to player
    public static final int GAME_SPEED = 1600;//the speed of the game, in milliseconds, the lower the faster
    public static final int HIDDEN_ROWS = ROWS - VISIBLE_ROWS;//the number of rows that are not visible to player, used for calculating the position of tetromino on the game board
    public static final Point SPAWN_POINT = new Point(COLUMNS / 2 - 1, 0);//the point where new tetromino spawns, it is in the middle of the top row of the game board
    //GUI parameters  */
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 800;
    private static final int CANVAS_TOP_MARGIN = 100;
    private static final int CANVAS_BOTTOM_MARGIN = 100;
    public static final int CANVAS_HEIGHT_MARGIN = CANVAS_TOP_MARGIN + CANVAS_BOTTOM_MARGIN;
    public static final int CANVAS_Y_OFFSET = 100;

}
