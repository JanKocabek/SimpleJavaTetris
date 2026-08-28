package org.sehes.tetris.config;

public enum GhostType {
    FULL,
    DASH,
    NONE;

    private static final GhostType[] VALUES = values();

    public GhostType next() {
        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}
