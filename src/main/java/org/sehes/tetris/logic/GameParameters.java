package org.sehes.tetris.logic;

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
    //
    //**
    //GUI parameters  */
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 800;
    public static final int CANVAS_WIDTH = WINDOW_WIDTH / 2;
    public static final int CANVAS_HEIGHT = WINDOW_HEIGHT - 200;
    public static final int CANVAS_X = WINDOW_WIDTH / 4;
    public static final int CANVAS_Y = 100;
}
