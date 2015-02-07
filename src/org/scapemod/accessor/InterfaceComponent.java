package org.scapemod.accessor;

public interface InterfaceComponent extends Node {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getScrollX();

    int getScrollY();

    InterfaceComponent[] getComponents();

    boolean isHidden();

    int getParentId();

    int getId();

    int getBoundsIndex();

    int getIndex();

    int getTextureId();

    String getText();
}