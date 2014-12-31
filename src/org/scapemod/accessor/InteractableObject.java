package org.scapemod.accessor;

public interface InteractableObject {

    int getHeight();

    int getOrientation();

    int getHash();

    int getX();

    int getY();
    
    RenderableNode getRenderableNode();
}