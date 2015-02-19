package org.scapemod.accessor;

public interface INode {

    long getUid();

    INode getNext();

    INode getPrevious();
}