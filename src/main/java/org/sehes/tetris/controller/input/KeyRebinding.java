package org.sehes.tetris.controller.input;

public interface KeyRebinding {

     /**
      * swap key for given action with new key
      * returns false if key rebinding unbound key
      * @param key new key replacement
      * @param action which action will be mapped to new key
      * @return true if key rebinding dont unbind key otherwise false
      */
     boolean keyRebind(int key, InputAction action);

     /**
      * reset all key bindings to default
      */
     void resetKeyBindings();
}
