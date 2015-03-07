package org.scapemod.accessor;

public interface IModel extends IRenderableNode {

    int[] getXIndices();
    
    int[] getYIndices();
    
    int[] getZIndices();
    
    int[] getXVertices();

    int[] getYVertices();

    int[] getZVertices();
}