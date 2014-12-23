package org.scapemod.accessor;

public interface Region {

    Tile[][][] getTiles();

    InteractableObject[] getObjects();
}