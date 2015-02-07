package org.scapemod.accessor;

public interface Tile {

    InteractableObject[] getObjects();

    BoundaryObject getBoundary();

    WallDecoration getWall();

    FloorObject getFloor();
}