package org.sehes.tetris.logic;

public final class GameParameters {

    private GameParameters() {
    }
    public static final int MOVE = 30;//how much pix are the rectangles move
    public static final int BLOCK_SIZE = 30;//size of one cell of GameBoard grid, also the size of one block of tetromino
    public static final int COLUMNS = 10;//number of cell in row (columns)
    public static final int ROWS = 22;//number of cell in column (rows)
    public static final int VISIBLE_ROWS = 20;//number of rows that are visible to player, the top 2 rows are for spawning new tetromino and are not visible to player
    public static final int STARTING_ROW = 0;//the row where new tetromino will spawn
    public static final int STARTING_COL = 4;//the column where new tetromino
    public static final int GAME_SPEED = 500;//the speed of the game, in milliseconds, the lower the faster
    public static final int[] STARTING_POS = {STARTING_COL, STARTING_ROW};
    public static final int ROW_OFFSET = ROWS - VISIBLE_ROWS;//the number of rows that are not visible to player, used for calculating the position of tetromino on the game board

}
