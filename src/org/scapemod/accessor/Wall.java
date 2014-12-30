package org.scapemod.accessor;

public interface Wall {

    RenderableNode getRenderableNode();

    int getHash();

    int getPlane();

    int getX();

    int getY();
}