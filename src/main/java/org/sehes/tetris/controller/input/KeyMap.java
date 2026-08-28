package org.sehes.tetris.controller.input;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_V;
import static org.sehes.tetris.controller.input.InputAction.CANCEL;
import static org.sehes.tetris.controller.input.InputAction.CONFIRM;
import static org.sehes.tetris.controller.input.InputAction.HARD_DROP;
import static org.sehes.tetris.controller.input.InputAction.MOVE_DOWN;
import static org.sehes.tetris.controller.input.InputAction.MOVE_LEFT;
import static org.sehes.tetris.controller.input.InputAction.MOVE_RIGHT;
import static org.sehes.tetris.controller.input.InputAction.ROTATE_CCW;
import static org.sehes.tetris.controller.input.InputAction.ROTATE_CW;
import static org.sehes.tetris.controller.input.InputAction.TOGGLE_GHOST;

public class KeyMap implements KeyRebinding {
    /**
     * Default non-modifiable key mapping for the game
     *
     * @see InputAction
     * @see java.awt.event.KeyEvent
     *
     */
    private static final Map<Integer, InputAction> DEFAULT_KEY_MAP = Map.of(
            VK_ESCAPE, CANCEL,
            VK_ENTER, CONFIRM,
            VK_UP, ROTATE_CW,
            VK_A, ROTATE_CCW,
            VK_SPACE, HARD_DROP,
            VK_DOWN, MOVE_DOWN,
            VK_LEFT, MOVE_LEFT,
            VK_RIGHT, MOVE_RIGHT,
            VK_V, TOGGLE_GHOST);

    private final Map<Integer, InputAction> map;

    public KeyMap(Map<Integer, InputAction> map) {
        this.map = map;
    }

    public static KeyMap createDefault() {
        return new KeyMap(new HashMap<>(DEFAULT_KEY_MAP));
    }

    public Optional<InputAction> getAction(KeyDTO key) {
        final var action = map.get(key.keyCode());
        return Optional.ofNullable(action).filter(a -> a.triggersOnPress() == key.isPressed());
    }

    //todo: create check for warning when key rebind unbind action
    @Override
    public boolean keyRebind(int key, InputAction action) {
        map.put(key, action);
        return true;
    }

    @Override
    public void resetKeyBindings() {
        map.clear();
        map.putAll(DEFAULT_KEY_MAP);
    }

}
