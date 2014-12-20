package org.scapemod.accessor;

public interface Node {

    long getUid();
    
    Node getNext();
    
    Node getPrevious();
}