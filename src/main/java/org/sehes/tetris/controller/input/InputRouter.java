package org.sehes.tetris.controller.input;

import org.sehes.tetris.controller.InputHandler;

public class InputRouter implements InputReceiver {

    private final InputMapper inputMapper;
    private final InputHandler inputHandler;


    public InputRouter(InputMapper inputMapper, InputHandler inputHandler) {
        this.inputMapper = inputMapper;
        this.inputHandler = inputHandler;
    }


    @Override
    public void handleInput(KeyDTO key) {
        inputMapper.getAction(key).ifPresent(inputHandler::handleInput);
    }


}
