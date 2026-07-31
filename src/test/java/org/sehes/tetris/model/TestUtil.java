package org.sehes.tetris.model;

import org.sehes.tetris.config.GameParameters;

import java.util.List;

public class TestUtil {

    public static void printBoardState(BoardView boardView, Tetromino tetromino) {
        final char TETROMINO = 'T';
        final char BLOCK = '#';
        final char EMPTY = '.';
        final var tetrominoBoardCord = getTetrominoBoardCord(tetromino);

        for (int y = 0; y < boardView.getHeight(); y++) {
            for (int x = 0; x < boardView.getWidth(); x++) {
                final var curX = x;
                final var curY = y;
                final var isTetromino = tetrominoBoardCord.stream().anyMatch(c -> c.x() == curX && c.y() == curY);
                switch (boardView.getBlockContent(y, x)) {
                    case TetrominoType.NON -> {
                        if (isTetromino) System.out.print(TETROMINO);
                        else System.out.print(EMPTY);
                    }
                    case null, default -> {
                        if (isTetromino) throw new RuntimeException("Tetromino should be placed on empty block");
                        else System.out.print(BLOCK);
                    }
                }
            }
            System.out.println();
        }
        System.out.println("----");
    }


    public static List<Coordinate> getTetrominoBoardCord(Tetromino tetromino) {
        List<Coordinate> cord = tetromino.getStateCord();
        return cord.stream().map(c -> new Coordinate(c.x() + tetromino.getPositionX(), c.y() + tetromino.getPositionY())).toList();
    }

    public static void prepareBoard(GameBoard gameBoard) {
        gameBoard.fillBlockForTestOnly(3, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(3, 1, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(3, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(3, 5, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(4, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(4, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(4, 5, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 1, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 1, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 2, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 3, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 4, TetrominoType.T);
    }

    public static void prepareBoard2T(GameBoard gameBoard) {
        gameBoard.fillBlockForTestOnly(3, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(3, 1, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(3, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(3, 5, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(4, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(4, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(4, 5, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 2, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 3, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(5, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 3, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(6, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(7, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(7, 2, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(7, 3, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(7, 4, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(8, 0, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(8, 1, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(8, 2, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(8, 3, TetrominoType.T);
        gameBoard.fillBlockForTestOnly(8, 4, TetrominoType.T);
    }

//    public static GameBoard prepareBoard(TetrominoType[][] tetrominoType) {
//        GameBoard gameBoard = new GameBoard();
//        prepareBoard(gameBoard);
//        return gameBoard;
//    }

    /**
     *
     * @param boardDescription board describe as string of # for TetrominoType.NON and . for TetrominoType.I
     * @return
     */
    public static GameBoard prepareBoard(String boardDescription) {
        TetrominoType[][] board = new TetrominoType[GameParameters.ROWS][GameParameters.COLUMNS];
        int x = 0;
        int y = 0;
        String b=boardDescription.strip();
        for (var c : b.toCharArray()) {

            switch (c) {
                case '#' -> board[x][y] = TetrominoType.NON;
                case 'I' -> board[x][y] = TetrominoType.I;
                default -> {
                    continue;
                }
            }
            y++;
            if (y == GameParameters.COLUMNS) {
                y = 0;
                x++;
            }


        }
        return GameBoard.createGameBoard(board);
    }

}



