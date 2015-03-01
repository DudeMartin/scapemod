package org.scapemod.accessor;

public interface IWallDecoration {

    IRenderableNode getRenderableNode();

    int getHash();

    int getPlane();

    int getX();

    int getY();
    
    int getOrientation();
}