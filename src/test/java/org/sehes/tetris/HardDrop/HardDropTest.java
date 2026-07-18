package org.sehes.tetris.HardDrop;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.config.GameParameters;
import org.sehes.tetris.model.DirectionFlag;
import org.sehes.tetris.model.GameBoard;

import static org.assertj.core.api.Assertions.assertThat;

class HardDropTest {

    /**
     * This test verifies the hard drop functionality moves a tetromino to the bottom of the game board.
     * <br>
     * Mathematical parameters from {@link GameParameters}:
     * <ul>
     *   <li>Total board rows: {@link GameParameters#ROWS} = 22</li>
     *   <li>Visible rows: {@link GameParameters#VISIBLE_ROWS} = 20</li>
     *   <li>Hidden rows: {@link GameParameters#HIDDEN_ROWS} = ROWS - VISIBLE_ROWS = 2</li>
     *   <li>Spawn point: {@link GameParameters#SPAWN_POINT} = (x:4, y:1)</li>
     * </ul>
     * The tetromino spawns at y=1 (in the hidden rows, second row from top). After hard drop,
     * it should land at position y = VISIBLE_ROWS + initialY = 20 + 1 = 21, meaning it drops
     * 20 rows total to reach the bottom of the visible game area.
     * @see GameParameters
     */
    @Test
    void hardDropTest() {
        //arrange
        final var gameBoardHeight = GameParameters.VISIBLE_ROWS;
        final var gameBoard = new GameBoard();
        gameBoard.trySetNewTetromino();
        final var mino = gameBoard.getCurrentTetromino();
        final var yPos = mino.getPositionY();
        //act
        gameBoard.tryHardDrop();
        final var newYPos = mino.getPositionY();
        //assert
        assertThat(gameBoard.tryMovePiece(DirectionFlag.DOWN)).isFalse();
        assertThat(newYPos).isEqualTo(gameBoardHeight + yPos);
    }
}
