package org.sehes.tetris.KeysBehavior;

import org.junit.jupiter.api.Test;
import org.sehes.tetris.controller.input.InputAction;
import org.sehes.tetris.controller.input.KeyDTO;
import org.sehes.tetris.controller.input.KeyMap;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

 class KeyMapRebindingTest {
    @Test
    void testKeyRebind() {
        //arrange
        final var defaultKey =KeyEvent.VK_ENTER;
        final var defaultKeyMap = new HashMap<Integer, InputAction>();
        final var action = InputAction.CONFIRM;
        defaultKeyMap.put(defaultKey, action);
        final var keyMap = new KeyMap(defaultKeyMap);
        //act
        final var remappedKey =KeyEvent.VK_Y;
        keyMap.keyRebind(remappedKey, action);
        //assert
        assertThat(keyMap.getAction(new KeyDTO(remappedKey, true))).isPresent().get().isEqualTo(action);
    }
}
