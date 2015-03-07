package org.scapemod.accessor;

public interface IChatMessage extends ICacheableNode {

    int getChannel();
    
    String getMessage();
    
    String getSender();
}