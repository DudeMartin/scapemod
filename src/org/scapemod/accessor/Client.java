package org.scapemod.accessor;

public interface Client {

    int getLoginState();

    int getConnectionState();

    String getUsername();

    String getPassword();
    
    int[] getCurrentStats();
    
    int[] getBaseStats();
    
    int[] getStatExperiences();

    InterfaceComponent[][] getInterfaceComponents();

    HashTable getInterfaceComponentNodes();

    Npc[] getNpcs();

    Player[] getPlayers();
    
    CollisionMap[] getCollisionMaps();
}