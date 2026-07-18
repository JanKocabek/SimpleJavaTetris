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

    @Test
    void testResetKeyBindingsRestoresDefaults() {
        // arrange: build default key map and create a KeyMap from it
        final var defaultKeyMap = KeyMap.createDefault();
        final var originalBindings = new HashMap<>(defaultKeyMap);
        final var keyMap = new KeyMap(defaultKeyMap);

        // act: rebind a key to a different mapping
        final var remappedKey = KeyEvent.VK_Y;
        final var remappedAction = InputAction.CONFIRM;
        keyMap.keyRebind(remappedKey, remappedAction);

        // verify the rebind took effect
        assertThat(keyMap.getAction(new KeyDTO(remappedKey, true))).isPresent().get().isEqualTo(remappedAction);

        // act: reset key bindings back to defaults
        keyMap.resetKeyBindings();

        // assert: all original bindings are restored
        originalBindings.forEach((key, originalAction) ->
                assertThat(keyMap.getAction(new KeyDTO(key, true))).isPresent().get().isEqualTo(originalAction)
        );

        // assert: remapped key is no longer bound if it was not part of the original defaults
        if (!originalBindings.containsKey(remappedKey)) {
            assertThat(keyMap.getAction(new KeyDTO(remappedKey, true))).isNotPresent();
        }
    }
}
