package org.sehes.tetris.model;

import java.util.EnumMap;
import java.util.List;

public class ShapeProvider {
    private static final EnumMap<TetrominoType, EnumMap<Orientation, List<Coordinate>>> tetrominoStates = new EnumMap<>(
            TetrominoType.class);
    static {
        for (TetrominoType type : TetrominoType.values()) {
            tetrominoStates.put(type, new EnumMap<>(Orientation.class));
        }
        tetrominoStates.get(TetrominoType.I).put(Orientation.NORTH, List.of(
                new Coordinate(0, 0),
                new Coordinate(1, 0),
                new Coordinate(-1, 0),
                new Coordinate(2, 0)));
        tetrominoStates.get(TetrominoType.I).put(Orientation.EAST, List.of(
                new Coordinate(1, 1),
                new Coordinate(1, -1),
                new Coordinate(1, 0),
                new Coordinate(1, 2)));
        tetrominoStates.get(TetrominoType.I).put(Orientation.SOUTH, List.of(
                new Coordinate(0, 1),
                new Coordinate(-1, 1),
                new Coordinate(1, 1),
                new Coordinate(2, 1)));
        tetrominoStates.get(TetrominoType.I).put(Orientation.WEST, List.of(
                new Coordinate(0, 0),
                new Coordinate(0, -1),
                new Coordinate(0, 1),
                new Coordinate(0, 2)));
        tetrominoStates.get(TetrominoType.O).put(Orientation.NORTH, List.of(
                new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.O).put(Orientation.EAST, List.of(
                new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.O).put(Orientation.SOUTH, List.of(
                new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.O).put(Orientation.WEST, List.of(
                new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.J).put(Orientation.NORTH, List.of(
                new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0)));
        tetrominoStates.get(TetrominoType.J).put(Orientation.EAST, List.of(
                new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1)));
        tetrominoStates.get(TetrominoType.J).put(Orientation.SOUTH, List.of(
                new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0)));
        tetrominoStates.get(TetrominoType.J).put(Orientation.WEST, List.of(
                new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.L).put(Orientation.NORTH, List.of(
                new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, -1)));
        tetrominoStates.get(TetrominoType.L).put(Orientation.EAST, List.of(
                new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)));
        tetrominoStates.get(TetrominoType.L).put(Orientation.SOUTH, List.of(
                new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(-1, 1)));
        tetrominoStates.get(TetrominoType.L).put(Orientation.WEST, List.of(
                new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(-1, -1)));
        tetrominoStates.get(TetrominoType.S).put(Orientation.NORTH, List.of(
                new Coordinate(1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(-1, 0)));
        tetrominoStates.get(TetrominoType.S).put(Orientation.EAST, List.of(
                new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.S).put(Orientation.SOUTH, List.of(
                new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(1, 0)));
        tetrominoStates.get(TetrominoType.S).put(Orientation.WEST, List.of(
                new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, 1)));
        tetrominoStates.get(TetrominoType.T).put(Orientation.NORTH, List.of(
                new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(-1, 0)));
        tetrominoStates.get(TetrominoType.T).put(Orientation.EAST, List.of(
                new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, -1)));
        tetrominoStates.get(TetrominoType.T).put(Orientation.SOUTH, List.of(
                new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0), new Coordinate(1, 0)));
        tetrominoStates.get(TetrominoType.T).put(Orientation.WEST, List.of(
                new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1), new Coordinate(0, 1)));
        tetrominoStates.get(TetrominoType.Z).put(Orientation.NORTH, List.of(
                new Coordinate(-1, -1), new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(1, 0)));
        tetrominoStates.get(TetrominoType.Z).put(Orientation.EAST, List.of(
                new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(0, 1)));
        tetrominoStates.get(TetrominoType.Z).put(Orientation.SOUTH, List.of(
                new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 0), new Coordinate(-1, 0)));
        tetrominoStates.get(TetrominoType.Z).put(Orientation.WEST, List.of(
                new Coordinate(-1, 1), new Coordinate(-1, 0), new Coordinate(0, 0), new Coordinate(0, -1)));
    }
    public static List<Coordinate> getTetrominoState(TetrominoType type, Orientation orientation) {
        return tetrominoStates.get(type).get(orientation);
    }
  private ShapeProvider() {}
}
