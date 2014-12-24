package org.scapemod.accessor;

public interface Tile {

    InteractableObject[] getObjects();

    Boundary getBoundary();

    Wall getWall();

    Floor getFloor();
}