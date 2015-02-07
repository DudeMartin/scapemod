package org.scapemod.accessor;

public interface BoundaryObject {

    RenderableNode getRenderableNode();

    int getHash();

    int getPlane();

    int getX();

    int getY();
}