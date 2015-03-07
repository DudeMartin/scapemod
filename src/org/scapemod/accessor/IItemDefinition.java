package org.scapemod.accessor;

public interface IItemDefinition extends ICacheableNode {

    String[] getGroundActions();
    
    String[] getInterfaceActions();
    
    String getName();
}