package org.sehes.tetris.controller.input;

import org.sehes.tetris.controller.InputHandler;

public class InputRouter implements InputReceiver {

    private final InputMapper inputMapper;
    private final InputHandler inputHandler;


    public InputRouter(InputMapper inputMapper, InputHandler inputHandler) {
        this.inputMapper = inputMapper;
        this.inputHandler = inputHandler;
    }


    /**
     * Handles the input forward to the InputHandler if key is mapped in mapper
     *
     * @param key the key taken as DTO object containing the key code and the edge on which it was fired
     *
     */
    @Override
    public void handleInput(KeyDTO key) {
        inputMapper.getAction(key).ifPresent(inputHandler::handleInput);
    }


}
