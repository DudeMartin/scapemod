package org.scapemod.accessor;

public interface IInterfaceComponent extends INode {

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    int getScrollX();

    int getScrollY();

    IInterfaceComponent[] getComponents();

    int[] getItemIds();
    
    int[] getItemStackSizes();
    
    boolean isHidden();

    int getParentId();

    int getId();

    int getBoundsIndex();

    int getIndex();

    int getTextureId();

    String getText();
}