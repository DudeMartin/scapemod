package org.scapemod.accessor;

public interface Boundary {

    RenderableNode getRenderableNode();

    int getHash();

    int getPlane();

    int getX();

    int getY();
}