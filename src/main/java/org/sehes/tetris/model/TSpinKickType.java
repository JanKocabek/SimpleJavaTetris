package org.sehes.tetris.model;

public enum TSpinKickType {
    NONE,            // No kick happened
    ORDINARY,        // A kick happened, but not the special 1×2 T-Spin kick
    T_SPIN_KICK      // The special +1/+2 or -1/+2 SRS kick
}
