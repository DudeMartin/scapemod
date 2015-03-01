package org.scapemod.accessor;

public interface ITile {

    IInteractableObject[] getObjects();

    IBoundaryObject getBoundaryObject();

    IWallDecoration getWallDecoration();

    IGroundDecoration getGroundDecoration();
}