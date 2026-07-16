package org.sehes.tetris.KeysBehavior;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sehes.tetris.controller.InputHandler;
import org.sehes.tetris.controller.input.*;

import java.awt.event.KeyEvent;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class KeyHandlingTest {
    @Mock
    InputHandler handler;

    @Test
    void testKeyPipelining_wrongLine() {
        //arrange
        KeyMap map = KeyMap.createDefault();
        InputMapper mapper = new InputMapper(map);
        InputReceiver inputRouter = new InputRouter(mapper, handler);
        //act
        inputRouter.handleInput(new KeyDTO(KeyEvent.VK_ENTER, false));
        //assert
        verifyNoInteractions(handler);
    }

    @Test
    void testKeyPipelining_passed() {
        //arrange
        KeyMap map = KeyMap.createDefault();
        InputMapper mapper = new InputMapper(map);
        InputReceiver inputRouter = new InputRouter(mapper, handler);
        //act
        inputRouter.handleInput(new KeyDTO(KeyEvent.VK_ENTER, true));

        //assert
        verify(handler).handleInput(InputAction.CONFIRM);
    }
}
