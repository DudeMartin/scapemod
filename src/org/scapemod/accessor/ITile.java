package org.scapemod.accessor;

public interface ITile {

    IInteractableObject[] getObjects();

    IBoundaryObject getBoundary();

    IWallDecoration getWall();

    IFloorObject getFloor();
}