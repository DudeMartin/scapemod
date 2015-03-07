package org.scapemod.accessor;

public interface IInteractableObject {

    IRenderableNode getRenderableNode();

    int getHash();

    int getPlane();

    int getX();

    int getY();
    
    int getRelativeX();
    
    int getRelativeY();
    
    int getHeight();

    int getOrientation();
}