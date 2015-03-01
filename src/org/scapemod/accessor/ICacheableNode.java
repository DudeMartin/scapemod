package org.scapemod.accessor;

public interface ICacheableNode extends INode {

    ICacheableNode getNext();
    
    ICacheableNode getPrevious();
}