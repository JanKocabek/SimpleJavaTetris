package org.sehes.tetris.controller.input;

import java.util.Optional;

public class InputMapper {

    private final KeyMap keyMap;

    public InputMapper(KeyMap map) {
        this.keyMap = map;
    }

    public Optional<InputAction> getAction(KeyDTO key) {
        return keyMap.getAction(key);
    }
}
