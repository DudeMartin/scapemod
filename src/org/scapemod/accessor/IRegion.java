package org.scapemod.accessor;

public interface IRegion {

    ITile[][][] getTiles();

    IInteractableObject[] getObjects();
}