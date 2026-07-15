package org.sehes.tetris.controller.input;

import java.util.Map;
import java.util.Optional;

import static java.awt.event.KeyEvent.*;
import static org.sehes.tetris.controller.input.InputAction.*;

public class KeyMap implements KeyRebinding {
    private static final Map<Integer, InputAction> DEFAULT_KEY_MAP =Map.of(
            VK_ESCAPE, CANCEL,
            VK_ENTER, CONFIRM,
            VK_UP, ROTATE_CW,
            VK_A, ROTATE_CCW,
            VK_SPACE, HARD_DROP,
            VK_DOWN, MOVE_DOWN,
            VK_LEFT, MOVE_LEFT,
            VK_RIGHT, MOVE_RIGHT);

private final Map<Integer, InputAction> map;
    public static KeyMap createDefault() {
        return new KeyMap(DEFAULT_KEY_MAP);
    }


    public KeyMap(Map<Integer, InputAction> map) {
        this.map = map;
    }

    public Optional<InputAction> getAction(KeyDTO key ) {
        final var action = map.get(key.keyCode());
        return Optional.ofNullable(action).filter(a -> a.pressedEdge() == key.isPressed());
    }

//todo:implement rebinding functionality
    @Override
    public void keyBind(int key) {

    }

    @Override
    public void keyUnbind(int key) {

    }
}
