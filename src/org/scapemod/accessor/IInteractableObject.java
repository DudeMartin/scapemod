package org.scapemod.accessor;

public interface IInteractableObject {

    int getHeight();

    int getOrientation();

    int getHash();

    int getX();

    int getY();
    
    int getPlane();
    
    IRenderableNode getRenderableNode();
}