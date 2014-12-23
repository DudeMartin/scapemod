package org.scapemod.accessor;

/**
 * Created by luckruns0ut on 23/12/2014.
 */
public interface Region {
    Tile[][][] getTiles();

    InteractableObject[] getObjects();
}
