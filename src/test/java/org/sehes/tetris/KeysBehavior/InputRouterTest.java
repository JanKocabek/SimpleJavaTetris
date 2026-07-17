package org.sehes.tetris.KeysBehavior;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sehes.tetris.controller.InputHandler;
import org.sehes.tetris.controller.input.*;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InputRouterTest {

    @Mock
    InputHandler handler;

    @Test
    void testHandleInput_RightKey_onRightEdge() {
        //arrange
        final var expectedOutput = InputAction.CONFIRM;
        final var keyData = new HashMap<Integer, InputAction>();
        keyData.put(KeyEvent.VK_ENTER, expectedOutput);
        KeyMap keyMap = new KeyMap(keyData);
        InputMapper mapper = new InputMapper(keyMap);
        InputReceiver inputRouter = new InputRouter(mapper, handler);
        //act
        inputRouter.handleInput(new KeyDTO(KeyEvent.VK_ENTER, true));
        //assert
        verify(handler).handleInput(expectedOutput);
        verifyNoMoreInteractions(handler);
    }

    @Test
    void testHandleInput_onOppositeEdge() {
        //arrange
        final var expectedOutput = InputAction.ROTATE_CW;
        final var keyData = new HashMap<Integer, InputAction>();
        keyData.put(KeyEvent.VK_ENTER, expectedOutput);
        KeyMap keyMap = new KeyMap(keyData);
        InputMapper mapper = new InputMapper(keyMap);
        InputReceiver inputRouter = new InputRouter(mapper, handler);
        //act
        inputRouter.handleInput(new KeyDTO(KeyEvent.VK_ENTER, true));
        //assert
        verifyNoInteractions(handler);
    }

    @Test
    void testHandleInput_onNotMappedKey() {
        //arrange
        final var expectedOutput = InputAction.ROTATE_CW;
        final var keyData = new HashMap<Integer, InputAction>();
        keyData.put(KeyEvent.VK_ENTER, expectedOutput);
        KeyMap keyMap = new KeyMap(keyData);
        InputMapper mapper = new InputMapper(keyMap);
        InputReceiver inputRouter = new InputRouter(mapper, handler);
        //act
        inputRouter.handleInput(new KeyDTO(KeyEvent.VK_A, false));
        //assert
        verifyNoInteractions(handler);
    }

}