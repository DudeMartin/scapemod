package org.scapemod.accessor;

import java.awt.*;

public interface Client {

    Canvas getCanvas();

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

    int[] getInterfaceComponentWidths();

    int[] getInterfaceComponentHeights();

    int[] getInterfaceComponentXPositions();

    int[] getInterfaceComponentYPositions();

    int getCameraX();

    int getCameraY();

    int getCameraZ();

    int getPlane();
}