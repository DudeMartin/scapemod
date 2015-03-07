package org.scapemod.accessor;

public interface ICharacter extends IRenderableNode {

    String getOverheadText();

    int getAnimation();
    
    int getInteractingIndex();
    
    int getX();

    int getY();
    
    int getOrientation();
}