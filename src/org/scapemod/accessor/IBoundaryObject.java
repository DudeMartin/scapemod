package org.scapemod.accessor;

public interface IBoundaryObject {

    IRenderableNode getRenderableNode();

    int getHash();

    int getPlane();

    int getX();

    int getY();
}